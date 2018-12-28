package com.wikav.teahcer.teacherApp.adapters;

/**
 * Created by wikav-pc on 7/4/2018.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wikav.teahcer.teacherApp.R;
import com.wikav.teahcer.teacherApp.model.CommentAnime;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aws on 11/03/2018.
 */

public class AdapterForComment extends RecyclerView.Adapter<AdapterForComment.MyViewHolder> {

    private Context mContext ;
    private List<CommentAnime> myData ;
    RequestOptions option;
    String ComId;


    public AdapterForComment(Context mContext, List<CommentAnime> mData) {
        this.mContext = mContext;
        this.myData = mData;


        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;


        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.comment_row,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;


        return viewHolder;
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tv_name.setText(myData.get(position).getNameCom());
        holder.tv_category.setText(myData.get(position).getComment());
        ComId=String.valueOf(myData.get(position).getIdCom());
        Glide.with(mContext).load(myData.get(position).getImage_url()).into(holder.tv_pro);


    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name ;
        TextView tv_category;
        CircleImageView tv_pro;






        public MyViewHolder(View itemView) {
            super(itemView);


            tv_name = itemView.findViewById(R.id.anime_name_on_comment);
            tv_category = itemView.findViewById(R.id.post_on_comment);
            tv_pro = itemView.findViewById(R.id.profile_pic_on_comment);


        }
    }


}

