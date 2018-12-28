package com.wikav.teahcer.teacherApp.adapters;


/**
 * Created by wikav-pc on 7/4/2018.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.wikav.teahcer.teacherApp.R;
import com.wikav.teahcer.teacherApp.model.Marks;

import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class AdapterForMarks extends RecyclerView.Adapter<AdapterForMarks.MyViewHolder> {

    private Context mContext ;
    private List<Marks> myData ;
    RequestOptions option;
    String studentId;


    public AdapterForMarks(Context mContext, List<Marks> mData) {
        this.mContext = mContext;
        this.myData = mData;

        // Request option for Glide
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;


        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.marksfeed,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.st_name.setText(myData.get(position).getStName());
        holder.totalMarks.setText(myData.get(position).getTotalMarks());
      //  holder.setMarks.setText(myData.get(position).getObtainMarrks());
        holder.setMarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    holder.setMarks.setText("");
                }
            }
        });

        holder.setMarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myData.get(position).setObtainMarrks(holder.setMarks.getText().toString());
//                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                myData.get(position).setObtainMarrks(holder.setMarks.getText().toString());
               // notifyDataSetChanged();
            }
        });

        studentId = String.valueOf(myData.get(position).getStId());
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView st_name,totalMarks ;
        EditText setMarks;


        public MyViewHolder(View itemView) {
            super(itemView);
            st_name = itemView.findViewById(R.id.stNameMark);
            totalMarks = itemView.findViewById(R.id.totalMarks);
            setMarks= itemView.findViewById(R.id.marksSet);
        }
    }


}

