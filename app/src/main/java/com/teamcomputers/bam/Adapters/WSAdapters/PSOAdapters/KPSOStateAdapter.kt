package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSOCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import org.greenrobot.eventbus.EventBus
import java.util.*

class KPSOStateAdapter(dashboardActivityContext: DashboardActivity, level: String, data: KPSOCustomerModel.Datum, fromRSM: Boolean, fromSP: Boolean, fromProduct: Boolean) : RecyclerView.Adapter<KPSOStateAdapter.ViewHolder>() {
    private var dataList: List<KPSOCustomerModel.StateCodeWise>? = data.stateCodeWise
    internal var data: KPSOCustomerModel.Datum = data
    internal var mActivity: Activity = dashboardActivityContext
    internal var fromRSM: Boolean = fromRSM
    internal var fromSP: Boolean = fromSP
    internal var fromProduct: Boolean = fromProduct
    internal var level: String = level
    internal var inflter: LayoutInflater? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tviStateName: TextView
        internal var tviStateSOAmount: TextView
        internal var iviOption: ImageView

        init {
            this.tviStateName = itemView.findViewById<View>(R.id.tviStateName) as TextView
            this.tviStateSOAmount = itemView.findViewById<View>(R.id.tviStateSOAmount) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.oso_state_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviStateName.text = (position + 1).toString() + ". " + dataList?.get(position)?.stateCode!!
        holder.tviStateSOAmount.text = BAMUtil.getRoundOffValue(dataList?.get(position)?.soAmount!!)
        holder.itemView.setOnClickListener {
            val toCustomerModel = KPSOCustomerModel().Datum()
            toCustomerModel.customerName = data.customerName
            toCustomerModel.sOAmount = data.sOAmount
            toCustomerModel.position = data.position
            val selected = ArrayList<KPSOCustomerModel.StateCodeWise>()
            selected.add(dataList?.get(position)!!)
            toCustomerModel.stateCodeWise = selected
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
            val popup = PopupMenu(mActivity, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.pso_options_menu)
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
                        val rsmData = KPSOCustomerModel().Datum()
                        rsmData.customerName = data.customerName
                        //rsmData.userId = userId
                        rsmData.sOAmount = data.sOAmount
                        rsmData.position = data.position
                        val rsmSelected = ArrayList<KPSOCustomerModel.StateCodeWise>()
                        rsmSelected.add(dataList?.get(position)!!)
                        rsmData.stateCodeWise = rsmSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmData))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        //dataList.get(position).setUserId(userId);
                        val spData = KPSOCustomerModel().Datum()
                        spData.customerName = data.customerName
                        //spData.userId = userId
                        spData.sOAmount = data.sOAmount
                        spData.position = data.position
                        val spSelected = ArrayList<KPSOCustomerModel.StateCodeWise>()
                        spSelected.add(dataList?.get(position)!!)
                        spData.stateCodeWise = spSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, spData))
                    }
                    R.id.menu3 -> {
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        //dataList.get(position).setUserId(userId);
                        val pData = KPSOCustomerModel().Datum()
                        pData.customerName = data.customerName
                        //pData.userId = userId
                        pData.sOAmount = data.sOAmount
                        pData.position = data.position
                        val pSelected = ArrayList<KPSOCustomerModel.StateCodeWise>()
                        pSelected.add(dataList?.get(position)!!)
                        pData.stateCodeWise = pSelected
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