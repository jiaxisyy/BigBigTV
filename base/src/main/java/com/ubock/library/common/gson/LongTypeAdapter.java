package com.ubock.library.common.gson;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import com.ubock.library.utils.NumberUtils;

/**
 * Created by ChenGD on 2017-9-4.
 */

public class LongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) {
        try {
            if (value == null){
                value = 0L;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long read(JsonReader in){
        try {
            Long value;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                Log.e("TypeAdapter", "null is not a number");
                return 0L;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean b = in.nextBoolean();
                Log.e("TypeAdapter", b + " is not a number");
                return 0L;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isInt(str)){
                    return Long.parseLong(str);
                } else {
                    Log.e("TypeAdapter", str + " is not a int number");
                    return 0L;
                }
            } else {
                value = in.nextLong();
                return value;
            }
        } catch (Exception e) {
            Log.e("TypeAdapter", e.getMessage(), e);
            try {
                in.skipValue();
            } catch (Exception e1) {
                Log.e("TypeAdapter", e1.getMessage(), e1);
            }
        }
        return 0L;
    }
}
