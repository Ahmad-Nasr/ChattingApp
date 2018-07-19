package com.nasr.ahmed.chattingapp.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nasr.ahmed.chattingapp.Activity.DialogsActivity;
import com.nasr.ahmed.chattingapp.BuildConfig;
import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.Dialog.SignUpDialog;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


public class SignInFragment extends Fragment{


    private static final String TAG = SignInFragment.class.getSimpleName();


    static final String APP_ID = BuildConfig.AppID;
    static final String AUTH_KEY = BuildConfig.AuthKey ;
    static final String AUTH_SECRET = BuildConfig.AuthSecret;
    static final String ACCOUNT_KEY = BuildConfig.AccountKey;


    private FragmentActivity hostingActivity;
    QBChatService chatService;

    @BindView(R.id.edt_name)
    EditText mEditTextName;
    @BindView(R.id.edt_password)
    EditText mEditTextPassword;
    @BindView(R.id.btn_sign_in)
    Button mButtonSignIn;
    @BindView(R.id.btn_sign_up)
    Button mButtonSignUp;


    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeQuickBloxFramework();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);


        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String username = mEditTextName.getText().toString();
               String password = mEditTextPassword.getText().toString();

               if (!username.equals("") && !password.equals("") )
               {
                   if (Common.isConnectedToInternet(getActivity())){
                       signIn(username, password);
                   }else {
                       Toast.makeText(getActivity(),"Check your Internet connectivity",Toast.LENGTH_SHORT).show();
                   }

               }else{
                   Toast.makeText(getActivity(),"Enter valid username and password",Toast.LENGTH_SHORT).show();
               }
           }

       });



        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SignUpDialog signUpDialog = new SignUpDialog();
               signUpDialog.show(hostingActivity.getSupportFragmentManager(),
                       "SignUp");

            }
        });
        return view;
    }



    private void initializeQuickBloxFramework() {

       if(getActivity() != null)
        QBSettings.getInstance().init(getActivity().getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

    }
    private void signIn(final String username, final String password) {
        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(hostingActivity).build();
        waitingDialog.setMessage("Please wait ...");
        waitingDialog.setCanceledOnTouchOutside(false);
        waitingDialog.show();

        QBUser qbUser = new QBUser(username, password);
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(getActivity(),"Signed in successfully",Toast.LENGTH_SHORT).show();
                mEditTextPassword.setText("");

                //launch DialogsActivity
                Intent intent = new Intent(hostingActivity, DialogsActivity.class);
                intent.putExtra(Common.EXTRA_CURRENT_USER_NAME,username);
                intent.putExtra(Common.EXTRA_CURRENT_USER_PASSWORD,password);

                startActivity(intent);
                waitingDialog.dismiss();
            }

            @Override
            public void onError(QBResponseException e) {
                waitingDialog.dismiss();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hostingActivity = (FragmentActivity) context;
    }
}
