package com.teamcomputers.bam.Adapters.Installation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.OpenCallsModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class OpenCallsAdapter extends RecyclerView.Adapter<OpenCallsAdapter.ViewHolder> {
    private List<OpenCallsModel.Table> dataList;
    private int card_layout;
    Activity mActivity;

    public OpenCallsAdapter(DashboardActivity dashboardActivityContext, List<OpenCallsModel.Table> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<OpenCallsModel.Table> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoice, tviCustomerName, tviAmount, tviZoneName, tviNOD, tviRemainingAmount, tviReason;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoice = (TextView) itemView.findViewById(R.id.tviInvoice);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviZoneName = (TextView) itemView.findViewById(R.id.tviZoneName);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviRemainingAmount = (TextView) itemView.findViewById(R.id.tviRemainingAmount);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
        }
    }

    @Override
    public OpenCallsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.installation_open_calls_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OpenCallsAdapter.ViewHolder holder, final int position) {
        holder.tviInvoice.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviAmount.setText(String.valueOf(dataList.get(position).getAmount()));
        holder.tviZoneName.setText(dataList.get(position).getZoneName());
        holder.tviNOD.setText(String.valueOf(dataList.get(position).getNOD()));
        holder.tviRemainingAmount.setText(String.valueOf(dataList.get(position).getRemainingAmount()));
        holder.tviReason.setText(dataList.get(position).getReason());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

