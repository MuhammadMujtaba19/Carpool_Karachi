package com.example.carpoolkarachi;


/**
 * Created by users12 on 10/12/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.viewholder> {
    Context context;

    public homeAdapter( Context context) {
        this.context = context;
    }

    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(final viewholder holder, int position) {
        if(position==1){
            holder.driverImage.setImageResource(R.drawable.ic_boy);
        }
        if(position==3){
            holder.driverImage.setImageResource(R.drawable.ic_woman);
        }

        if(position==2){
            holder.driverImage.setImageResource(R.drawable.ic_girl);
        }
        holder.btn.setOnClickListener(v->{
                Intent i = new Intent(context,RideDetails.class);

                //Testing purpose k liye hardcoded Lat Lng Tariq road se North namzimabad xD
                LatLng sourceLatLng = new LatLng(24.87186,67.059895);
                LatLng destLatLng = new LatLng(24.9372146,67.042281);


                i.putExtra("sourceLatLng",sourceLatLng);
                i.putExtra("destLatLng",destLatLng);

                context.startActivity(i);

            }
        );
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView driverImage;
        TextView name, route;
        Button btn;
        public viewholder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.driverName);
            route=(TextView)itemView.findViewById(R.id.driverRoute);
            driverImage = (ImageView)itemView.findViewById(R.id.driverImage);
            btn = (Button)itemView.findViewById(R.id.viewDetails);
        }
    }
}

