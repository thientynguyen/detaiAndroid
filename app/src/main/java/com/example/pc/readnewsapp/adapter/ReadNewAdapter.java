package com.example.pc.readnewsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pc.readnewsapp.R;
import com.example.pc.readnewsapp.model.ReadNew;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 3/24/2018.
 */

public class ReadNewAdapter extends ArrayAdapter<ReadNew>{

    private Context context;
    private int resource;
    private List<ReadNew> arrContact;

    public ReadNewAdapter(Context context, int resource, ArrayList<ReadNew> arrContact) {
        super(context, resource, arrContact);
        this.context = context;
        this.resource = resource;
        this.arrContact = arrContact;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_display, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle= (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.tvpubDate = (TextView) convertView.findViewById(R.id.tvpubDate);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReadNew readNew = arrContact.get(position);
        Picasso.with(context).load(readNew.getPicture()).into(viewHolder.imageView);

        viewHolder.tvTitle.setText(readNew.getTitle());
        viewHolder.tvpubDate.setText(readNew.getPubDate());

        return convertView;
    }

    public class ViewHolder {
        private TextView tvTitle;
        private ImageView imageView;
        private TextView tvpubDate;

    }
}
