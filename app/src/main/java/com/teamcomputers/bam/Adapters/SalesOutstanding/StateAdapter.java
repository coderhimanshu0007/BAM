package com.teamcomputers.bam.Adapters.SalesOutstanding;

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
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.ViewHolder> {
    private List<SalesCustomerModel.StateCodeWise> dataList;
    SalesCustomerModel data;
    Activity mActivity;
    boolean fromRSM, fromSP, fromProduct;
    String userId, level;
    LayoutInflater inflter;

    public StateAdapter(DashboardActivity dashboardActivityContext, String userId, String level,SalesCustomerModel data, boolean fromRSM, boolean fromSP, boolean fromProduct) {
        this.data = data;
        dataList = this.data.getStateCodeWise();
        this.userId = userId;
        this.level = level;
        this.mActivity = dashboardActivityContext;
        this.fromRSM = fromRSM;
        this.fromSP = fromSP;
        this.fromProduct = fromProduct;
        inflter = (LayoutInflater.from(dashboardActivityContext));
    }

    public void setItems(List<SalesCustomerModel.StateCodeWise> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tviStateName, tviStateYTD, tviStateQTD, tviStateMTD;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tviStateName = (TextView) itemView.findViewById(R.id.tviStateName);
            this.tviStateYTD = (TextView) itemView.findViewById(R.id.tviStateYTD);
            this.tviStateQTD = (TextView) itemView.findViewById(R.id.tviStateQTD);
            this.tviStateMTD = (TextView) itemView.findViewById(R.id.tviStateMTD);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
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
        holder.tviStateName.setText(position + 1 + ". " + dataList.get(position).getStateCode());
        holder.tviStateYTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getYTD()));
        holder.tviStateQTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getQTD()));
        holder.tviStateMTD.setText(BAMUtil.getRoundOffValue(dataList.get(position).getMTD()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_SELECT, salesCustomerModel));
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_ITEM, salesCustomerModel));
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
                                SalesCustomerModel rsmData = new SalesCustomerModel();
                                rsmData.setUserId(userId);
                                rsmData.setCustomerName(data.getCustomerName());
                                rsmData.setYTD(data.getYTD());
                                rsmData.setQTD(data.getQTD());
                                rsmData.setMTD(data.getMTD());
                                rsmData.setPosition(data.getPosition());
                                List<SalesCustomerModel.StateCodeWise> selected = new ArrayList<>();
                                selected.add(dataList.get(position));
                                rsmData.setStateCodeWise(selected);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmData));
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                //dataList.get(position).setUserId(userId);
                                SalesCustomerModel spData = new SalesCustomerModel();
                                spData.setUserId(userId);
                                spData.setCustomerName(data.getCustomerName());
                                spData.setYTD(data.getYTD());
                                spData.setQTD(data.getQTD());
                                spData.setMTD(data.getMTD());
                                spData.setPosition(data.getPosition());
                                List<SalesCustomerModel.StateCodeWise> spSelected = new ArrayList<>();
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
                                SalesCustomerModel productData = new SalesCustomerModel();
                                productData.setUserId(userId);
                                productData.setCustomerName(data.getCustomerName());
                                productData.setYTD(data.getYTD());
                                productData.setQTD(data.getQTD());
                                productData.setMTD(data.getMTD());
                                productData.setPosition(data.getPosition());
                                List<SalesCustomerModel.StateCodeWise> productSelected = new ArrayList<>();
                                productSelected.add(dataList.get(position));
                                productData.setStateCodeWise(productSelected);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, productData));
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

