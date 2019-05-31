package com.mylove.commonbusiness.common;

import android.app.Application;
import android.content.Context;
import android.os.Message;

import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.mylove.commonbusiness.R;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.armscomponent.commonservice.dao.ClsBean;
import me.jessyan.armscomponent.commonservice.dao.DaoHelper;
import me.jessyan.armscomponent.commonservice.dao.InfoBean;
import me.jessyan.armscomponent.commonservice.entity.AdResponse;
import me.jessyan.armscomponent.commonservice.entity.HomeResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static me.jessyan.armscomponent.commonres.utils.Contanst.MSG_WHAT_LAUNCHER_DATA;
import static me.jessyan.armscomponent.commonres.utils.Contanst.MSG_WHAT_LAUNCHER_MSG;

public class CommonPresenter<M extends CommonContract.Model, V extends CommonContract.View> extends BasePresenter<M,V> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    public CommonPresenter(M model, V rootView) {
        super(model, rootView);
    }


    public void initLauncher(Context context){
        int init = DataHelper.getIntergerSF(context,"init");
        if(init == -1){
            String[] tags = context.getResources().getStringArray(R.array.tv_launcher_tags);
            String[] pkgs = context.getResources().getStringArray(R.array.tv_launcher_pkgs);
            List<InfoBean> infoBeans = new ArrayList<InfoBean>();
            for (int i=0; i<tags.length; i++){
                InfoBean infoBean = new InfoBean();
                infoBean.setTag(tags[i]);
                if (i<pkgs.length){
                    infoBean.setPkg(pkgs[i]);
                }else{
                    infoBean.setPkg("");
                }
                infoBeans.add(infoBean);
            }
            DaoHelper.saveInfo(infoBeans);
            DataHelper.setIntergerSF(context,"init",1);
        }
    }


    public void fetchHomeData(){
        if(mModel != null){
            mModel.fetchHomeData()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BlockingBaseObserver<HomeResponse>() {
                        @Override
                        public void onNext(HomeResponse homeResponse) {
                            try{
                                List<ClsBean> clsBeans = homeResponse.getInfo().getData().getCls();
                                DaoHelper.saveCls(clsBeans);
                                List<InfoBean> infoBeans = homeResponse.getInfo().getData().getInfo();
                                DaoHelper.saveInfo(infoBeans);

                                Message msg = Message.obtain();
                                msg.what = MSG_WHAT_LAUNCHER_DATA;
                                EventBus.getDefault().post(msg);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    public void fetchAD(){
        if(mModel != null){
            mModel.fetchAD()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BlockingBaseObserver<AdResponse>() {
                        @Override
                        public void onNext(AdResponse adResponse) {
                            try{
                                if(adResponse != null){
                                    Message msg = Message.obtain();
                                    msg.what = MSG_WHAT_LAUNCHER_MSG;
                                    msg.obj = adResponse;
                                    EventBus.getDefault().post(msg);
                                }
                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}
