/*
 * Copyright 2018 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mylove.commonbusiness.webservice;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.Converter;
import com.yanzhenjie.andserver.framework.MessageConverter;
import com.yanzhenjie.andserver.framework.body.StringBody;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by YanZhenjie on 2018/9/11.
 */
@Converter
public class AppMessageConverter implements MessageConverter {
    private static final String TAG = "AppMessageConverter";
    @Override
    public ResponseBody convert(@NonNull Object output, @Nullable MediaType mediaType) {
        Log.e(TAG,"AppMessageConverter ==> convert ... return ResponseBody ");
        return new StringBody("success ...... ");
    }

    @Nullable
    @Override
    public <T> T convert(@NonNull InputStream stream, @Nullable MediaType mediaType, Type type) throws IOException {
        Log.e(TAG,"AppMessageConverter ==> convert ... return T ");
//        Charset charset = mediaType == null ? null : mediaType.getCharset();
//        if (charset == null) {
//            return JsonUtils.parseJson(IOUtils.toString(stream), type);
//        }
        return null;
    }
}