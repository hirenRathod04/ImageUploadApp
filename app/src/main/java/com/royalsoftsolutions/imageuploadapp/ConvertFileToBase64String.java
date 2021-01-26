package com.royalsoftsolutions.imageuploadapp;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConvertFileToBase64String {
    public static String convertFileToBase64String(String filePath) {

        String base64 = "";
        try {
            File file = new File(filePath);
            byte[] buffer = new byte[(int) file.length() + 100];
            //@SuppressWarnings("resource")
            int length = new FileInputStream (file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static String convertBitmapToBase64String(Bitmap bmp) {

        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageBytes = baos.toByteArray();

            return Base64.encodeToString(imageBytes, Base64.DEFAULT);

        }
        else {
            return "";
        }
    }

    public static String convertBitmapToBase64StringWithQuality(Bitmap bmp, int quality) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        byte[] imageBytes = baos.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}

