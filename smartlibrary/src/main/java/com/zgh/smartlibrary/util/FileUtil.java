package com.zgh.smartlibrary.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhuguuohui on 2016/8/19.
 */
public class FileUtil {
    public static String readRaw(Context context, String name) {
        int rawID = context.getResources().getIdentifier(name, "raw", context.getPackageName());
        if (rawID == 0) {
            return "";
        }
        InputStream inputStream = context.getResources().openRawResource(rawID);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[2048];
        String result = "";
        try {
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            result = outputStream.toString();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
