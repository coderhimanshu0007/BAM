package com.teamcomputers.bam.Adapters.Logistics;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        LinearLayout llValue;
        TextView tviCustomer, tviFromCity, tviToCity, tviDaysCountHeading, tviDaysCount, tviCourier,
                tviReasonHeading, tviCourierHeading, tviReason, tviInvoice;

        public ViewHolder(View itemView) {
            super(itemView);
            this.llValue = (LinearLayout) itemView.findViewById(R.id.llValue);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviFromCity = (TextView) itemView.findViewById(R.id.tviFromCity);
            this.tviToCity = (TextView) itemView.findViewById(R.id.tviToCity);
            this.tviReasonHeading = (TextView) itemView.findViewById(R.id.tviReasonHeading);
            this.tviCourierHeading = (TextView) itemView.findViewById(R.id.tviCourierHeading);
            this.tviDaysCountHeading = (TextView)itemView.findViewById(R.id.tviDaysCountHeading);
            this.tviDaysCount = (TextView) itemView.findViewById(R.id.tviDaysCount);
            this.tviCourier = (TextView) itemView.findViewById(R.id.tviCourier);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
        }
    }

    @Override
    public DispatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logistics_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DispatchAdapter.ViewHolder holder, final int position) {
        holder.llValue.setVisibility(View.GONE);
        holder.tviDaysCountHeading.setText("Hrs/Days");
        holder.tviReasonHeading.setText("Dispatch Pending Value");
        holder.tviCourierHeading.setText("Dispatch Pending");
        holder.tviCustomer.setText(dataList.get(position).getCustomerName());
        holder.tviFromCity.setText(dataList.get(position).getFromCity());
        holder.tviDaysCount.setText(dataList.get(position).getHoursDays());
        holder.tviCourier.setText(String.valueOf(dataList.get(position).getDispatchPending()));
        holder.tviReason.setText(String.valueOf(dataList.get(position).getDispatchPendingValue()));
        holder.tviInvoice.setText(dataList.get(position).getInvoiceNo());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

