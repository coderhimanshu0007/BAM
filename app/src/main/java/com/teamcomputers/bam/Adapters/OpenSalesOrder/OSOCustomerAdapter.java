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
import com.teamcomputers.bam.Adapters.TotalOutstanding.TOStateAdapter;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalOutstanding.TOCustomerModel;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSOCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class OSOCustomerAdapter extends RecyclerView.Adapter<OSOCustomerAdapter.ViewHolder> {
    private List<OSOCustomerModel> dataList;
    Activity mActivity;
    DashboardActivity dashboardActivity;
    boolean fromRSM, fromSP, fromProduct;
    private LinearLayoutManager layoutManager;

    String userId, level;

    int progress = 20;

    public OSOCustomerAdapter(DashboardActivity dashboardActivityContext, String userId, String level, List<OSOCustomerModel> data, boolean fromRSM, boolean fromSP, boolean fromProduct) {
        this.dataList = data;
        this.mActivity = dashboardActivityContext;
        this.userId = userId;
        this.level = level;
        this.dashboardActivity = dashboardActivityContext;
        this.fromRSM = fromRSM;
        this.fromSP = fromSP;
        this.fromProduct = fromProduct;
    }

    public void setItems(List<OSOCustomerModel> data) {
        this.dataList = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout, llExpand;
        RelativeLayout rlStateWise;
        TextView tviName, tviSOAmount;
        RecyclerView rviStateCode;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            rlStateWise = (RelativeLayout) itemView.findViewById(R.id.rlStateWise);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
            this.tviSOAmount = (TextView) itemView.findViewById(R.id.tviSOAmount);
            this.llExpand = (LinearLayout) itemView.findViewById(R.id.llExpand);
            this.rviStateCode = (RecyclerView) itemView.findViewById(R.id.rviStateCode);
        }
    }

    @Override
    public OSOCustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oso_customer_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OSOCustomerAdapter.ViewHolder holder, final int position) {
        dataList.get(position).setOpen(0);
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
        holder.tviName.setText(position + 1 + ". " + dataList.get(position).getCustomerName());
        holder.tviSOAmount.setText(BAMUtil.getRoundOffValue(dataList.get(position).getSOAmount()));

        holder.iviOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mActivity, holder.iviOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                popup.getMenu().getItem(3).setTitle("Invoice");
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
                                dataList.get(position).setUserId(userId);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                dataList.get(position).setUserId(userId);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, dataList.get(position)));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                            case R.id.menu4:
                                //handle menu3 click
                                dataList.get(position).setUserId(userId);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, dataList.get(position)));
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
                //SharedPreferencesController.getInstance(mActivity).setFrom("2");
                //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
                dataList.get(position).setUserId(userId);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, dataList.get(position)));
            }
        });
        //List<TOCustomerModel.StateCodeWise> stateCodeWise = dataList.get(position).getStateCodeWise();
        dataList.get(position).setPosition(position);
        OSOStateAdapter aa = new OSOStateAdapter(dashboardActivity, userId, level, dataList.get(position), fromRSM, fromSP, fromProduct);
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

