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
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOProductModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import kotlinx.android.synthetic.main.oso_rsm_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.to_customer_recyclerview_layout.view.iviOption
import kotlinx.android.synthetic.main.to_customer_recyclerview_layout.view.llRSMLayout
import kotlinx.android.synthetic.main.to_customer_recyclerview_layout.view.tviName
import org.greenrobot.eventbus.EventBus
import java.util.*

class KPSOProductAdapter(val dashboardActivityContext: DashboardActivity, val level: String, val dataList: List<KPSOProductModel.Datum>, val fromRSM: Boolean, val fromSP: Boolean, val fromCustomer: Boolean, val fromSO: Boolean) : RecyclerView.Adapter<KPSOProductAdapter.ViewHolder>(), Filterable {
    private var dataListFiltered: List<KPSOProductModel.Datum>? = dataList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var product: KPSOProductModel.Datum? = null
        var currentPosition: Int = 0

        init {
            /*itemView.setOnClickListener {
                val productCustomerModel = KPSOProductModel().Datum()
                productCustomerModel.productName = product?.productName
                productCustomerModel.sOAmount = product?.sOAmount
                productCustomerModel.position = currentPosition
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_ITEM, productCustomerModel));
            }*/
        }

        fun showData(dashboardContext: DashboardActivity, productData: KPSOProductModel.Datum?, pos: Int, level: String, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean, fromSO: Boolean) {
            product = productData
            currentPosition = pos
            val mContext = dashboardContext
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
            itemView.tviName.text = (pos + 1).toString() + ". " + productData?.productName

            itemView.tviSOAmount.text = BAMUtil.getRoundOffValue(productData?.sOAmount!!)

            if (level == "R1") {
                if (fromRSM && fromSP && fromSO && fromCustomer) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R2" || level == "R3") {
                if (fromSP && fromSO) {
                    itemView.iviOption.visibility = View.GONE
                }
            } else if (level == "R4" && fromSO) {
                itemView.iviOption.visibility = View.GONE
            } else if (level == "R4" && !fromSO) {
                itemView.iviOption.visibility = View.VISIBLE
            }

            itemView.iviOption.setOnClickListener {
                //creating a popup menu
                val popup = PopupMenu(mContext, itemView.iviOption)
                //inflating menu from xml resource
                popup.inflate(R.menu.pso_options_menu)
                if (level == "R2" || level == "R3") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (level == "R4") {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(2).isVisible = false
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
                if (fromSO) {
                    popup.menu.getItem(3).isVisible = false
                }
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu1 -> {
                            //handle menu1 click
                            val productCustomerModel = KPSOProductModel().Datum()
                            productCustomerModel.code = product?.code
                            productCustomerModel.productName = product?.productName
                            productCustomerModel.sOAmount = product?.sOAmount
                            productCustomerModel.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, productCustomerModel))
                        }
                        R.id.menu2 -> {
                            //handle menu2 click
                            val productCustomerModel = KPSOProductModel().Datum()
                            productCustomerModel.code = product?.code
                            productCustomerModel.productName = product?.productName
                            productCustomerModel.sOAmount = product?.sOAmount
                            productCustomerModel.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, productCustomerModel))
                        }
                        R.id.menu3 -> {
                            //handle menu3 click
                            val productCustomerModel = KPSOProductModel().Datum()
                            productCustomerModel.code = product?.code
                            productCustomerModel.productName = product?.productName
                            productCustomerModel.sOAmount = product?.sOAmount
                            productCustomerModel.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_ITEM, productCustomerModel))
                        }
                        R.id.menu4 -> {
                            val productCustomerModel = KPSOProductModel().Datum()
                            productCustomerModel.code = product?.code
                            productCustomerModel.productName = product?.productName
                            productCustomerModel.sOAmount = product?.sOAmount
                            productCustomerModel.position = pos
                            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SO_ITEM_SELECT, productCustomerModel));
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.oso_rsm_recyclerview_layout, parent, false)

        return KPSOProductAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showData(dashboardActivityContext, dataListFiltered?.get(position), position, level, fromRSM, fromSP, fromCustomer, fromSO)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KPSOProductModel.Datum>()
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
                dataListFiltered = filterResults.values as ArrayList<KPSOProductModel.Datum>
                notifyDataSetChanged()
            }
        }
    }
}