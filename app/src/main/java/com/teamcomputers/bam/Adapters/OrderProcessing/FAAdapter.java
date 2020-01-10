package com.teamcomputers.bam.Adapters.OrderProcessing;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class FAAdapter extends RecyclerView.Adapter<FAAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public FAAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomerName, tviBu, tviCount, tviValue, tviProjectNo, tviHrsDays, tviName, tviFinanceDelay;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviBu = (TextView) itemView.findViewById(R.id.tviBu);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
            this.tviProjectNo = (TextView) itemView.findViewById(R.id.tviProjectNo);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviFinanceDelay = (TextView) itemView.findViewById(R.id.tviFinanceDelay);
        }
    }

    @Override
    public FAAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fa_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FAAdapter.ViewHolder holder, final int position) {
        holder.tviCustomerName.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviBu.setText(String.valueOf(dataList.get(position).get("BU")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviValue.setText(String.valueOf(dataList.get(position).get("Value")));
        holder.tviProjectNo.setText(String.valueOf(dataList.get(position).get("ProjectNo")));
        holder.tviHrsDays.setText(String.valueOf(dataList.get(position).get("HoursDays")));
        holder.tviName.setText(String.valueOf(dataList.get(position).get("Name")));
        holder.tviFinanceDelay.setText(String.valueOf(dataList.get(position).get("FinanceDelay")));



        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesController.getInstance(mActivity).setFrom("2");
                SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                EventBus.getDefault().post(new EventObject(ClickEvents.PENDING_TASK_CALLS, dataList.get(position)));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

