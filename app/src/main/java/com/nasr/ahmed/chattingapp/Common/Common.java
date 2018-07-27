package com.nasr.ahmed.chattingapp.Common;


import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.security.PublicKey;

import dmax.dialog.SpotsDialog;


public class Common {

    private final static String TAG = Common.class.getSimpleName();

    public static QBUser currentQbUser;

    public static final int SELECT_IMAGE_REQUEST =10;

    public static final String  EXTRA_CURRENT_USER_NAME ="name";
    public static final String  EXTRA_CURRENT_USER_PASSWORD ="password";
    public static final String  EXTRA_CHOOSEN_CHAT_DIALOG ="chatDialog";
    public static final String EXTRA_CHOOSEN_CHAT_USER = "userParticipant";


    public static final String  ARG_CURRENT_USER_NAME ="name";
    public static final String  ARG_CURRENT_USER_PASSWORD ="password";
    public static final String  ARG_CHOOSEN_CHAT_DIALOG ="chatDialog";


    //not good for use , become null ,pass its info through intent instead
    //public static  QBUser currentQBUser;




    //context must be passed to use such method
    //can't be intialized outside an application component
    //so set it as an argument
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {

            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting())
                return true;

        } catch (NullPointerException ex) { Log.e(TAG, ex.getMessage());}

        return false;
    }


}
