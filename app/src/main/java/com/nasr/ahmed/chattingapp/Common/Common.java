package com.nasr.ahmed.chattingapp.Common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.security.PublicKey;


public class Common {

    private final static String TAG = Common.class.getSimpleName();
    public static QBUser currentQbUser;

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
