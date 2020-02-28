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

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {
    private List<FullSalesModel> dataList;
    String type;
    Activity mActivity;

    public NewProductAdapter(DashboardActivity dashboardActivityContext, String type, List<FullSalesModel> data) {
        this.dataList = data;
        this.type = type;
        this.mActivity = dashboardActivityContext;
    }

    public void setItems(List<FullSalesModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout;
        TextView tviName, tviYTD, tviQTD, tviMTD;
        ProgressBar pBar;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviYTD = (TextView) itemView.findViewById(R.id.tviYTD);
            this.tviQTD = (TextView) itemView.findViewById(R.id.tviQTD);
            this.tviMTD = (TextView) itemView.findViewById(R.id.tviMTD);
            this.pBar = (ProgressBar) itemView.findViewById(R.id.pBar);
        }
    }

    @Override
    public NewProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_product_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewProductAdapter.ViewHolder holder, final int position) {
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
        if (!type.equals("0")) {
            String target = "", actual = "";
            int bar = 0;
            if (type.equals("YTD")) {
                target = BAMUtil.getRoundOffValue(dataList.get(position).getYTDTarget());
                actual = BAMUtil.getRoundOffValue(dataList.get(position).getYTD());
                //bar = (dataList.get(position).getYTDPercentage()).intValue();
            } else if (type.equals("QTD")) {
                target = BAMUtil.getRoundOffValue(dataList.get(position).getQTDTarget());
                actual = BAMUtil.getRoundOffValue(dataList.get(position).getQTD());
                //bar = (dataList.get(position).getQTDPercentage()).intValue();
            } else if (type.equals("MTD")) {
                target = BAMUtil.getRoundOffValue(dataList.get(position).getMTDTarget());
                actual = BAMUtil.getRoundOffValue(dataList.get(position).getMTD());
                //bar = (dataList.get(position).getMTDPercentage()).intValue();
            }

            //holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
            //holder.tviQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
            //holder.tviMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));
            holder.tviYTD.setText(target);
            holder.tviQTD.setText(actual);

            bar = (dataList.get(position).getYTDPercentage()).intValue();
            holder.tviMTD.setText(bar + "%");
            holder.pBar.setProgress(bar);

            if (bar < 35) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 35 && bar < 70) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 70) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            }
        } else {
            holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
            holder.tviQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
            holder.tviMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));
            holder.pBar.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.ACCOUNT_ITEM, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

