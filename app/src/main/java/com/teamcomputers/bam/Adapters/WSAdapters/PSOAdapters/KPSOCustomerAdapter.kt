package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import org.greenrobot.eventbus.EventBus

class KPSOCustomerAdapter(val mContext: DashboardActivity, val level: String, val dataList: List<KPSOCustomerModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromSO: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KPSOCustomerAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KPSOCustomerModel.Datum>? = dataList
    private var layoutManager: LinearLayoutManager? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var llExpand: LinearLayout
        internal var rlStateWise: RelativeLayout
        internal var tviName: TextView
        internal var tviSOAmount: TextView
        internal var tviStateWise: TextView
        internal var rviStateCode: RecyclerView
        internal var iviOption: ImageView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            rlStateWise = itemView.findViewById<View>(R.id.rlStateWise) as RelativeLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviSOAmount = itemView.findViewById<View>(R.id.tviSOAmount) as TextView
            this.tviStateWise = itemView.findViewById<View>(R.id.tviStateWise) as TextView
            this.llExpand = itemView.findViewById<View>(R.id.llExpand) as LinearLayout
            this.rviStateCode = itemView.findViewById<View>(R.id.rviStateCode) as RecyclerView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.oso_customer_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_first_item_value))
        } else if (position == 1) {
            holder.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_second_item_value))
        } else if (position == 2) {
            holder.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_third_item_value))
        } else if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white))
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.login_bg))
        }
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered?.get(position)?.customerName

        holder.tviSOAmount.text = BAMUtil.getRoundOffValue(dataListFiltered?.get(position)?.sOAmount!!)

        if (level == "R1") {
            if (fromRSM && fromSP && fromSO) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromSP && fromSO) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R4" && fromSO) {
            holder.iviOption.visibility = View.GONE
        } else if (level == "R4" && !fromSO) {
            holder.iviOption.visibility = View.VISIBLE
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mContext, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.pso_options_menu)
            popup.menu.getItem(3).title = "SO"
            if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(2).isVisible = false
            } else if (level == "R4") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
                popup.menu.getItem(2).isVisible = false
            }
            popup.menu.getItem(2).isVisible = false
            if (fromRSM) {
                popup.menu.getItem(0).isVisible = false
            }
            if (fromSP) {
                popup.menu.getItem(1).isVisible = false
            }
            if (fromSO) {
                popup.menu.getItem(3).isVisible = false
            }
            if (fromProduct) {
                popup.menu.getItem(4).isVisible = false
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 -> {
                        //handle menu1 click
                        val productCustomerModel = KPSOCustomerModel().Datum()
                        productCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        productCustomerModel.sOAmount = dataListFiltered?.get(position)?.sOAmount
                        productCustomerModel.position = dataListFiltered?.get(position)?.position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, productCustomerModel))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        val productCustomerModel = KPSOCustomerModel().Datum()
                        productCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        productCustomerModel.sOAmount = dataListFiltered?.get(position)?.sOAmount
                        productCustomerModel.position = dataListFiltered?.get(position)?.position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, productCustomerModel))
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        val productCustomerModel = KPSOCustomerModel().Datum()
                        productCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        productCustomerModel.sOAmount = dataListFiltered?.get(position)?.sOAmount
                        productCustomerModel.position = dataListFiltered?.get(position)?.position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_ITEM, productCustomerModel))
                    }
                    R.id.menu5 -> {
                        val productCustomerModel = KPSOCustomerModel().Datum()
                        productCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        productCustomerModel.sOAmount = dataListFiltered?.get(position)?.sOAmount
                        productCustomerModel.position = dataListFiltered?.get(position)?.position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, productCustomerModel));
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }

        holder.itemView.setOnClickListener {
            val productCustomerModel = KPSOCustomerModel().Datum()
            productCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
            productCustomerModel.sOAmount = dataListFiltered?.get(position)?.sOAmount
            productCustomerModel.position = dataListFiltered?.get(position)?.position
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_ITEM, productCustomerModel));
        }
        dataListFiltered?.get(position)?.position = position
        val aa = KPSOStateAdapter(mContext, level, dataListFiltered?.get(position)!!, fromRSM, fromSP, fromSO)
        layoutManager = LinearLayoutManager(mContext)
        holder.rviStateCode.layoutManager = layoutManager
        holder.rviStateCode.adapter = aa
        holder.rlStateWise.setOnClickListener {
            if (dataListFiltered?.get(position)?.open == 0) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expand))
                } else {
                    holder.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_expand)
                }
                dataListFiltered?.get(position)?.open = 1
                holder.rviStateCode.visibility = View.VISIBLE
            } else if (dataListFiltered?.get(position)?.open == 1) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_colapse))
                } else {
                    holder.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_colapse)
                }
                dataListFiltered?.get(position)?.open = 0
                holder.rviStateCode.visibility = View.GONE
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KPSOCustomerModel.Datum>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.customerName?.toLowerCase()?.contains(charString.toLowerCase())!! || row.customerName?.contains(charSequence)!!) {
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
                dataListFiltered = filterResults.values as ArrayList<KPSOCustomerModel.Datum>
                notifyDataSetChanged()
            }
        }
    }

}