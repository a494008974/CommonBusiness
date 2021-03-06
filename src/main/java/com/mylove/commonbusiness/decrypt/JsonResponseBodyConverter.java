package com.mylove.commonbusiness.decrypt;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mylove.commonbusiness.utils.DesHelper;

import java.io.IOException;

import me.jessyan.armscomponent.commonres.utils.Contanst;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2018/7/24.
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson mGson;//gson对象
    private final TypeAdapter<T> adapter;

    /**
     * 构造器
     */
    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.mGson = gson;
        this.adapter = adapter;
    }

    /**
     * 转换
     *
     * @param responseBody
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        String response = responseBody.string();

        String result = "";
        for (int i=0; i<Contanst.strKey.length; i++){
            result = DesHelper.decrypt(response,Contanst.strKey[i]);
            if(!"fail".equals(result)){
                break;
            }
        }
        System.out.println("RESULT => "+result);
        try{
            return adapter.fromJson(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            responseBody.close();
        }
        return  null;
    }

}
