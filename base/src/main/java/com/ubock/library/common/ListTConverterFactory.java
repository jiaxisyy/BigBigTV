package com.ubock.library.common;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 传弟复杂对象，如json,List<T>,使用参数为：@Body String jsonStr
 * Created by ChenGD on 2017-5-23.
 */

public class ListTConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    public static ListTConverterFactory create() {
        return new ListTConverterFactory();
    }

    private ListTConverterFactory() {

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                return value.toString();
            }
        };
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new Converter<String, RequestBody>() {
            @Override
            public RequestBody convert(String value) throws IOException {
                Buffer buffer = new Buffer();
                Writer writer = new OutputStreamWriter(buffer.outputStream(), "utf-8");
                writer.write(value);
                writer.close();
                return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
            }
        };
    }
}
