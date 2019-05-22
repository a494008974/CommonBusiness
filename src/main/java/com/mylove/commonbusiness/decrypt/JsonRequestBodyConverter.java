package com.mylove.commonbusiness.decrypt;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/7/24.
 */

public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */

    public JsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        return null;
    }
//    @Override
//    public RequestBody convert(T value) throws IOException {
//        //加密
////        APIBodyData data = new APIBodyData();
////        Log.i("xiaozhang", "request中传递的json数据：" + value.toString());
////        data.setData(XXTEA.Encrypt(value.toString(), HttpConstant.KEY));
////        String postBody = gson.toJson(data); //对象转化成json
////        Log.i("xiaozhang", "转化后的数据：" + postBody);
////        return RequestBody.create(MEDIA_TYPE, postBody);
//        return RequestBody.create(MEDIA_TYPE,value);
//    }

}