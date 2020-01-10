package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.OutstandingModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class OutstandingAdapter extends RecyclerView.Adapter<OutstandingAdapter.ViewHolder> {
    private ArrayList<OutstandingModel> dataList;
    private int card_layout;
    Activity mActivity;

    public OutstandingAdapter(DashboardActivity dashboardActivityContext, ArrayList<OutstandingModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<OutstandingModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo, tviName, tviAmount, tviFinancePerson, tviNOD, tviDueDate, tviExpectedDate, tviPaymentStage, tviRemarks;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviFinancePerson = (TextView) itemView.findViewById(R.id.tviFinancePerson);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviDueDate = (TextView) itemView.findViewById(R.id.tviDueDate);
            this.tviExpectedDate = (TextView) itemView.findViewById(R.id.tviExpectedDate);
            this.tviPaymentStage = (TextView) itemView.findViewById(R.id.tviPaymentStage);
            this.tviRemarks = (TextView) itemView.findViewById(R.id.tviRemarks);
        }
    }

    @Override
    public OutstandingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OutstandingAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(dataList.get(position).getInvoiceNo());
        holder.tviName.setText(dataList.get(position).getName());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviFinancePerson.setText(dataList.get(position).getFinancePerson());
        holder.tviNOD.setText(dataList.get(position).getNod());
        holder.tviDueDate.setText(dataList.get(position).getDueDate());
        holder.tviExpectedDate.setText(dataList.get(position).getExpectedDate());
        holder.tviPaymentStage.setText(dataList.get(position).getPaymetStage());
        holder.tviRemarks.setText(dataList.get(position).getRemarks());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

