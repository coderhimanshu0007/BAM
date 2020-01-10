package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.StockModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder> {
    private ArrayList<StockModel> dataList;
    private int card_layout;
    Activity mActivity;

    public StockAdapter(DashboardActivity dashboardActivityContext, ArrayList<StockModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<StockModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomerName, tviItemNo, tviDescription, tviPurchaserName, tviAmount, tviNOD;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviItemNo = (TextView) itemView.findViewById(R.id.tviItemNo);
            this.tviDescription = (TextView) itemView.findViewById(R.id.tviDescription);
            this.tviPurchaserName = (TextView) itemView.findViewById(R.id.tviPurchaserName);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
        }
    }

    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StockAdapter.ViewHolder holder, final int position) {
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviItemNo.setText(dataList.get(position).getItemNo());
        holder.tviDescription.setText(dataList.get(position).getDescription());
        holder.tviPurchaserName.setText(dataList.get(position).getPurchaserName());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviNOD.setText(dataList.get(position).getNod());



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

