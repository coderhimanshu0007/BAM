package com.teamcomputers.bam.Adapters.OpenSalesOrder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOCustomerModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOInvoiceModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OSOProductAdapter extends RecyclerView.Adapter<OSOProductAdapter.ViewHolder> {
    private List<OSOInvoiceModel.Product> dataList;
    OSOInvoiceModel data;
    Activity mActivity;
    LayoutInflater inflter;

    public OSOProductAdapter(DashboardActivity dashboardActivityContext, OSOInvoiceModel data) {
        this.data = data;
        dataList = this.data.getProduct();
        this.mActivity = dashboardActivityContext;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<OSOInvoiceModel.Product> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviStateName, tviStateSOAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviStateName = (TextView) itemView.findViewById(R.id.tviStateName);
            this.tviStateSOAmount = (TextView) itemView.findViewById(R.id.tviStateSOAmount);
        }
    }

    @Override
    public OSOProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflter.inflate(R.layout.oso_state_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OSOProductAdapter.ViewHolder holder, final int position) {
        holder.tviStateName.setText(position + 1 + ". " + dataList.get(position).getProduct());
        holder.tviStateSOAmount.setText(BAMUtil.getRoundOffValue(dataList.get(position).getSOAmount()));
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OSOCustomerModel toCustomerModel = new OSOCustomerModel();
                toCustomerModel.setCustomerName(data.getCustomerName());
                toCustomerModel.setSOAmount(data.getSOAmount());
                toCustomerModel.setPosition(data.getPosition());
                List<OSOCustomerModel.StateCodeWise> selected = new ArrayList<>();
                selected.add(dataList.get(position));
                toCustomerModel.setStateCodeWise(selected);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, toCustomerModel));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

