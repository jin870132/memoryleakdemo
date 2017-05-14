package com.memoryleakdemo.king.memoryleakdemo.utils;

import android.content.Context;
import android.widget.Toast;

import static android.widget.Toast.makeText;

/**
 * Created by king on 2017/5/14.
 */

public class ToastUtils {

    private static Toast toast;

    public static void ShowToast(Context context, String text){
        if (toast==null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }else{
            toast.setText(text);
        }
        toast.show();
    }
}
