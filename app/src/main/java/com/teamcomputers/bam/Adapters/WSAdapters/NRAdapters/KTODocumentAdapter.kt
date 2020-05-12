package com.teamcomputers.bam.Adapters.WSAdapters.NRAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.NRModels.KNRCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class KTODocumentAdapter(val mContext: DashboardActivity, val level: String, val customerData: KNRCustomerModel.Datum, val fromRSM: Boolean, val fromSP: Boolean, val fromProduct: Boolean) : RecyclerView.Adapter<KTODocumentAdapter.ViewHolder>() {
    private var dataList: List<KNRCustomerModel.DocumentNo>? = customerData.documentNo
    internal var data: KNRCustomerModel.Datum = customerData

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tviStateName: TextView
        internal var tviStateAmount: TextView
        internal var iviOption: ImageView

        init {
            this.tviStateName = itemView.findViewById<View>(R.id.tviStateName) as TextView
            this.tviStateAmount = itemView.findViewById<View>(R.id.tviStateAmount) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.to_state_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviStateName.text = (position + 1).toString() + ". " + dataList?.get(position)?.documentNo
        holder.tviStateAmount.text = KBAMUtils.getRoundOffValue(dataList?.get(position)?.amount!!)
        holder.itemView.setOnClickListener {
            val toCustomerModel = KNRCustomerModel().Datum()
            toCustomerModel.customerName = data.customerName
            toCustomerModel.amount = data.amount
            toCustomerModel.position = data.position!!
            val rsmSelected = ArrayList<KNRCustomerModel.DocumentNo>()
            rsmSelected.add(dataList?.get(position)!!)
            toCustomerModel.documentNo = rsmSelected
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.STATE_ITEM, toCustomerModel))
        }
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
                        //dataList.get(position).setUserId(userId);
                        val rsmData = KNRCustomerModel().Datum()
                        rsmData.customerName = data.customerName
                        rsmData.amount = data.amount
                        rsmData.position = data.position!!
                        val rsmSelected = ArrayList<KNRCustomerModel.DocumentNo>()
                        rsmSelected.add(dataList?.get(position)!!)
                        rsmData.documentNo = rsmSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmData))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        //dataList.get(position).setUserId(userId);
                        val spData = KNRCustomerModel().Datum()
                        spData.customerName = data.customerName
                        spData.amount = data.amount
                        spData.position = data.position!!
                        val rsmSelected = ArrayList<KNRCustomerModel.DocumentNo>()
                        rsmSelected.add(dataList?.get(position)!!)
                        spData.documentNo = rsmSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, spData))
                    }
                    R.id.menu3 -> {
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        //dataList.get(position).setUserId(userId);
                        val pData = KNRCustomerModel().Datum()
                        pData.customerName = data.customerName
                        pData.amount = data.amount
                        pData.position = data.position!!
                        val rsmSelected = ArrayList<KNRCustomerModel.DocumentNo>()
                        rsmSelected.add(dataList?.get(position)!!)
                        pData.documentNo = rsmSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, pData))
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }
    }
}