package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters.KPSORSMAdapter.ViewHolder
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import org.greenrobot.eventbus.EventBus
import java.util.*

class KPSORSMAdapter(dashboardActivityContext: DashboardActivity, type: String, level: String, data: List<KPSORSMModel.Datum>, fromSP: Boolean, fromCustomer: Boolean, fromProduct: Boolean) : RecyclerView.Adapter<ViewHolder>(), Filterable {
    val dataList: List<KPSORSMModel.Datum> = data
    var dataListFiltered: List<KPSORSMModel.Datum>? = data
    var mActivity: Activity = dashboardActivityContext
    var type: String = type
    var level: String = level
    var fromSP: Boolean = fromSP
    var fromCustomer: Boolean = fromCustomer
    var fromProduct: Boolean = fromProduct

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llRSMLayout: LinearLayout
        var tviName: TextView
        var tviSOAmount: TextView
        var iviOption: ImageView

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
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered?.get(position)?.name
        holder.tviSOAmount.text = dataListFiltered?.get(position)?.soAmount?.let { BAMUtil.getRoundOffValue(it) }
        if (level == "R1") {
            if (fromSP && fromCustomer && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mActivity, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            popup.menu.getItem(3).title = "SO"
            if (level == "R1") {
                popup.menu.getItem(0).isVisible = false
                if (fromSP && fromCustomer) {
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromSP && fromProduct) {
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromCustomer && fromProduct) {
                    popup.menu.getItem(2).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                } else if (fromProduct) {
                    popup.menu.getItem(3).isVisible = false
                }
            } else if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
                if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                } else if (fromProduct) {
                    popup.menu.getItem(3).isVisible = false
                }
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 -> {
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        dataListFiltered!![position].position=position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_CLICK, dataListFiltered?.get(position)))
                    }
                    R.id.menu3 -> {
                        //handle menu3 click
                        dataListFiltered!![position].position=position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        dataListFiltered!![position].position=position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                }//handle menu1 click
                false
            }
            //displaying the popup
            popup.show()
        }
        holder.itemView.setOnClickListener {
            //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, position));
            dataListFiltered!![position].position=position
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_CLICK, dataListFiltered?.get(position)))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KPSORSMModel.Datum>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!! || row.name?.contains(charSequence)!!) {
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
                dataListFiltered = filterResults.values as ArrayList<KPSORSMModel.Datum>
                notifyDataSetChanged()
            }
        }
    }

}