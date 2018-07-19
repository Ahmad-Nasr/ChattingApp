package com.nasr.ahmed.chattingapp.Dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;

public class SignUpDialog extends DialogFragment{

    //because getActivity() may return null
    private Context hostingContext;

    QBUser newQBuser ;

    @BindView(R.id.edt_new_user_name)  EditText mEditTextNewUserName;
    @BindView(R.id.edt_new_user_password) EditText mEditTextNewUserPassword;
    @BindView(R.id.btn_choose_avatar)Button mButtonChooseAvatar;
    @BindView(R.id.btn_upload)Button mButtonUpload;
    @BindView(R.id.avatar_image)ImageView mImageViewAvatarImage;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(hostingContext);
        alertDialogBuilder.setCancelable(false);

        //Custom sign up view
        LayoutInflater inflater = ((FragmentActivity)hostingContext).getLayoutInflater();
        View sign_up_view = inflater.inflate(R.layout.dialog_sign_up,null);
        alertDialogBuilder.setView(sign_up_view);
        ButterKnife.bind(this, sign_up_view);


        mButtonChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent selectImageIntent = new Intent();
                selectImageIntent.setType("image/*");
                selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(selectImageIntent, Common.SELECT_IMAGE_REQUEST);
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //later_not supported
                Toast.makeText(hostingContext,"Under development",Toast.LENGTH_SHORT).show();
            }
        });



        alertDialogBuilder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String newUserName = mEditTextNewUserName.getText().toString();
                String newUserPassword = mEditTextNewUserPassword.getText().toString();

                if (!newUserName.equals("") && !newUserPassword.equals("") )
                {
                    if (Common.isConnectedToInternet(getActivity())){
                        signUp(newUserName, newUserPassword);
                    }else {
                        Toast.makeText(hostingContext,"Check your Internet connectivity",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(hostingContext,"Enter valid username and password",Toast.LENGTH_SHORT).show();
                }

                dialogInterface.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });




        return alertDialogBuilder.create();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK){
            if(requestCode == Common.SELECT_IMAGE_REQUEST){
                Uri selectedImageUri = intent.getData();
                Picasso.get()
                        .load(selectedImageUri)
                        .resize(100,100)
                        .centerCrop()
                        .into(mImageViewAvatarImage);
            }

        }

    }

    private void signUp(String username, String password) {
        newQBuser = new QBUser(username, password);



        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(hostingContext).build();
        waitingDialog.setMessage("Please wait ...");
        waitingDialog.setCanceledOnTouchOutside(false);
        waitingDialog.setCancelable(false);
        waitingDialog.show();


        QBUsers.signUp(newQBuser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Toast.makeText(hostingContext,"Signed up successfully",Toast.LENGTH_SHORT).show();
               waitingDialog.dismiss();
                //launch DialogsActivity
            }

            @Override
            public void onError(QBResponseException e) {
                Toast.makeText(hostingContext,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hostingContext = context;

    }
}



