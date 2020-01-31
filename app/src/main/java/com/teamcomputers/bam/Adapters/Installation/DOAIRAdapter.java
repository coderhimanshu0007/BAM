package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.DOAIRModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class DOAIRAdapter extends RecyclerView.Adapter<DOAIRAdapter.ViewHolder> {
    private List<DOAIRModel.Table> dataList;
    Activity mActivity;

    public DOAIRAdapter(DashboardActivity dashboardActivityContext, List<DOAIRModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<DOAIRModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoice, tviCustomerName, tviPaymentStatus, tviCount, tviZoneName, tviReason, tviCity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviPaymentStatus = (TextView) itemView.findViewById(R.id.tviPaymentStatus);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviCity = (TextView) itemView.findViewById(R.id.tviCity);
        }
    }

    @Override
    public DOAIRAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.installation_doair_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DOAIRAdapter.ViewHolder holder, final int position) {
        holder.tviInvoice.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviPaymentStatus.setText(dataList.get(position).getPaymentStatus());
        holder.tviCount.setText(String.valueOf(dataList.get(position).getCount()));
        holder.tviZoneName.setText(dataList.get(position).getZoneName());
        holder.tviReason.setText(dataList.get(position).getReason());
        holder.tviCity.setText(dataList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

