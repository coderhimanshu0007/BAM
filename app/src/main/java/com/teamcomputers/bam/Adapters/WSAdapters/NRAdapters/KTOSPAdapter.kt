package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRRSMModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus

class KTOSPAdapter(val mContext: DashboardActivity, val type: String, val level: String, val dataList: List<KNRRSMModel.Datum>, val fromRSM: Boolean, val fromCustomer: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KTOSPAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KNRRSMModel.Datum>? = dataList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var llAmountDSO: LinearLayout
        internal var tviName: TextView
        internal var tviAmount: TextView
        internal var tviOutstanding: TextView
        internal var tviDSO: TextView
        internal var iviOption: ImageView
        internal var pBar: ProgressBar

        init {
            this.llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            this.llAmountDSO = itemView.findViewById<View>(R.id.llAmountDSO) as LinearLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.tviOutstanding = itemView.findViewById<View>(R.id.tviOutstanding) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviAmount = itemView.findViewById<View>(R.id.tviAmount) as TextView
            this.tviDSO = itemView.findViewById<View>(R.id.tviDSO) as TextView
            this.pBar = itemView.findViewById<View>(R.id.pBar) as ProgressBar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_rsm_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bar = 0
        holder.llRSMLayout.setBackgroundColor(mContext.resources.getColor(R.color.login_bg))
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered?.get(position)?.name
        holder.tviAmount.text = KBAMUtils.getRoundOffValue(dataListFiltered?.get(position)?.amount!!)
        bar = dataListFiltered?.get(position)?.dso?.toInt()!!
        holder.tviDSO.text = "$bar Days"
        holder.pBar.progress = bar
        if (bar < 30) {
            holder.pBar.progressDrawable.setColorFilter(mContext.resources.getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN)
        } else {
            holder.pBar.progressDrawable.setColorFilter(mContext.resources.getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN)
        }

        if (fromCustomer) {
            holder.llAmountDSO.visibility = View.GONE
            holder.tviOutstanding.text = KBAMUtils.getRoundOffValue(dataListFiltered?.get(position)?.amount!!)
        } else {
            holder.llAmountDSO.visibility = View.VISIBLE
            holder.tviOutstanding.visibility = View.GONE
        }

        if (level == "R1") {
            if (fromRSM && fromCustomer && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromCustomer && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mContext, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.pso_options_menu)
            popup.menu.getItem(3).setTitle("Product")
            popup.menu.getItem(4).setTitle("Invoice")
            if (level == "R1") {
                popup.menu.getItem(1).isVisible = false
                if (fromRSM && fromCustomer) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromRSM && fromProduct) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromCustomer && fromProduct) {
                    popup.menu.getItem(2).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
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
                        //handle menu1 click
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu2 -> {
                    }
                    R.id.menu3 -> {
                        //handle menu3 click
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu4 -> {
                        //handle menu4 click
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu5 -> {
                        //handle menu5 click
                        dataListFiltered?.get(position)?.position = position
                        //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, dataListFiltered?.get(position)))
                    }
                }//handle menu2 click
                false
            }
            //displaying the popup
            popup.show()
        }
        holder.itemView.setOnClickListener {
            dataListFiltered?.get(position)?.position = position
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_CLICK, dataListFiltered?.get(position)))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KNRRSMModel.Datum>()
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
                dataListFiltered = filterResults.values as ArrayList<KNRRSMModel.Datum>
                notifyDataSetChanged()
            }
        }
    }

}