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
import kotlinx.android.synthetic.main.to_product_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus
import java.util.*

class KTOProductAdapter(val mContext: DashboardActivity, val level: String, val type: String, val dataList: List<KNRProductModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean, val fromInvoice: Boolean) : RecyclerView.Adapter<KTOProductAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KNRProductModel.Datum>? = dataList


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product: KNRProductModel.Datum? = null
        var itemPos: Int = 0

        init {
        }

        fun showData(mContext: DashboardActivity, level: String, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean, fromInvoice: Boolean, productData: KNRProductModel.Datum, pos: Int) {
            product = productData
            itemPos = pos
            itemView.llRSMLayout.setBackgroundColor(mContext.resources.getColor(R.color.login_bg))
            itemView.tviName.text = (position + 1).toString() + ". " + productData?.productName
            itemView.tviAmount.text = KBAMUtils.getRoundOffValue(productData?.amount!!)

            if (level == "R1") {
                if (fromRSM && fromSP && fromCustomer) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R2" || level == "R3") {
                if (fromSP && fromCustomer) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R4" && fromCustomer) {
                itemView.iviOption.visibility = View.GONE
            } else if (level == "R4" && !fromCustomer) {
                itemView.iviOption.visibility = View.VISIBLE
            }

            itemView.iviOption.setOnClickListener {
                //creating a popup menu
                val popup = PopupMenu(mContext, itemView.iviOption)
                //inflating menu from xml resource
                popup.inflate(R.menu.pso_options_menu)
                popup.menu.getItem(3).setTitle("Invoice")
                if (level == "R2" || level == "R3") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (level == "R4") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                }
                popup.menu.getItem(4).isVisible = false
                if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                }
                if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                }
                if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                }
                if (fromInvoice) {
                    popup.menu.getItem(3).isVisible = false
                }
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu1 -> {
                            //handle menu1 click
                            productData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, productData))
                        }
                        R.id.menu2 -> {
                            //handle menu2 click
                            productData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, productData))
                        }
                        R.id.menu3 -> {
                            //handle menu3 click
                            productData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, productData))
                        }
                        R.id.menu4 -> {
                            //handle menu5 click
                            productData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, productData))
                        }
                    }//handle menu3 click
                    false
                }
                //displaying the popup
                popup.show()
            }
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
        holder.showData(mContext, level, fromRSM, fromSP, fromCustomer, fromInvoice, dataListFiltered?.get(position)!!, position)

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