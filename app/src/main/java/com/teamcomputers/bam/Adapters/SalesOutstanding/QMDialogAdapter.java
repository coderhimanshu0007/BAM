package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.NewYTDQTDModel;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.YTDQTDModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class QMDialogAdapter extends RecyclerView.Adapter<QMDialogAdapter.ViewHolder> {
    //private List<SalesCustomerModel.StateCodeWise> dataList;
    List<NewYTDQTDModel.QTD> dataList;
    Activity mActivity;
    LayoutInflater inflter;

    public QMDialogAdapter(DashboardActivity dashboardActivityContext, List<NewYTDQTDModel.QTD> data) {
        this.dataList = data;
        //dataList = this.data.getStateCodeWise();
        this.mActivity = dashboardActivityContext;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<NewYTDQTDModel.QTD> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviType, tviTarget, tviActual, tviPercent;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviType = (TextView) itemView.findViewById(R.id.tviType);
            this.tviTarget = (TextView) itemView.findViewById(R.id.tviTarget);
            this.tviActual = (TextView) itemView.findViewById(R.id.tviActual);
            this.tviPercent = (TextView) itemView.findViewById(R.id.tviPercent);
        }
    }

    @Override
    public QMDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflter.inflate(R.layout.qmdialog_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QMDialogAdapter.ViewHolder holder, final int position) {
        holder.tviType.setText(position + 1 + ". " + dataList.get(position).getName());
        holder.tviTarget.setText(BAMUtil.getRoundOffValue(dataList.get(position).getTarget()));
        holder.tviActual.setText(BAMUtil.getRoundOffValue(dataList.get(position).getActual()));
        DecimalFormat df2 = new DecimalFormat("#.##");
        holder.tviPercent.setText(df2.format(dataList.get(position).getPercentage()) + "%");
        int bar = dataList.get(position).getPercentage().intValue();
        if (bar < 35) {
            holder.tviPercent.setTextColor(mActivity.getResources().getColor(R.color.color_progress_start));
        } else if (bar >= 35 && bar < 70) {
            holder.tviPercent.setTextColor(mActivity.getResources().getColor(R.color.color_progress_mid));
        } else if (bar >= 70) {
            holder.tviPercent.setTextColor(mActivity.getResources().getColor(R.color.color_progress_end));
        }
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesCustomerModel salesCustomerModel = new SalesCustomerModel();
                salesCustomerModel.setCustomerName(data.getCustomerName());
                salesCustomerModel.setYTD(data.getYTD());
                salesCustomerModel.setQTD(data.getQTD());
                salesCustomerModel.setMTD(data.getMTD());
                salesCustomerModel.setPosition(data.getPosition());
                List<SalesCustomerModel.StateCodeWise> selected = new ArrayList<>();
                selected.add(dataList.get(position));
                salesCustomerModel.setStateCodeWise(selected);
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, salesCustomerModel));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

