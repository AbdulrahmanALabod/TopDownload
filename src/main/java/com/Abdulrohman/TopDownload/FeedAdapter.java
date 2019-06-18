package com.Abdulrohman.TopDownload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private  final int resource;
    private  final LayoutInflater layoutInflater;
    private  ArrayList<RssFeed>applicition;
    public FeedAdapter(Context context, int resource, ArrayList<RssFeed>appliciton) {
        super(context, resource);
        this.resource= resource;
        this.layoutInflater=LayoutInflater.from(context) ;
        this.applicition=appliciton;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            convertView = layoutInflater.inflate(this.resource, parent, false);
        }
       TextView txtName= convertView.findViewById(R.id.tvName);
        TextView txtArtist= convertView.findViewById(R.id.txtArtist);
        TextView txtSummary= convertView.findViewById(R.id.txtSummary);
        txtName.setText(this.applicition.get(position).getName());
        txtSummary.setText(this.applicition.get(position).getSummary());
        txtArtist.setText(this.applicition.get(position).getArtist());
        return convertView;
    }


    @Override
    public int getCount() {
        return  applicition.size();
    }
}
