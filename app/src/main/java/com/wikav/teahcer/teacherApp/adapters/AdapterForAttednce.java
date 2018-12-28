package com.wikav.teahcer.teacherApp.adapters;

/**
 * Created by wikav-pc on 7/4/2018.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.wikav.teahcer.teacherApp.R;
import com.wikav.teahcer.teacherApp.model.Attendence;


import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class AdapterForAttednce extends RecyclerView.Adapter<AdapterForAttednce.MyViewHolder> {

    private Context mContext ;
    private List<Attendence> myData ;
    RequestOptions option;

    public AdapterForAttednce(Context mContext, List<Attendence> mData) {
        this.mContext = mContext;
        this.myData = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.attedence_feed,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_name.setText(myData.get(position).getStundentName());
        holder.present.setChecked(myData.get(position).isPresent());
        holder.absent.setChecked(! myData.get(position).isPresent());

        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myData.get(position).setPresent(true);
                notifyDataSetChanged();
            }
        });

        holder.absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myData.get(position).setPresent(false);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        RadioButton present;
        RadioButton absent;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.stName);
            present = itemView.findViewById(R.id.present);
            absent  = itemView.findViewById(R.id.absent);
        }
    }


}

