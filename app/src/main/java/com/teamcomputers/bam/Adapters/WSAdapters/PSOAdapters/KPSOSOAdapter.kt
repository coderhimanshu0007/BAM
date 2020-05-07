package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Adapters.OpenSalesOrder.OSOProductAdapter
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import org.greenrobot.eventbus.EventBus

class KPSOSOAdapter(dashboardActivityContext: DashboardActivity, level: String, type: String, data: List<KPSOSOModel.Datum>, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean) : RecyclerView.Adapter<KPSOSOAdapter.ViewHolder>(), Filterable {
    private val dataList: List<KPSOSOModel.Datum> = data
    private var dataListFiltered: List<KPSOSOModel.Datum>? = data
    internal var level: String = level
    internal var type: String = type
    internal var mActivity: Activity = dashboardActivityContext
    internal var dashboardActivity: DashboardActivity = dashboardActivityContext
    internal var fromRSM: Boolean = fromRSM
    internal var fromSP: Boolean = fromSP
    internal var fromCustomer: Boolean = fromCustomer
    private var layoutManager: LinearLayoutManager? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var tviName: TextView
        internal var tviSOAmount: TextView
        internal var iviOption: ImageView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviSOAmount = itemView.findViewById<View>(R.id.tviSOAmount) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.oso_rsm_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_first_item_value))
        } else if (position == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_second_item_value))
        } else if (position == 2) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_third_item_value))
        } else if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_white))
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.login_bg))
        }
        holder.tviName.setText((position + 1).toString() + ". " + dataListFiltered?.get(position)?.soNumber)

        holder.tviSOAmount.setText(BAMUtil.getRoundOffValue(dataListFiltered?.get(position)?.soAmount!!))

        if (level == "R1") {
            if (fromRSM && fromSP && fromCustomer) {
                holder.iviOption.setVisibility(View.GONE)
            }
        } else if (level == "R2" || level == "R3") {
            if (fromSP && fromCustomer) {
                holder.iviOption.setVisibility(View.GONE)
            }
        } else if (level == "R4" && fromCustomer) {
            holder.iviOption.setVisibility(View.GONE)
        } else if (level == "R4" && !fromCustomer) {
            holder.iviOption.setVisibility(View.VISIBLE)
        }

        holder.iviOption.setOnClickListener(View.OnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mActivity, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            if (level == "R1") {
                popup.menu.getItem(3).isVisible = false
                if (fromSP && fromCustomer) {
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromSP && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                } else if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                }
            } else if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(3).isVisible = false
                if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                }
            } else if (level == "R4") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
                popup.menu.getItem(3).isVisible = false
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 -> {

                        //handle menu1 click
                        dataListFiltered!![position].position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        dataListFiltered!![position].position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu3 -> {
                        //handle menu3 click
                        dataListFiltered!![position].position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu4 -> {
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        })

        holder.itemView.setOnClickListener {
            //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.ACCOUNT_ITEM, position));
        }
        /*dataListFiltered.get(position).setPosition(position)
        val aa = OSOProductAdapter(dashboardActivity, dataListFiltered.get(position))
        layoutManager = LinearLayoutManager(dashboardActivity)
        holder.rviStateCode.setLayoutManager(layoutManager)
        holder.rviStateCode.setAdapter(aa)
        holder.rlStateWise.setOnClickListener(View.OnClickListener {
            if (dataListFiltered.get(position).getOpen() == 0) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand))
                } else {
                    holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand))
                }
                dataListFiltered.get(position).setOpen(1)
                holder.rviStateCode.setVisibility(View.VISIBLE)
            } else if (dataListFiltered.get(position).getOpen() == 1) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse))
                } else {
                    holder.llExpand.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse))
                }
                dataListFiltered.get(position).setOpen(0)
                holder.rviStateCode.setVisibility(View.GONE)
            }
        })*/
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KPSOSOModel.Datum>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.soNumber?.toLowerCase()?.contains(charString.toLowerCase())!! || row.soNumber?.contains(charSequence)!!) {
                            filteredList.add(row)
                        }
                    }

                    dataListFiltered = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = dataListFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, filterResults: Filter.FilterResults) {
                dataListFiltered = filterResults.values as ArrayList<KPSOSOModel.Datum>
                notifyDataSetChanged()
            }
        }
    }

}