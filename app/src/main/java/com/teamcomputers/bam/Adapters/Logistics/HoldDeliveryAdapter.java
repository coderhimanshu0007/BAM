package com.teamcomputers.bam.Adapters.Logistics;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.HoldDeliveryModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class HoldDeliveryAdapter extends RecyclerView.Adapter<HoldDeliveryAdapter.ViewHolder> {
    private List<HoldDeliveryModel.Table> dataList;
    Activity mActivity;

    public HoldDeliveryAdapter(DashboardActivity dashboardActivityContext, List<HoldDeliveryModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<HoldDeliveryModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoice, tviCustomer, tviFromCity, tviToCity, tviCourier, tviDaysCountHeading, tviDaysCount,
                tviReasonHeading, tviCourierHeading, tviReason, tviValue;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviReasonHeading = (TextView) itemView.findViewById(R.id.tviReasonHeading);
            this.tviCourierHeading = (TextView) itemView.findViewById(R.id.tviCourierHeading);
            this.tviCourier = (TextView) itemView.findViewById(R.id.tviCourier);
            this.tviDaysCountHeading = (TextView) itemView.findViewById(R.id.tviDaysCountHeading);
            this.tviDaysCount = (TextView) itemView.findViewById(R.id.tviDaysCount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            //this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviValue = (TextView) itemView.findViewById(R.id.tviValue);
        }
    }

    @Override
    public HoldDeliveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logistics_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HoldDeliveryAdapter.ViewHolder holder, final int position) {
        holder.tviInvoice.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomer.setText(dataList.get(position).getCustomerName());
        holder.tviFromCity.setText(dataList.get(position).getFromCity());
        holder.tviToCity.setText(dataList.get(position).getToCity());
        holder.tviCourierHeading.setText("Account Manager");
        holder.tviCourier.setText(dataList.get(position).getAccountManager());
        holder.tviDaysCountHeading.setText("Days");
        holder.tviDaysCount.setText(String.valueOf(dataList.get(position).getDays()));
        holder.tviReason.setText(dataList.get(position).getReason());
        //holder.tviCount.setVisibility(View.GONE);
        holder.tviValue.setText(String.valueOf(dataList.get(position).getValue()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

