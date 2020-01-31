package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.ExpectedCollectionModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class ExpectedCollectionAdapter extends RecyclerView.Adapter<ExpectedCollectionAdapter.ViewHolder> {
    private ArrayList<ExpectedCollectionModel> dataList;
    private int card_layout;
    Activity mActivity;

    public ExpectedCollectionAdapter(DashboardActivity dashboardActivityContext, ArrayList<ExpectedCollectionModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<ExpectedCollectionModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviInvoiceNo,tviCustomer,  tviAmount, tviExpectedDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviInvoiceNo = (TextView) itemView.findViewById(R.id.tviInvoiceNo);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviExpectedDate = (TextView) itemView.findViewById(R.id.tviExpectedDate);
        }
    }

    @Override
    public ExpectedCollectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expected_collecion_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ExpectedCollectionAdapter.ViewHolder holder, final int position) {
        holder.tviInvoiceNo.setText(dataList.get(position).getInvoiceNo());
        holder.tviCustomer.setText(dataList.get(position).getCustomer());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviExpectedDate.setText(dataList.get(position).getExpectedDate());


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

