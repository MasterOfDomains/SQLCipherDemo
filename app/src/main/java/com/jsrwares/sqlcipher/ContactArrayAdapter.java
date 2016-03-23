package com.jsrwares.sqlcipher;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {

    Context mContext;
    int mLayoutResourceId;
    Contact mData[] = null;

    public ContactArrayAdapter(Context mContext, int layoutResourceId, Contact[] data) {

        super(mContext, layoutResourceId, data);

        this.mLayoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of
         * inflating a new row layout.
         */

        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutResourceId, parent, false);
        }

        // object item based on the position
        Contact objectItem = mData[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView idTextView = (TextView) convertView.findViewById(R.id.idTextView);
        TextView firstNameTextView = (TextView) convertView.findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = (TextView) convertView.findViewById(R.id.lastNameTextView);
        TextView emailTextView = (TextView) convertView.findViewById(R.id.emailTextView);
        TextView phoneTextView = (TextView) convertView.findViewById(R.id.phoneTextView);

        idTextView.setText((objectItem.getId() != null) ? Integer.toString(objectItem.getId()) : "");
        firstNameTextView.setText(objectItem.getFirstName());
        lastNameTextView.setText(objectItem.getLastName());
        emailTextView.setText(objectItem.getEmail());
        phoneTextView.setText(objectItem.getPhone());

        return convertView;
    }
}