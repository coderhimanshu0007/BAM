package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRInvoiceModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.to_invoice_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.to_state_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.to_state_recyclerview_layout.view.iviOption
import kotlinx.android.synthetic.main.to_state_recyclerview_layout.view.tviStateName
import org.greenrobot.eventbus.EventBus

class KTOInvoiceAdapter(val mContext: DashboardActivity, val level: String, val customerData: List<KNRInvoiceModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KTOInvoiceAdapter.ViewHolder>(), Filterable {

    private var dataList: List<KNRInvoiceModel.Datum>? = customerData

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var invoice: KNRInvoiceModel.Datum? = null
        var itemPos: Int = 0

        init {
            itemView.setOnClickListener {
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.STATE_ITEM, invoice))
            }
        }

        fun showData(mContext: DashboardActivity, level: String, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean, fromProduct: Boolean, invoiceData: KNRInvoiceModel.Datum, pos: Int) {
            invoice = invoiceData
            itemPos = pos
            itemView.tviStateName.text = (pos + 1).toString() + ". " + invoiceData.documentNo
            itemView.tviAmount.text = KBAMUtils.getRoundOffValue(invoiceData.amount!!)
            itemView.tviNOD.text = invoiceData.nod.toString()!!
            if (level == "R1") {
                if (fromRSM && fromSP && fromProduct && fromCustomer) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R2" || level == "R3") {
                if (fromSP && fromProduct) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R4") {
                itemView.iviOption.visibility = View.GONE
            }

            itemView.iviOption.setOnClickListener {
                //creating a popup menu
                val popup = PopupMenu(mContext, itemView.iviOption)
                //inflating menu from xml resource
                popup.inflate(R.menu.pso_options_menu)
                popup.menu.getItem(3).setTitle("Product")
                popup.menu.getItem(4).setTitle("Invoice")
                if (level == "R1") {
                    popup.menu.getItem(4).isVisible = false
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
                            //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, invoiceData))
                        }
                        R.id.menu2 -> {
                            //handle menu2 click
                            //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, invoiceData))
                        }
                        R.id.menu3 -> {
                            //handle menu3 click
                            //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, invoiceData))
                        }
                        R.id.menu4 -> {
                            //handle menu3 click
                            //EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, invoiceData))

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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_invoice_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showData(mContext, level, fromRSM, fromSP, fromCustomer, fromProduct, dataList?.get(position)!!, position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataList = customerData
                } else {
                    val filteredList = ArrayList<KNRInvoiceModel.Datum>()
                    for (row in customerData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.documentNo?.toLowerCase()?.contains(charString.toLowerCase())!! || row.documentNo?.contains(charSequence)!!) {
                            filteredList.add(row)
                        }
                    }

                    dataList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = dataList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, filterResults: Filter.FilterResults) {
                dataList = filterResults.values as ArrayList<KNRInvoiceModel.Datum>
                notifyDataSetChanged()
            }
        }
    }
}