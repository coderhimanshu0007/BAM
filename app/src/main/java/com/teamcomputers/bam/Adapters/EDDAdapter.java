package com.teamcomputers.bam.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Models.EDDModel;
import com.teamcomputers.bam.R;

import java.util.ArrayList;

public class EDDAdapter extends RecyclerView.Adapter<EDDAdapter.ViewHolder> {
    private ArrayList<EDDModel> dataList;
    private int card_layout;
    Activity mActivity;

    public EDDAdapter(DashboardActivity dashboardActivityContext, ArrayList<EDDModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(ArrayList<EDDModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviPurchaserName, tviPoNo, tviCustomerName, tviVendorName, tviAmount, tviNOD, tviDR;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviPurchaserName = (TextView) itemView.findViewById(R.id.tviPurchaserName);
            this.tviPoNo = (TextView) itemView.findViewById(R.id.tviPoNo);
            this.tviCustomerName = (TextView) itemView.findViewById(R.id.tviCustomerName);
            this.tviVendorName = (TextView) itemView.findViewById(R.id.tviVendorName);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviNOD = (TextView) itemView.findViewById(R.id.tviNOD);
            this.tviDR = (TextView) itemView.findViewById(R.id.tviDR);
        }
    }

    @Override
    public EDDAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edd_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EDDAdapter.ViewHolder holder, final int position) {
        holder.tviPurchaserName.setText(dataList.get(position).getPurchaserName());
        holder.tviPoNo.setText(dataList.get(position).getPoNo());
        holder.tviCustomerName.setText(dataList.get(position).getCustomerName());
        holder.tviVendorName.setText(dataList.get(position).getVendorName());
        holder.tviAmount.setText(dataList.get(position).getAmount());
        holder.tviNOD.setText(dataList.get(position).getNod());
        holder.tviDR.setText(dataList.get(position).getDr());


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

