package com.nasr.ahmed.chattingapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nasr.ahmed.chattingapp.Activity.DialogsActivity;
import com.nasr.ahmed.chattingapp.Activity.MessagesActivity;
import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.model.QBChatDialog;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    QBChatDialog choosedQBChatDialog;

    @BindView(R.id.img_dialog_image)
    ImageView mImageViewDialogImage;
    @BindView(R.id.txt_dialog_name)
    TextView mTextViewDialogName;
    @BindView(R.id.txt_last_msg)
    TextView mTextViewLastMsg;


    public DialogHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void bindDialogItem(QBChatDialog dialogItemData) {
        choosedQBChatDialog = dialogItemData;

        mTextViewDialogName.setText(dialogItemData.getName());
        mTextViewLastMsg.setText(dialogItemData.getLastMessage());

        //setting colored TextDrawable to the ImageView
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;
        int randomColor = colorGenerator.getRandomColor();

        TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                .withBorder(3)
                .endConfig()
                .round();
        String text = mTextViewDialogName.getText().toString().substring(0, 1).toUpperCase();
        TextDrawable textDrawable = builder.build(text, randomColor);
        mImageViewDialogImage.setImageDrawable(textDrawable);

    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        Context context = view.getContext();

        //launch messagesList Activity, passing the dialog information
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra(Common.EXTRA_CHOOSEN_CHAT_DIALOG, choosedQBChatDialog);
        ((DialogsActivity)context).startActivity(intent);
        view.setEnabled(true);

    }
}
