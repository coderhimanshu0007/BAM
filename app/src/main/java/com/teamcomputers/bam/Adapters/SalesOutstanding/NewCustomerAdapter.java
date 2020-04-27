package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.teamcomputers.bam.Models.SalesCustomerModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class NewCustomerAdapter extends RecyclerView.Adapter<NewCustomerAdapter.ViewHolder> implements Filterable {
    private List<SalesCustomerModel> dataList;
    private List<SalesCustomerModel> dataListFiltered;
    Activity mActivity;
    DashboardActivity dashboardActivity;
    boolean fromRSM, fromSP, fromProduct;
    private LinearLayoutManager layoutManager;

    String userId, level;

    int progress = 20;

    public NewCustomerAdapter(DashboardActivity dashboardActivityContext, String userId, String level, List<SalesCustomerModel> data, boolean fromRSM, boolean fromSP, boolean fromProduct) {
        this.dataList = data;
        this.dataListFiltered = data;
        this.mActivity = dashboardActivityContext;
        this.userId = userId;
        this.level = level;
        this.dashboardActivity = dashboardActivityContext;
        this.fromRSM = fromRSM;
        this.fromSP = fromSP;
        this.fromProduct = fromProduct;
    }

    public void setItems(List<SalesCustomerModel> data) {
        this.dataListFiltered = data;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
                    List<SalesCustomerModel> filteredList = new ArrayList<>();
                    for (SalesCustomerModel row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase()) || row.getCustomerName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                dataListFiltered = (ArrayList<SalesCustomerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout, llExpand;
        RelativeLayout rlStateWise;
        TextView tviName, tviYTD, tviQTD, tviMTD;
        RecyclerView rviStateCode;
        ProgressBar pBar;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            rlStateWise = (RelativeLayout) itemView.findViewById(R.id.rlStateWise);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
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
        dataListFiltered.get(position).setOpen(0);
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

        /*if (bar < 35) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 35 && bar < 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 70) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }*/
        if (bar < 50) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 50 && bar < 80) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 80 && bar < 99) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN);
        } else if (bar >= 99) {
            holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
        }
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
        holder.tviName.setText(position + 1 + ". " + dataListFiltered.get(position).getCustomerName());
        holder.tviYTD.setText(BAMUtil.getRoundOffValue(dataListFiltered.get(position).getYTD()));
        holder.tviQTD.setText(BAMUtil.getRoundOffValue(dataListFiltered.get(position).getQTD()));
        holder.tviMTD.setText(BAMUtil.getRoundOffValue(dataListFiltered.get(position).getMTD()));

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
                        popup.getMenu().getItem(3).setVisible(false);
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
                                //dataListFiltered.get(position).setUserId(userId);
                                SalesCustomerModel rsmCustomerModel = new SalesCustomerModel();
                                rsmCustomerModel.setUserId(dataListFiltered.get(position).getUserId());
                                rsmCustomerModel.setCustomerName(dataListFiltered.get(position).getCustomerName());
                                rsmCustomerModel.setYTD(dataListFiltered.get(position).getYTD());
                                rsmCustomerModel.setQTD(dataListFiltered.get(position).getQTD());
                                rsmCustomerModel.setMTD(dataListFiltered.get(position).getMTD());
                                rsmCustomerModel.setPosition(dataListFiltered.get(position).getPosition());
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmCustomerModel));
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                //dataListFiltered.get(position).setUserId(userId);
                                SalesCustomerModel salesCustomerModel = new SalesCustomerModel();
                                salesCustomerModel.setUserId(dataListFiltered.get(position).getUserId());
                                salesCustomerModel.setCustomerName(dataListFiltered.get(position).getCustomerName());
                                salesCustomerModel.setYTD(dataListFiltered.get(position).getYTD());
                                salesCustomerModel.setQTD(dataListFiltered.get(position).getQTD());
                                salesCustomerModel.setMTD(dataListFiltered.get(position).getMTD());
                                salesCustomerModel.setPosition(dataListFiltered.get(position).getPosition());
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, salesCustomerModel));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                            case R.id.menu4:
                                //handle menu3 click
                                //dataListFiltered.get(position).setUserId(userId);
                                SalesCustomerModel productCustomerModel = new SalesCustomerModel();
                                productCustomerModel.setUserId(dataListFiltered.get(position).getUserId());
                                productCustomerModel.setCustomerName(dataListFiltered.get(position).getCustomerName());
                                productCustomerModel.setYTD(dataListFiltered.get(position).getYTD());
                                productCustomerModel.setQTD(dataListFiltered.get(position).getQTD());
                                productCustomerModel.setMTD(dataListFiltered.get(position).getMTD());
                                productCustomerModel.setPosition(dataListFiltered.get(position).getPosition());
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, productCustomerModel));
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
                SalesCustomerModel salesCustomerModel = new SalesCustomerModel();
                salesCustomerModel.setUserId(dataListFiltered.get(position).getUserId());
                salesCustomerModel.setCustomerName(dataListFiltered.get(position).getCustomerName());
                salesCustomerModel.setYTD(dataListFiltered.get(position).getYTD());
                salesCustomerModel.setQTD(dataListFiltered.get(position).getQTD());
                salesCustomerModel.setMTD(dataListFiltered.get(position).getMTD());
                salesCustomerModel.setPosition(dataListFiltered.get(position).getPosition());
                //dataListFiltered.get(position).setUserId(userId);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, salesCustomerModel));
            }
        });
        List<SalesCustomerModel.StateCodeWise> stateCodeWise = dataListFiltered.get(position).getStateCodeWise();
        dataListFiltered.get(position).setPosition(position);
        StateAdapter aa = new StateAdapter(dashboardActivity, userId, level, dataListFiltered.get(position), fromRSM, fromSP, fromProduct);
        layoutManager = new LinearLayoutManager(dashboardActivity);
        holder.rviStateCode.setLayoutManager(layoutManager);
        holder.rviStateCode.setAdapter(aa);
        holder.rlStateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataListFiltered.get(position).getOpen() == 0) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand));
                    } else {
                        holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand));
                    }
                    dataListFiltered.get(position).setOpen(1);
                    holder.rviStateCode.setVisibility(View.VISIBLE);
                } else if (dataListFiltered.get(position).getOpen() == 1) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse));
                    } else {
                        holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse));
                    }
                    dataListFiltered.get(position).setOpen(0);
                    holder.rviStateCode.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }
}

