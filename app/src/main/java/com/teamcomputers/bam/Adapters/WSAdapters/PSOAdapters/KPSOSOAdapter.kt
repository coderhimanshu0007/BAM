package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOSOModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import kotlinx.android.synthetic.main.to_invoice_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus

class KPSOSOAdapter(val mContext: DashboardActivity, level: String, type: String, data: List<KPSOSOModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KPSOSOAdapter.ViewHolder>(), Filterable {
    private val dataList: List<KPSOSOModel.Datum> = data
    private var dataListFiltered: List<KPSOSOModel.Datum>? = data
    internal var level: String = level
    internal var type: String = type

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var soData: KPSOSOModel.Datum? = null
        var itemPos: Int = 0

        init {
            itemView.setOnClickListener {
                soData?.position = itemPos
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, soData));
            }
        }

        fun showData(mContext: DashboardActivity, level: String, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean, fromProduct: Boolean, SOData: KPSOSOModel.Datum, pos: Int) {
            soData = SOData
            itemPos = pos
            if (pos == 0) {
                itemView.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_first_item_value))
            } else if (pos == 1) {
                itemView.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_second_item_value))
            } else if (pos == 2) {
                itemView.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_third_item_value))
            } else if (pos % 2 == 0) {
                itemView.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.color_white))
            } else if (pos % 2 == 1) {
                itemView.llRSMLayout.setBackgroundColor(ContextCompat.getColor(mContext,R.color.login_bg))
            }
            itemView.tviStateName.setText((pos + 1).toString() + ". " + SOData.soNumber)

            itemView.tviAmount.setText(BAMUtil.getRoundOffValue(SOData.soAmount!!))
            itemView.tviNOD.text = SOData.nod.toString()!!

            if (level == "R1") {
                if (fromRSM && fromSP && fromCustomer && fromProduct) {
                    itemView.iviOption.setVisibility(View.GONE)
                }
            } else if (level == "R2" || level == "R3") {
                if (fromSP && fromCustomer) {
                    itemView.iviOption.setVisibility(View.GONE)
                }
            } else if (level == "R4" && fromCustomer) {
                itemView.iviOption.setVisibility(View.GONE)
            } else if (level == "R4" && !fromCustomer) {
                itemView.iviOption.setVisibility(View.VISIBLE)
            }

            itemView.iviOption.setOnClickListener(View.OnClickListener {
                //creating a popup menu
                val popup = PopupMenu(mContext, itemView.iviOption)
                //inflating menu from xml resource
                popup.inflate(R.menu.pso_options_menu)
                if (level == "R2" || level == "R3") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(3).isVisible = false
                } else if (level == "R4") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(3).isVisible = false
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
                            SOData?.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, SOData))
                        }
                        R.id.menu2 -> {
                            //handle menu2 click
                            SOData?.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, SOData))
                        }
                        R.id.menu3 -> {
                            //handle menu3 click
                            SOData?.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, SOData))
                        }
                        R.id.menu5 -> {
                            SOData?.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, SOData));
                        }
                    }//handle menu3 click
                    false
                }
                //displaying the popup
                popup.show()
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_invoice_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showData(mContext, level, fromRSM, fromSP, fromCustomer, fromProduct, dataListFiltered?.get(position)!!, position)
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