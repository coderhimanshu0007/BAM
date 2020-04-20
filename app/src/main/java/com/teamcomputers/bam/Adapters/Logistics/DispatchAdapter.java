package com.teamcomputers.bam.Adapters.Logistics;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.DispatchModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.ViewHolder> {
    private List<DispatchModel.Table> dataList;
    Activity mActivity;

    public DispatchAdapter(DashboardActivity dashboardActivityContext, List<DispatchModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<DispatchModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomer, tviFromCity, tviToCity, tviHrsDays, tviDispatchPending, tviDispatchPendingValue, tviInvoice;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviDispatchPending = (TextView) itemView.findViewById(R.id.tviDispatchPending);
            this.tviDispatchPendingValue = (TextView) itemView.findViewById(R.id.tviDispatchPendingValue);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
        }
    }

    @Override
    public DispatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logistics_dispatch_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DispatchAdapter.ViewHolder holder, final int position) {
        holder.tviCustomer.setText(dataList.get(position).getCustomerName());
        holder.tviFromCity.setText(dataList.get(position).getFromCity());
        holder.tviHrsDays.setText(dataList.get(position).getHoursDays());
        holder.tviDispatchPending.setText(String.valueOf(dataList.get(position).getDispatchPending()));
        holder.tviDispatchPendingValue.setText(String.valueOf(dataList.get(position).getDispatchPendingValue()));
        holder.tviInvoice.setText(dataList.get(position).getInvoiceNo());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

