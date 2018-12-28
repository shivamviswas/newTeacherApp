package com.wikav.teahcer.teacherApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class NotificationMyAdaptor extends RecyclerView.Adapter <NotificationMyAdaptor.ProductViewHolder> {
    private Context mCtx;
   private List<com.wikav.teahcer.teacherApp.AdaptorForNotificationRecyclerView> adaptorForNotificationRecyclerView ;

    public NotificationMyAdaptor(Context mCtx, List <AdaptorForNotificationRecyclerView> adaptorForNotificationRecyclerView) {
        this.mCtx = mCtx;
        this.adaptorForNotificationRecyclerView = adaptorForNotificationRecyclerView;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view =inflater.inflate( R.layout.item_list_notification,null );
        return new ProductViewHolder( view );

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
    AdaptorForNotificationRecyclerView adaptorForNotificationRecycler = adaptorForNotificationRecyclerView.get( position );

        holder.textviewtitle.setText(adaptorForNotificationRecycler.getHead());
        holder.textViewDesc.setText(adaptorForNotificationRecycler.getDecp());




    }

    @Override
    public int getItemCount() {
        return adaptorForNotificationRecyclerView.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textviewtitle,textViewDesc,textViewRating,textViewPrice;


        public ProductViewHolder(View itemView) {
            super( itemView );

            textviewtitle = itemView.findViewById( R.id.userid );
            textViewDesc = itemView.findViewById( R.id.notification );



        }
    }
}

