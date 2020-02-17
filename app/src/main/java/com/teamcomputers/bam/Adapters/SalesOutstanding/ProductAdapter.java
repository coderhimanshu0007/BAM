package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.RSMDataModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<FullSalesModel> dataList;
    Activity mActivity;

    public ProductAdapter(DashboardActivity dashboardActivityContext, List<FullSalesModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<FullSalesModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout;
        TextView tviName, tviYTD, tviQTD, tviMTD;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviYTD = (TextView) itemView.findViewById(R.id.tviYTD);
            this.tviQTD = (TextView) itemView.findViewById(R.id.tviQTD);
            this.tviMTD = (TextView) itemView.findViewById(R.id.tviMTD);
        }
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rsm_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_white));
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.login_bg));
        }
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getName());
        holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
        holder.tviQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
        holder.tviMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_ITEM, dataList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

