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

public class FloatTypeAdapter extends TypeAdapter<Float> {
    @Override
    public void write(JsonWriter out, Float value) {
        try {
            if (value == null){
                value = 0F;
            }
            out.value(value.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Float read(JsonReader in) {
        try {
            Float value;
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                Log.e("TypeAdapter", "null is not a number");
                return 0F;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean b = in.nextBoolean();
                Log.e("TypeAdapter", b + " is not a number");
                return 0F;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isFloat(str)){
                    return Float.parseFloat(str);
                } else {
                    Log.e("TypeAdapter", str + " is not a number");
                    return 0F;
                }
            } else {
                String str = in.nextString();
                value = Float.valueOf(str);
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
        return 0F;
    }
}
