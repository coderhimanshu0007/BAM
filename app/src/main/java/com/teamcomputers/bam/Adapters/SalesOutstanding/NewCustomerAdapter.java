package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class NewCustomerAdapter extends RecyclerView.Adapter<NewCustomerAdapter.ViewHolder> {
    private List<SalesCustomerModel> dataList;
    Activity mActivity;
    DashboardActivity dashboardActivity;
    private LinearLayoutManager layoutManager;

    String userId;

    int progress = 20;

    public NewCustomerAdapter(DashboardActivity dashboardActivityContext, String userId, List<SalesCustomerModel> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
        this.userId = userId;
        this.dashboardActivity = dashboardActivityContext;
    }

    public void setItems(List<SalesCustomerModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout, llExpand;
        RelativeLayout rlStateWise;
        TextView tviName, tviYTD, tviQTD, tviMTD;
        RecyclerView rviStateCode;
        ProgressBar pBar;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            rlStateWise = (RelativeLayout) itemView.findViewById(R.id.rlStateWise);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.tviYTD = (TextView) itemView.findViewById(R.id.tviYTD);
            this.tviQTD = (TextView) itemView.findViewById(R.id.tviQTD);
            this.tviMTD = (TextView) itemView.findViewById(R.id.tviMTD);
            this.llExpand = (LinearLayout) itemView.findViewById(R.id.llExpand);
            this.rviStateCode = (RecyclerView) itemView.findViewById(R.id.rviStateCode);
            this.pBar = (ProgressBar) itemView.findViewById(R.id.pBar);
        }
    }

    @Override
    public NewCustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_customer_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewCustomerAdapter.ViewHolder holder, final int position) {
        dataList.get(position).setOpen(0);
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
        int bar = 0;
        bar = progress * (position + 1);
        holder.pBar.setProgress(bar);

        if (bar < 35) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 35 && bar < 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }

        /*Handler hdlr = new Handler();
        i = holder.pBar.getProgress();
        new Thread(new Runnable() {
            public void run() {
                while (i < 100) {
                    i += 1;
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            holder.pBar.setProgress(i);
                            //txtView.setText(i+"/"+pgsBar.getMax());
                            if(i>50){
                                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_first_item_value), PorterDuff.Mode.SRC_IN);
                            }
                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getCustomerName());
        holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
        holder.tviQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
        holder.tviMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                dataList.get(position).setUserId(userId);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, dataList.get(position)));
            }
        });
        List<SalesCustomerModel.StateCodeWise> stateCodeWise = dataList.get(position).getStateCodeWise();
        dataList.get(position).setPosition(position);
        StateAdapter aa = new StateAdapter(dashboardActivity, dataList.get(position));
        layoutManager = new LinearLayoutManager(dashboardActivity);
        holder.rviStateCode.setLayoutManager(layoutManager);
        holder.rviStateCode.setAdapter(aa);
        holder.rlStateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataList.get(position).getOpen() == 0) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand));
                    } else {
                        holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand));
                    }
                    dataList.get(position).setOpen(1);
                    holder.rviStateCode.setVisibility(View.VISIBLE);
                } else if (dataList.get(position).getOpen() == 1) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse));
                    } else {
                        holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse));
                    }
                    dataList.get(position).setOpen(0);
                    holder.rviStateCode.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

