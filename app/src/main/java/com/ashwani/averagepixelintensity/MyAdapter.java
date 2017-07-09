package com.ashwani.averagepixelintensity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ashwa on 7/9/2017.
 */

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    List<MyColor> map;
    Context mContext;

    public MyAdapter(Context context,List<MyColor> map){
        this.mContext = context;
        this.map =  map;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_color,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyColor color = map.get(position);
        holder.colorView.setBackgroundColor(Color.argb(255,color.getRED(),color.getGREEN(),color.getBLUE()));
        holder.tvPopulation.setText(""+color.getPopulation());
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View colorView;
        TextView tvPopulation;

        public ViewHolder(View item){
            super(item);

            colorView = item.findViewById(R.id.view);
            tvPopulation = (TextView)item.findViewById(R.id.population);

        }

    }

}
