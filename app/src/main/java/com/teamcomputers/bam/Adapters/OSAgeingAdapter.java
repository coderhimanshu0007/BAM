package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.OSAgeingModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class OSAgeingAdapter extends RecyclerView.Adapter<OSAgeingAdapter.ViewHolder> {
    private ArrayList<OSAgeingModel> dataList;
    private int card_layout;
    Activity mActivity;

    public OSAgeingAdapter(DashboardActivity dashboardActivityContext, ArrayList<OSAgeingModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<OSAgeingModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviName, tviDUO, tviUpto30, tviUpto60, tviUpto90, tviUpto120, tviMoreThan120, tviTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviDUO = (TextView) itemView.findViewById(R.id.tviDUO);
            this.tviUpto30 = (TextView) itemView.findViewById(R.id.tviUpto30);
            this.tviUpto60 = (TextView) itemView.findViewById(R.id.tviUpto60);
            this.tviUpto90 = (TextView) itemView.findViewById(R.id.tviUpto90);
            this.tviUpto120 = (TextView) itemView.findViewById(R.id.tviUpto120);
            this.tviMoreThan120 = (TextView) itemView.findViewById(R.id.tviMoreThan120);
            this.tviTotal = (TextView) itemView.findViewById(R.id.tviTotal);
        }
    }

    @Override
    public OSAgeingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.os_ageing_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OSAgeingAdapter.ViewHolder holder, final int position) {
        holder.tviName.setText(dataList.get(position).getName());
        holder.tviDUO.setText(dataList.get(position).getDUO());
        holder.tviUpto30.setText(dataList.get(position).getUpto30());
        holder.tviUpto60.setText(dataList.get(position).getUpto60());
        holder.tviUpto90.setText(dataList.get(position).getUpto90());
        holder.tviUpto120.setText(dataList.get(position).getUpto120());
        holder.tviMoreThan120.setText(dataList.get(position).getMoreThan120());
        holder.tviTotal.setText(dataList.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

