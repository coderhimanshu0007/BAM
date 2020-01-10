package com.teamcomputers.bam.Adapters.OrderProcessing;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.LinkedTreeMap;
import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class SPCSubmissionAdapter extends RecyclerView.Adapter<SPCSubmissionAdapter.ViewHolder> {
    private ArrayList<LinkedTreeMap> dataList;
    private int card_layout;
    Activity mActivity;

    public SPCSubmissionAdapter(DashboardActivity dashboardActivityContext, ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<LinkedTreeMap> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviCustomer, tviProjectNo, tviCount,   tviHrsDays, tviHeadName, tviReason;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviCustomer = (TextView) itemView.findViewById(R.id.tviCustomer);
            this.tviProjectNo = (TextView) itemView.findViewById(R.id.tviProjectNo);
            this.tviCount = (TextView) itemView.findViewById(R.id.tviCount);
            this.tviHrsDays = (TextView) itemView.findViewById(R.id.tviHrsDays);
            this.tviHeadName = (TextView) itemView.findViewById(R.id.tviHeadName);
            this.tviReason = (TextView) itemView.findViewById(R.id.tviReason);
        }
    }

    @Override
    public SPCSubmissionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spcsubmission_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SPCSubmissionAdapter.ViewHolder holder, final int position) {
        holder.tviCustomer.setText(String.valueOf(dataList.get(position).get("CustomerName")));
        holder.tviProjectNo.setText(String.valueOf(dataList.get(position).get("ProjectNo")));
        holder.tviCount.setText(String.valueOf(dataList.get(position).get("Count")));
        holder.tviHrsDays.setText(String.valueOf(dataList.get(position).get("HoursDays")));
        holder.tviHeadName.setText(String.valueOf(dataList.get(position).get("ProjectHeadName")));
        holder.tviReason.setText(String.valueOf(dataList.get(position).get("Reasons")));


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

