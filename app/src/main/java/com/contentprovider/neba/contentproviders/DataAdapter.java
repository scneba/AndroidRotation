package com.contentprovider.neba.contentproviders;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.contentprovider.neba.contentproviders.models.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neba on 01-Jul-17.
 */


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder>{

    private ArrayList<Data> listdata;
    private Activity activity;
    public DataAdapter(ArrayList<Data> data, Activity activity){
        this.listdata=data;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cardview,parent,false);

        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.e("list size",""+listdata.size());
        Data thisdata = listdata.get(position);
        holder.tvcity.setText(thisdata.getCity());
        holder.tvname.setText(thisdata.getName());
        holder.tvhouse.setText(thisdata.getHouse()==null?"coll":thisdata.getHouse());
        holder.tvyear.setText(thisdata.getYears());

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    /**view halper class for offer and swap*/
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvname,tvcity,tvhouse,tvyear;

        public MyViewHolder(View view) {
            super(view);
            tvname = (TextView) view.findViewById(R.id.tvname);
            tvhouse = (TextView) view.findViewById(R.id.tvhouse);
            tvcity = (TextView) view.findViewById(R.id.tvcity);
            tvyear = (TextView) view.findViewById(R.id.tvyear);;
        }
    }
}
