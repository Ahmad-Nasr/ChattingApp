package com.nasr.ahmed.chattingapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nasr.ahmed.chattingapp.Activity.MessagesActivity;
import com.nasr.ahmed.chattingapp.Activity.UsersActivity;
import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = UserHolder.class.getSimpleName();

    private QBUser choosedQBUser;

    @BindView(R.id.img_user_image)
    ImageView mImageViewUserImage;
    @BindView(R.id.txt_user_name)
    TextView mTextViewUserName;


    public UserHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bindDialogItem(QBUser userItemData) {
        choosedQBUser = userItemData;

        //I'm dealing with user login as his name
        mTextViewUserName.setText(userItemData.getLogin());

        //setting colored TextDrawable to the ImageView
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int randomColor = colorGenerator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                .withBorder(3)
                .endConfig()
                .round();
        String text = mTextViewUserName.getText().toString().substring(0, 1).toUpperCase();
        TextDrawable textDrawable = builder.build(text, randomColor);
        mImageViewUserImage.setImageDrawable(textDrawable);

    }

    @Override
    public void onClick(final View view) {
        view.setEnabled(false);

        //launch messagesList Activity, passing the dialog/user information
        final Context context = view.getContext();


        final QBChatDialog dialog = DialogUtils.buildPrivateDialog(choosedQBUser.getId());

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {


                //launch messagesList Activity, passing the dialog information
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra(Common.EXTRA_CHOOSEN_CHAT_DIALOG, qbChatDialog);
                ((UsersActivity) context).startActivity(intent);

                view.setEnabled(true);
            }

            @Override
            public void onError(QBResponseException e) {
                view.setEnabled(true);
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.e(TAG,e.getMessage());

            }
        });

    }
}
