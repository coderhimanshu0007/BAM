package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
    private List<SalesCustomerModel.StateCodeWise> dataList;
    Activity mActivity;
    LayoutInflater inflter;

    public StateAdapter(DashboardActivity dashboardActivityContext, List<SalesCustomerModel.StateCodeWise> data) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<SalesCustomerModel.StateCodeWise> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviStateName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviStateName = (TextView) itemView.findViewById(R.id.tviStateName);
        }
    }

    @Override
    public StateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflter.inflate(R.layout.state_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StateAdapter.ViewHolder holder, final int position) {
        holder.tviStateName.setText(dataList.get(position).getStateCode());
        //holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

