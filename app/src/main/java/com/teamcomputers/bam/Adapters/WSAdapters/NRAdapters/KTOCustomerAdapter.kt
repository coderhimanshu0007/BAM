package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

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
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.to_customer_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class KTOCustomerAdapter(val mContext: DashboardActivity, val userId: String, val level: String, val dataList: List<KNRCustomerModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KTOCustomerAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KNRCustomerModel.Datum>? = dataList
    private var layoutManager: LinearLayoutManager? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentDataItem: KNRCustomerModel.Datum? = null
        var currentPosition: Int = 0

        internal var llRSMLayout: LinearLayout
        internal var llExpand: LinearLayout
        internal var rlStateWise: RelativeLayout
        internal var rviStateCode: RecyclerView
        internal var iviOption: ImageView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            rlStateWise = itemView.findViewById<View>(R.id.rlStateWise) as RelativeLayout
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.llExpand = itemView.findViewById<View>(R.id.llExpand) as LinearLayout
            this.rviStateCode = itemView.findViewById<View>(R.id.rviStateCode) as RecyclerView

            itemView.setOnClickListener {
                val toCustomerModel = KNRCustomerModel().Datum()
                toCustomerModel.customerName = currentDataItem?.customerName
                toCustomerModel.amount = currentDataItem?.amount
                toCustomerModel.position = currentDataItem?.position!!
                toCustomerModel.open = currentDataItem?.open!!
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, toCustomerModel))
            }
        }

        fun setData(filterDataList: KNRCustomerModel.Datum?, pos: Int) {
            itemView.tviName.text = (position + 1).toString() + ". " + filterDataList?.customerName
            itemView.tviAmount.text = KBAMUtils.getRoundOffValue(filterDataList?.amount!!)

            currentDataItem = filterDataList
            currentPosition = pos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KTOCustomerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_customer_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataListFiltered?.get(position)?.open = 0
        holder.llRSMLayout.setBackgroundColor(mContext.resources.getColor(R.color.login_bg))
        holder.setData(dataListFiltered?.get(position), position)

        if (level == "R1") {
            if (fromRSM && fromSP && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromSP && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R4") {
            holder.iviOption.visibility = View.GONE
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mContext, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            if (level == "R1") {
                popup.menu.getItem(2).isVisible = false
                if (fromSP && fromProduct) {
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromSP && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                } else if (fromProduct && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromProduct) {
                    popup.menu.getItem(2).isVisible = false
                } else if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                }
            } else if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(2).isVisible = false
                if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromProduct) {
                    popup.menu.getItem(3).isVisible = false
                }
            } else if (level == "R4") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
                popup.menu.getItem(2).isVisible = false
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 -> {
                        //handle menu1 click
                        //dataListFiltered.get(position).setUserId(userId);
                        val rsmCustomerModel = KNRCustomerModel().Datum()
                        //rsmCustomerModel.userId = userId
                        rsmCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        rsmCustomerModel.amount = dataListFiltered?.get(position)?.amount
                        rsmCustomerModel.position = dataListFiltered?.get(position)?.position!!
                        rsmCustomerModel.open = dataListFiltered?.get(position)?.open!!
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmCustomerModel))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        //dataListFiltered.get(position).setUserId(userId);
                        val salesCustomerModel = KNRCustomerModel().Datum()
                        //salesCustomerModel.userId = userId
                        salesCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        salesCustomerModel.amount = dataListFiltered?.get(position)?.amount
                        salesCustomerModel.position = dataListFiltered?.get(position)?.position!!
                        salesCustomerModel.open = dataListFiltered?.get(position)?.open!!

                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, salesCustomerModel))
                    }
                    R.id.menu3 -> {
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        val toCustomerModel = KNRCustomerModel().Datum()
                        //toCustomerModel.userId = userId
                        toCustomerModel.customerName = dataListFiltered?.get(position)?.customerName
                        toCustomerModel.amount = dataListFiltered?.get(position)?.amount
                        toCustomerModel.position = dataListFiltered?.get(position)?.position!!
                        toCustomerModel.open = dataListFiltered?.get(position)?.open!!
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, toCustomerModel))
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }

        dataListFiltered?.get(position)?.position = position
        val aa = KTODocumentAdapter(mContext, level, dataListFiltered!!.get(position), fromRSM, fromSP, fromProduct)
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
                    val filteredList = ArrayList<KNRCustomerModel.Datum>()
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
                dataListFiltered = filterResults.values as ArrayList<KNRCustomerModel.Datum>
                notifyDataSetChanged()
            }
        }
    }
}