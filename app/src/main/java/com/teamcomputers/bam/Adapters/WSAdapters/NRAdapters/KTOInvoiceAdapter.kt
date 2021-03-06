package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.NonNull
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRInvoiceModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.to_invoice_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.to_state_recyclerview_layout.view.iviOption
import kotlinx.android.synthetic.main.to_state_recyclerview_layout.view.tviStateName
import org.greenrobot.eventbus.EventBus


class KTOInvoiceAdapter(val mContext: DashboardActivity, val level: String, val customerData: List<KNRInvoiceModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var dataList: List<KNRInvoiceModel.Datum>? = customerData

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun showLoadingView(viewHolder: LoadingViewHolder, position: Int) {
            //ProgressBar would be displayed
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                popup.menu.getItem(3).setTitle("Invoice")
                if (level == "R2" || level == "R3") {
                    popup.menu.getItem(0).isVisible = false
                    //popup.menu.getItem(2).isVisible = false
                } else if (level == "R4") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                }
                popup.menu.getItem(3).isVisible = false
                if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                }
                if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                }
                if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                }
                if (fromProduct) {
                    popup.menu.getItem(4).isVisible = false
                }
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu1 -> {
                            //handle menu1 click
                            invoiceData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, invoiceData))
                        }
                        R.id.menu2 -> {
                            //handle menu2 click
                            invoiceData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, invoiceData))
                        }
                        R.id.menu3 -> {
                            //handle menu3 click
                            invoiceData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, invoiceData))
                        }
                        R.id.menu5 -> {
                            //handle menu3 click
                            invoiceData.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, invoiceData))

                        }
                    }//handle menu3 click
                    false
                }
                //displaying the popup
                popup.show()
            }
        }
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType === VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.to_invoice_recyclerview_layout, parent, false)
            return ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            return LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (dataList == null) 0 else dataList?.size!!
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (dataList?.get(position) == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.showData(mContext, level, fromRSM, fromSP, fromCustomer, fromProduct, dataList?.get(position)!!, position)
        } else if (holder is LoadingViewHolder) {
            holder.showLoadingView(holder as LoadingViewHolder, position)
        }
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