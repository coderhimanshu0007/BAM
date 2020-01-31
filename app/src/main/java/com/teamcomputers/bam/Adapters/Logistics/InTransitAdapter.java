package com.teamcomputers.bam.Adapters.Logistics;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.InTransitModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class InTransitAdapter extends RecyclerView.Adapter<InTransitAdapter.ViewHolder> {
    private List<InTransitModel.Table> dataList;
    Activity mActivity;

    public InTransitAdapter(DashboardActivity dashboardActivityContext, List<InTransitModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<InTransitModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviCustomerName, tviFromCity, tviToCity, tviCourier, tviDays, tviReason, tviCount, tviValue;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviCourier = (TextView) itemView.findViewById(R.id.tviCourier);
            this.tviDays = (TextView) itemView.findViewById(R.id.tviDays);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
        }
    }

    @Override
    public InTransitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intransit_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InTransitAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviFromCity.setText(dataList.get(position).getFromCity());
        holder.tviToCity.setText(dataList.get(position).getToCity());
        holder.tviCourier.setText(dataList.get(position).getCourier());
        holder.tviDays.setText(String.valueOf(dataList.get(position).getDays()));
        holder.tviReason.setText(dataList.get(position).getReason());
        holder.tviCount.setText(String.valueOf(dataList.get(position).getCount()));
        holder.tviValue.setText(String.valueOf(dataList.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

