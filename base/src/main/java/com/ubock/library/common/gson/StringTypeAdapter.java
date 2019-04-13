package com.ubock.library.common.gson;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Created by ChenGD on 2017-9-4.
 */

public class StringTypeAdapter extends TypeAdapter<String> {
    @Override
    public void write(JsonWriter out, String value) {
        try {
            if (value == null){
                value = "";
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String read(JsonReader in){
        try {
            String value;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                value = "";
            } else {
                value = in.nextString();
            }
            return value;
        } catch (Exception e) {
            Log.e("TypeAdapter", e.getMessage(), e);
            try {
                in.skipValue();
            } catch (Exception e1) {
                Log.e("TypeAdapter", e1.getMessage(), e1);
            }
        }
        return "";
    }
}
