package com.nasr.ahmed.chattingapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


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
    public void onClick(View view) {
        //launch messagesList Activity, passing the dialog/user information
    }
}
