package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.FullSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewRSMAdapter extends RecyclerView.Adapter<NewRSMAdapter.ViewHolder> {
    private List<FullSalesModel> dataList;
    Activity mActivity;
    String type;

    public NewRSMAdapter(DashboardActivity dashboardActivityContext, String type, List<FullSalesModel> data) {
        this.dataList = data;
        this.type = type;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<FullSalesModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout;
        TextView tviName, tviTarget, tviActual, tviACH;
        ProgressBar pBar;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviTarget = (TextView) itemView.findViewById(R.id.tviTarget);
            this.tviActual = (TextView) itemView.findViewById(R.id.tviActual);
            this.tviACH = (TextView) itemView.findViewById(R.id.tviACH);
            this.pBar = (ProgressBar) itemView.findViewById(R.id.pBar);
        }
    }

    @Override
    public NewRSMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_person_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewRSMAdapter.ViewHolder holder, final int position) {
        if (position == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_first_item_value));
        } else if (position == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_second_item_value));
        } else if (position == 2) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_third_item_value));
        } else if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_white));
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.login_bg));
        }
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getName());
        String target = "", actual = "";
        int bar = 0;
        if (type.equals("YTD")) {
            target = BAMUtil.getRoundOffValue(dataList.get(position).getYTDTarget());
            actual = BAMUtil.getRoundOffValue(dataList.get(position).getYTD());
            bar = (dataList.get(position).getYTDPercentage()).intValue();
        } else if (type.equals("QTD")) {
            target = BAMUtil.getRoundOffValue(dataList.get(position).getQTDTarget());
            actual = BAMUtil.getRoundOffValue(dataList.get(position).getQTD());
            bar = (dataList.get(position).getQTDPercentage()).intValue();
        } else if (type.equals("MTD")) {
            target = BAMUtil.getRoundOffValue(dataList.get(position).getMTDTarget());
            actual = BAMUtil.getRoundOffValue(dataList.get(position).getMTD());
            bar = (dataList.get(position).getMTDPercentage()).intValue();
        }
        holder.tviTarget.setText(target);
        holder.tviActual.setText(actual);


        holder.tviACH.setText(bar + "%");
        holder.pBar.setProgress(bar);

        if (bar < 35) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 35 && bar < 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, position));
                dataList.get(position).setPosition(position);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, dataList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

