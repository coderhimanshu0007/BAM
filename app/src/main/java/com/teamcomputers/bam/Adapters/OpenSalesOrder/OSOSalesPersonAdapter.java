package com.teamcomputers.bam.Adapters.OpenSalesOrder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Interface.BAMConstant;
import com.teamcomputers.bam.Models.TotalSalesOrder.OSORSMSalesModel;
import com.teamcomputers.bam.Models.common.EventObject;
import com.teamcomputers.bam.R;
import com.teamcomputers.bam.Utils.BAMUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OSOSalesPersonAdapter extends RecyclerView.Adapter<OSOSalesPersonAdapter.ViewHolder> implements Filterable {
    private List<OSORSMSalesModel> dataList;
    private List<OSORSMSalesModel> dataListFiltered;
    Activity mActivity;
    String type, level;
    boolean fromRSM, fromCustomer, fromProduct;

    public OSOSalesPersonAdapter(DashboardActivity dashboardActivityContext, String type, String level, List<OSORSMSalesModel> data, boolean fromRSM, boolean fromCustomer, boolean fromProduct) {
        this.dataList = data;
        this.dataListFiltered = data;
        this.type = type;
        this.level = level;
        this.mActivity = dashboardActivityContext;
        this.fromRSM = fromRSM;
        this.fromCustomer = fromCustomer;
        this.fromProduct = fromProduct;
    }

    public void setItems(List<OSORSMSalesModel> data) {
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
                    List<OSORSMSalesModel> filteredList = new ArrayList<>();
                    for (OSORSMSalesModel row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getName().contains(charSequence)) {
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
                dataListFiltered = (ArrayList<OSORSMSalesModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRSMLayout;
        TextView tviName, tviSOAmount;
        ImageView iviOption;

        public ViewHolder(View itemView) {
            super(itemView);
            llRSMLayout = (LinearLayout) itemView.findViewById(R.id.llRSMLayout);
            this.tviName = (TextView) itemView.findViewById(R.id.tviName);
            this.iviOption = (ImageView) itemView.findViewById(R.id.iviOption);
            this.tviSOAmount = (TextView) itemView.findViewById(R.id.tviSOAmount);
        }
    }

    @Override
    public OSOSalesPersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oso_rsm_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        holder.tviName.setText(position + 1 + ". " + dataListFiltered.get(position).getName());
        holder.tviSOAmount.setText(BAMUtil.getRoundOffValue(dataListFiltered.get(position).getSOAmount()));
        if (level.equals("R1")) {
            if (fromRSM && fromCustomer && fromProduct) {
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
                popup.getMenu().getItem(3).setTitle("Invoice");
                if (level.equals("R1")) {
                    popup.getMenu().getItem(1).setVisible(false);
                    if (fromRSM && fromCustomer) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(2).setVisible(false);
                    } else if (fromRSM && fromProduct) {
                        popup.getMenu().getItem(0).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromCustomer && fromProduct) {
                        popup.getMenu().getItem(2).setVisible(false);
                        popup.getMenu().getItem(3).setVisible(false);
                    } else if (fromRSM) {
                        popup.getMenu().getItem(0).setVisible(false);
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
                                dataListFiltered.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, dataListFiltered.get(position)));
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                dataListFiltered.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered.get(position)));
                                break;
                            case R.id.menu4:
                                //handle menu3 click
                                dataListFiltered.get(position).setPosition(position);
                                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, dataListFiltered.get(position)));
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
                dataListFiltered.get(position).setPosition(position);
                EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.SP_CLICK, dataListFiltered.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }
}

