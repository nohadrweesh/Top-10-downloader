package com.example.speed.top10dowmloader;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Speed on 24/08/2017.
 */

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final  LayoutInflater layoutInflater ;
    private List<FeedEntry> applications;

    public FeedAdapter(@NonNull Context context, @LayoutRes int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource=resource;
        this.layoutInflater=LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=layoutInflater.inflate(layoutResource,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            //Log.d(TAG, "getView: view position is :"+position+" name is :"+applications.get(position).getName());
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //View view=layoutInflater.inflate(layoutResource,parent,false);

//        TextView tvName=(TextView)view.findViewById(R.id.tvName);
//        TextView tvArtist=(TextView)view.findViewById(R.id.tvArtist);
//        TextView tvSummary=(TextView)view.findViewById(R.id.tvSummary);
        /*TextView tvName=(TextView)convertView.findViewById(tvName);
        TextView tvArtist=(TextView)convertView.findViewById(R.id.tvArtist);
        TextView tvSummary=(TextView)convertView.findViewById(R.id.tvSummary);*/


        FeedEntry currentApp=applications.get(position);

        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());
        viewHolder.tvSummary.setText(currentApp.getSummary());



        return convertView;
    }
    private class ViewHolder{
        private final TextView tvName;
        private final TextView tvArtist;
        private final TextView tvSummary;
                ViewHolder( View v){
                    this.tvName=(TextView)v.findViewById(R.id.tvName);
                    this.tvArtist=(TextView)v.findViewById(R.id.tvArtist);
                    this.tvSummary=(TextView)v.findViewById(R.id.tvSummary);
                }

    }
}
