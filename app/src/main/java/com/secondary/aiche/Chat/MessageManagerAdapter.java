package com.secondary.aiche.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.secondary.aiche.R;

import java.util.List;


public class MessageManagerAdapter extends ArrayAdapter<FriendlyMessage> {
    public MessageManagerAdapter(Context context, int resource, List<FriendlyMessage> objects) {
        super(context, resource, objects);
    }

    TextView authorTextView;
    MessageManager.thread newThead;
    LinearLayout _LinearLayout;
    String UID;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_manager_item, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);
        authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        _LinearLayout = (LinearLayout) convertView.findViewById(R.id.message_manager_background);
        newThead = new MessageManager.thread(authorTextView,_LinearLayout);
        newThead.start();

        FriendlyMessage message = getItem(position);


        messageTextView.setText(message.getText());
        authorTextView.setText(message.getName());

        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto) {
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getPhotoUrl())
                    .into(photoImageView);
        } else {
            photoImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}