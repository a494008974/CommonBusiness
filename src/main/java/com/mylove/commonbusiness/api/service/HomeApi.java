package com.mylove.commonbusiness.api.service;


import io.reactivex.Observable;
import me.jessyan.armscomponent.commonservice.entity.AdResponse;
import me.jessyan.armscomponent.commonservice.entity.HomeResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.mylove.commonbusiness.api.Api.HOME_DOMAIN_NAME;
import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;


/**
 * Created by zhou on 2018/11/28.
 */

public interface HomeApi {

    @Headers({DOMAIN_NAME_HEADER + HOME_DOMAIN_NAME})
    @GET("/index.php/win8/getContentVer1")
    Observable<HomeResponse> getDataList(@Query("data") String data);

    @Headers({DOMAIN_NAME_HEADER + HOME_DOMAIN_NAME})
    @GET("/index/getPoster")
    Observable<AdResponse> getAD(@Query("data") String data);
}
