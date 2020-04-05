package com.teamcomputers.bam.Adapters.TotalOutstanding;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalOutstanding.TORSMSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class TORSMAdapter extends RecyclerView.Adapter<TORSMAdapter.ViewHolder> {
    private List<TORSMSalesModel> dataList;
    Activity mActivity;
    String type, level;
    boolean fromSP, fromCustomer, fromProduct;

    public TORSMAdapter(DashboardActivity dashboardActivityContext, String type, String level, List<TORSMSalesModel> data, boolean fromSP, boolean fromCustomer, boolean fromProduct) {
        this.dataList = data;
        this.type = type;
        this.level = level;
        this.mActivity = dashboardActivityContext;
        this.fromSP = fromSP;
        this.fromCustomer = fromCustomer;
        this.fromProduct = fromProduct;
    }

    public void setItems(List<TORSMSalesModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout;
        TextView tviName, tviAmount, tviDSO;
        ImageView iviOption;
        ProgressBar pBar;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
            this.tviAmount = (TextView) itemView.findViewById(R.id.tviAmount);
            this.tviDSO = (TextView) itemView.findViewById(R.id.tviDSO);
            this.pBar = (ProgressBar) itemView.findViewById(R.id.pBar);
        }
    }

    @Override
    public TORSMAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.to_rsm_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TORSMAdapter.ViewHolder holder, final int position) {
        int bar = 0;
        /*if (position == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_first_item_value));
        } else if (position == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_second_item_value));
        } else if (position == 2) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_third_item_value));
        } else if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.color_white));
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.login_bg));
        }*/
        holder.llRSMLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.login_bg));
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getName());
        holder.tviAmount.setText(BAMUtil.getRoundOffValue(dataList.get(position).getAmount()));
        bar = (dataList.get(position).getDSO()).intValue();
        holder.tviDSO.setText(bar + " Days");
        holder.pBar.setProgress(bar);
        if (bar < 30) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        } else {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        }

        if (level.equals("R1")) {
            if (fromSP && fromCustomer && fromProduct) {
                holder.iviOption.setVisibility(View.GONE);
            }
        }

        holder.iviOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.iviOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                if (level.equals("R1")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    if (fromSP && fromCustomer) {
                        popup.getMenu().getItem(1).setVisible(false);
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromSP && fromProduct) {
                        popup.getMenu().getItem(1).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromCustomer && fromProduct) {
                        popup.getMenu().getItem(2).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromSP) {
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromCustomer) {
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromProduct) {
                        popup.getMenu().getItem(3).setVisible(false);
                    }
                } else if (level.equals("R2") || level.equals("R3")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    if (fromCustomer) {
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromProduct) {
                        popup.getMenu().getItem(3).setVisible(false);
                    }
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                dataList.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, dataList.get(position)));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                dataList.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu4:
                                //handle menu3 click
                                dataList.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, dataList.get(position)));
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
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

