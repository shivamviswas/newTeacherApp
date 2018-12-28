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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.wikav.teahcer.teacherApp.R;
import com.wikav.teahcer.teacherApp.model.Syllabus;


import java.util.List;

/**
 * Created by Aws on 11/03/2018.
 */

public class AdapterForSyllabus extends RecyclerView.Adapter<AdapterForSyllabus.MyViewHolder> {

    private Context mContext ;
    private List<Syllabus> myData ;
    RequestOptions option;
    String studentId;


    public AdapterForSyllabus(Context mContext, List<Syllabus> mData) {
        this.mContext = mContext;
        this.myData = mData;

        // Request option for Glide
        //option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view ;


        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.syllabusfeed,parent,false) ;
        final MyViewHolder viewHolder = new MyViewHolder(view) ;
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.subject.setText(myData.get(position).getSubject());
        holder.totalsyllabus.setText(myData.get(position).getTotalSyllabus());
      //  holder.setMarks.setText(myData.get(position).getObtainMarrks());
       holder.setSyllbus.setText( myData.get(position).getSyllabusComplete());
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i= Integer.parseInt(myData.get(position).getSyllabusComplete());
                i++;
                holder.setSyllbus.setText(String.valueOf(i));
                myData.get(position).setSyllabusComplete(holder.setSyllbus.getText().toString());
            }
        });

        String val = myData.get(position).getSyllabusComplete();
        if (val.equals("0"))
        {
            holder.mines.setEnabled(false);
        }

            holder.mines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(myData.get(position).getSyllabusComplete());
                    i--;
                    holder.setSyllbus.setText(String.valueOf(i));
                    myData.get(position).setSyllabusComplete(holder.setSyllbus.getText().toString());
                }
            });


        holder.setSyllbus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setSyllbus.setCursorVisible(true);
            }
        });

        holder.setSyllbus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                myData.get(position).setSyllabusComplete(holder.setSyllbus.getText().toString());
                String val = myData.get(position).getSyllabusComplete();
                if (!val.equals("0"))
                {
                    holder.mines.setEnabled(true);
                }
                else {
                    holder.mines.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                myData.get(position).setSyllabusComplete(holder.setSyllbus.getText().toString());
               // notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subject,totalsyllabus ;
        EditText setSyllbus;
        Button plus,mines;


        public MyViewHolder(View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.subjectSyl);
            totalsyllabus=itemView.findViewById(R.id.totalsyll);
            setSyllbus=itemView.findViewById(R.id.complteSyl);
            plus=itemView.findViewById(R.id.plus);
            mines=itemView.findViewById(R.id.mines);


        }
    }


}

