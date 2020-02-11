package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.WIPModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class WIPAdapter extends RecyclerView.Adapter<WIPAdapter.ViewHolder> {
    private List<WIPModel.Table> dataList;
    Activity mActivity;

    public WIPAdapter(DashboardActivity dashboardActivityContext, List<WIPModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<WIPModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviCustomerName, tviPaymentStage, tviCount, tviAmount, tviNOD, tviRemainingAmount, tviReason, tviZoneName, tviCity;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviPaymentStage = (TextView) itemView.findViewById(R.id.tviPaymentStage);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviRemainingAmount = (TextView) itemView.findViewById(R.id.tviRemainingAmount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviCity = (TextView) itemView.findViewById(R.id.tviCity);
        }
    }

    @Override
    public WIPAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wip_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WIPAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviPaymentStage.setText(dataList.get(position).getPaymentStatus());
        holder.tviCount.setText(String.valueOf(dataList.get(position).getCount()));
        holder.tviAmount.setText(String.valueOf(dataList.get(position).getAmount()));
        holder.tviNOD.setText(String.valueOf(dataList.get(position).getNOD()));
        holder.tviRemainingAmount.setText(String.valueOf(dataList.get(position).getRemainingAmount()));
        holder.tviReason.setText(dataList.get(position).getReason());
        holder.tviZoneName.setText(dataList.get(position).getZoneName());
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

