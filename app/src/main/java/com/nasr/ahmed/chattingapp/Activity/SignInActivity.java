package com.nasr.ahmed.chattingapp.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nasr.ahmed.chattingapp.Fragment.SignInFragment;
import com.nasr.ahmed.chattingapp.R;

public class SignInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SignInFragment.newInstance();
    }


}
