package com.teamcomputers.bam.Adapters.TotalOutstanding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalOutstanding.TOCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TOStateAdapter extends RecyclerView.Adapter<TOStateAdapter.ViewHolder> {
    private List<TOCustomerModel.StateCodeWise> dataList;
    TOCustomerModel data;
    Activity mActivity;
    LayoutInflater inflter;

    public TOStateAdapter(DashboardActivity dashboardActivityContext, TOCustomerModel data) {
        this.data = data;
        dataList = this.data.getStateCodeWise();
        this.mActivity = dashboardActivityContext;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<TOCustomerModel.StateCodeWise> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviStateName, tviStateYTD, tviStateQTD, tviStateMTD;
        ;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviStateName = (TextView) itemView.findViewById(R.id.tviStateName);
            this.tviStateYTD = (TextView) itemView.findViewById(R.id.tviStateYTD);
            this.tviStateQTD = (TextView) itemView.findViewById(R.id.tviStateQTD);
            this.tviStateMTD = (TextView) itemView.findViewById(R.id.tviStateMTD);
        }
    }

    @Override
    public TOStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflter.inflate(R.layout.state_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TOStateAdapter.ViewHolder holder, final int position) {
        holder.tviStateName.setText(position + 1 + ". " + dataList.get(position).getStateCode());
        //holder.tviStateYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
        //holder.tviStateQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
        //holder.tviStateMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOCustomerModel toCustomerModel = new TOCustomerModel();
                toCustomerModel.setCustomerName(data.getCustomerName());
                //salesCustomerModel.setYTD(data.getYTD());
                //salesCustomerModel.setQTD(data.getQTD());
                //salesCustomerModel.setMTD(data.getMTD());
                toCustomerModel.setPosition(data.getPosition());
                List<TOCustomerModel.StateCodeWise> selected = new ArrayList<>();
                selected.add(dataList.get(position));
                toCustomerModel.setStateCodeWise(selected);
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_SELECT, salesCustomerModel));
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, toCustomerModel));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

