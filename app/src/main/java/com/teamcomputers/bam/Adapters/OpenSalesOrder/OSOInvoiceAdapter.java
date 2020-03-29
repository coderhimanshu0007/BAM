package com.teamcomputers.bam.Adapters.OpenSalesOrder;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalOutstanding.TOProductModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOInvoiceModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class OSOInvoiceAdapter extends RecyclerView.Adapter<OSOInvoiceAdapter.ViewHolder> {
    private List<OSOInvoiceModel> dataList;
    String level, type;
    Activity mActivity;
    DashboardActivity dashboardActivity;
    boolean fromRSM, fromSP, fromCustomer;
    private LinearLayoutManager layoutManager;

    public OSOInvoiceAdapter(DashboardActivity dashboardActivityContext, String level, String type, List<OSOInvoiceModel> data, boolean fromRSM, boolean fromSP, boolean fromCustomer) {
        this.dataList = data;
        this.level = level;
        this.type = type;
        this.mActivity = dashboardActivityContext;
        this.dashboardActivity = dashboardActivityContext;
        this.fromRSM = fromRSM;
        this.fromSP = fromSP;
        this.fromCustomer = fromCustomer;
    }

    public void setItems(List<OSOInvoiceModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout, llExpand;
        RelativeLayout rlStateWise;
        TextView tviName, tviSOAmount, tviStateWise;
        RecyclerView rviStateCode;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            rlStateWise = (RelativeLayout) itemView.findViewById(R.id.rlStateWise);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
            this.tviSOAmount = (TextView) itemView.findViewById(R.id.tviSOAmount);
            this.tviStateWise = (TextView) itemView.findViewById(R.id.tviStateWise);
            this.llExpand = (LinearLayout) itemView.findViewById(R.id.llExpand);
            this.rviStateCode = (RecyclerView) itemView.findViewById(R.id.rviStateCode);
        }
    }

    @Override
    public OSOInvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oso_customer_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OSOInvoiceAdapter.ViewHolder holder, final int position) {
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
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getInvoiceNo());

        holder.tviSOAmount.setText(BAMUtil.getRoundOffValue(dataList.get(position).getSOAmount()));
        holder.tviStateWise.setText("Product");

        if (level.equals("R1")) {
            if (fromRSM && fromSP && fromCustomer) {
                holder.iviOption.setVisibility(View.GONE);
            }
        } else if (level.equals("R2") || level.equals("R3")) {
            if (fromSP && fromCustomer) {
                holder.iviOption.setVisibility(View.GONE);
            }
        } else if (level.equals("R4") && fromCustomer) {
            holder.iviOption.setVisibility(View.GONE);
        } else if (level.equals("R4") && !fromCustomer) {
            holder.iviOption.setVisibility(View.VISIBLE);
        }

        holder.iviOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.iviOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                if (level.equals("R1")) {
                    popup.getMenu().getItem(3).setVisible(false);
                    if (fromSP && fromCustomer) {
                        popup.getMenu().getItem(1).setVisible(false);
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromSP && fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromCustomer && fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromSP) {
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromCustomer) {
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
                    }
                } else if (level.equals("R2") || level.equals("R3")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    if (fromSP) {
                        popup.getMenu().getItem(1).setVisible(false);
                    } else if (fromCustomer) {
                        popup.getMenu().getItem(2).setVisible(false);
                    }
                } else if (level.equals("R4")) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu4:
                                //handle menu3 click
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
                //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.ACCOUNT_ITEM, position));
            }
        });
        dataList.get(position).setPosition(position);
        OSOProductAdapter aa = new OSOProductAdapter(dashboardActivity, dataList.get(position));
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
