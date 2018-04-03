package com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.Utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by user on 3/27/2018.
 */

public class ImageDbUtils {

    //This Method converts the Image gotten from the url to Byte Array
    //We need to do this cause we cannot really insert an Image directly into a data base like we do for Strings
    //we need the blob data type which returns data as it is stored
    public static byte[] getPictureByteArray(@Nullable Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
        //this method helps us to convert the Image from byteArray back to BitMap so that we will be able to display it in or ui
    }
    //convert byte array  back to bitmap
    public static Bitmap getBitmapFromByteArray(@Nullable byte[]Image){
        return BitmapFactory.decodeByteArray(Image,0, Image.length);

    }
    public void deleteImageFile(Context context, String filePath){
        // Get the file
        File imageFile = new File(filePath);

        // Delete the image
         imageFile.delete();


    }
}
