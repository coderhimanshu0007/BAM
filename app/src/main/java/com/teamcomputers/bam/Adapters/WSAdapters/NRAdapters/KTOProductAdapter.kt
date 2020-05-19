package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRProductModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class KTOProductAdapter(val mContext: DashboardActivity, val level: String, val type: String, val dataList: List<KNRProductModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean) : RecyclerView.Adapter<KTOProductAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KNRProductModel.Datum>? = dataList


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var tviName: TextView
        internal var tviAmount: TextView
        internal var iviOption: ImageView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviAmount = itemView.findViewById<View>(R.id.tviAmount) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_product_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.llRSMLayout.setBackgroundColor(mContext.resources.getColor(R.color.login_bg))
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered?.get(position)?.productName
        holder.tviAmount.text = KBAMUtils.getRoundOffValue(dataListFiltered?.get(position)?.amount!!)

        if (level == "R1") {
            if (fromRSM && fromSP && fromCustomer) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromSP && fromCustomer) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R4" && fromCustomer) {
            holder.iviOption.visibility = View.GONE
        } else if (level == "R4" && !fromCustomer) {
            holder.iviOption.visibility = View.VISIBLE
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mContext, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.pso_options_menu)
            popup.menu.getItem(3).setTitle("Product")
            popup.menu.getItem(4).setTitle("Invoice")
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
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu3 -> {
                        //handle menu3 click
                        dataListFiltered?.get(position)?.position = position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered?.get(position)))
                    }
                    R.id.menu5 -> {
                        //handle menu5 click
                        dataListFiltered?.get(position)?.position = position
                        //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, dataListFiltered?.get(position)))
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }

        holder.itemView.setOnClickListener {
            dataListFiltered?.size
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KNRProductModel.Datum>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.productName?.toLowerCase()?.contains(charString.toLowerCase())!! || row.productName?.contains(charSequence)!!) {
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
                dataListFiltered = filterResults.values as ArrayList<KNRProductModel.Datum>
                notifyDataSetChanged()
            }
        }
    }

}