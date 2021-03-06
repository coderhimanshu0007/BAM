package com.teamcomputers.bam.Adapters.TotalOutstanding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalOutstanding.TOCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class TOStateAdapter extends RecyclerView.Adapter<TOStateAdapter.ViewHolder> {
    private List<TOCustomerModel.StateCodeWise> dataList;
    TOCustomerModel data;
    Activity mActivity;
    boolean fromRSM, fromSP, fromProduct;
    String userId, level;
    LayoutInflater inflter;

    public TOStateAdapter(DashboardActivity dashboardActivityContext, String userId, String level, TOCustomerModel data, boolean fromRSM, boolean fromSP, boolean fromProduct) {
        this.data = data;
        dataList = this.data.getStateCodeWise();
        this.mActivity = dashboardActivityContext;
        this.userId = userId;
        this.level = level;
        this.fromRSM = fromRSM;
        this.fromSP = fromSP;
        this.fromProduct = fromProduct;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<TOCustomerModel.StateCodeWise> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviStateName, tviStateAmount;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviStateName = (TextView) itemView.findViewById(R.id.tviStateName);
            this.tviStateAmount = (TextView) itemView.findViewById(R.id.tviStateAmount);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
        }
    }

    @Override
    public TOStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflter.inflate(R.layout.to_state_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TOStateAdapter.ViewHolder holder, final int position) {
        holder.tviStateName.setText(position + 1 + ". " + dataList.get(position).getStateCode());
        holder.tviStateAmount.setText(BAMUtil.getRoundOffValue(dataList.get(position).getAmount()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TOCustomerModel toCustomerModel = new TOCustomerModel();
                toCustomerModel.setCustomerName(data.getCustomerName());
                toCustomerModel.setAmount(data.getAmount());
                toCustomerModel.setPosition(data.getPosition());
                List<TOCustomerModel.StateCodeWise> selected = new ArrayList<>();
                selected.add(dataList.get(position));
                toCustomerModel.setStateCodeWise(selected);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, toCustomerModel));
            }
        });
        if (level.equals("R1")) {
            if (fromRSM && fromSP && fromProduct) {
                holder.iviOption.setVisibility(View.GONE);
            }
        } else if (level.equals("R2") || level.equals("R3")) {
            if (fromSP && fromProduct) {
                holder.iviOption.setVisibility(View.GONE);
            }
        } else if (level.equals("R4")) {
            holder.iviOption.setVisibility(View.GONE);
        }

        holder.iviOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.iviOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                if (level.equals("R1")) {
                    popup.getMenu().getItem(2).setVisible(false);
                    if (fromSP && fromProduct) {
                        popup.getMenu().getItem(1).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromSP && fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromProduct && fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromSP) {
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromProduct) {
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                    }
                } else if (level.equals("R2") || level.equals("R3")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    if (fromSP) {
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromProduct) {
                        popup.getMenu().getItem(3).setVisible(false);
                    }
                } else if (level.equals("R4")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                //dataList.get(position).setUserId(userId);
                                TOCustomerModel rsmData = new TOCustomerModel();
                                rsmData.setCustomerName(data.getCustomerName());
                                rsmData.setAmount(data.getAmount());
                                rsmData.setPosition(data.getPosition());
                                List<TOCustomerModel.StateCodeWise> rsmSelected = new ArrayList<>();
                                rsmSelected.add(dataList.get(position));
                                rsmData.setStateCodeWise(rsmSelected);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmData));
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                //dataList.get(position).setUserId(userId);
                                TOCustomerModel spData = new TOCustomerModel();
                                spData.setCustomerName(data.getCustomerName());
                                spData.setAmount(data.getAmount());
                                spData.setPosition(data.getPosition());
                                List<TOCustomerModel.StateCodeWise> spSelected = new ArrayList<>();
                                spSelected.add(dataList.get(position));
                                spData.setStateCodeWise(spSelected);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, spData));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                            case R.id.menu4:
                                //handle menu3 click
                                //dataList.get(position).setUserId(userId);
                                TOCustomerModel pData = new TOCustomerModel();
                                pData.setCustomerName(data.getCustomerName());
                                pData.setAmount(data.getAmount());
                                pData.setPosition(data.getPosition());
                                List<TOCustomerModel.StateCodeWise> pSelected = new ArrayList<>();
                                pSelected.add(dataList.get(position));
                                pData.setStateCodeWise(pSelected);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, pData));
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

