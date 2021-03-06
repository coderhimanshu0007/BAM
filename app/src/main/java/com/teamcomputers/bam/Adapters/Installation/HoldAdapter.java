package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.HoldModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class HoldAdapter extends RecyclerView.Adapter<HoldAdapter.ViewHolder> {
    private List<HoldModel.Table> dataList;
    Activity mActivity;

    public HoldAdapter(DashboardActivity dashboardActivityContext, List<HoldModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<HoldModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviCustomerName, tviOnHoldAmount, tviZoneName, tviNOD, tviPaymentStatus, tviRemainingAmount, tviReason, tviCity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviOnHoldAmount = (TextView) itemView.findViewById(R.id.tviOnHoldAmount);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviPaymentStatus = (TextView) itemView.findViewById(R.id.tviPaymentStatus);
            this.tviRemainingAmount = (TextView) itemView.findViewById(R.id.tviRemainingAmount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviCity = (TextView) itemView.findViewById(R.id.tviCity);
        }
    }

    @Override
    public HoldAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onhold_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HoldAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviOnHoldAmount.setText(String.valueOf(dataList.get(position).getOnHoldAmount()));
        holder.tviZoneName.setText(dataList.get(position).getZoneName());
        holder.tviNOD.setText(String.valueOf(dataList.get(position).getNOD()));
        holder.tviPaymentStatus.setText(dataList.get(position).getPaymentStatus());
        holder.tviRemainingAmount.setText(String.valueOf(dataList.get(position).getRemainingAmount()));
        holder.tviReason.setText(dataList.get(position).getReason());
        holder.tviCity.setText(dataList.get(position).getCity());

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

