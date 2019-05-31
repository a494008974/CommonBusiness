package com.mylove.commonbusiness.common;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.jess.arms.mvp.IModel;
import com.mylove.commonbusiness.api.service.HomeApi;
import com.mylove.commonbusiness.utils.DesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonres.utils.Contanst;
import me.jessyan.armscomponent.commonsdk.core.RouterHub;
import me.jessyan.armscomponent.commonservice.entity.AdResponse;
import me.jessyan.armscomponent.commonservice.entity.HomeResponse;
import me.jessyan.armscomponent.commonservice.tvlauncher.service.XdsService;

public class CommonModel extends BaseModel {

    public CommonModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    public Observable<HomeResponse> fetchHomeData(){

        JSONObject jm = new JSONObject();
        try {
            jm.put("model", Contanst.model);
            jm.put("path", Contanst.firmware);
            jm.put("tem_index",Contanst.tem_index);
            jm.put("serial", "0001");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
        }
        System.out.println("fetchHomeData => "+jm.toString());
        String data = DesHelper.encrypt(jm.toString(), "win81688");

        return mRepositoryManager
                .obtainRetrofitService(HomeApi.class)
                .getDataList(data);
    }

    public Observable<AdResponse> fetchAD() {

        JSONObject jm = new JSONObject();
        try {
            jm.put("model", Contanst.model);
            jm.put("path", Contanst.firmware);
            jm.put("province", "");
            jm.put("city", "");

        } catch (JSONException e) {

        }
        System.out.println("fetchAD => "+jm.toString());
        String data = DesHelper.encrypt(jm.toString(), "home1688");

        return mRepositoryManager
                .obtainRetrofitService(HomeApi.class)
                .getAD(data);
    }
}
