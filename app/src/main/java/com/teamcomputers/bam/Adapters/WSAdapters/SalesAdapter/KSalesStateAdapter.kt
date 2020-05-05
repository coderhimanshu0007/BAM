package com.teamcomputers.bam.Adapters.WSAdapters.SalesAdapter

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
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import org.greenrobot.eventbus.EventBus
import java.util.*

class KSalesStateAdapter(dashboardActivityContext: DashboardActivity, userId: String, level: String, data: KSalesCustomerModel.Data, fromRSM: Boolean, fromSP: Boolean, fromProduct: Boolean) : RecyclerView.Adapter<KSalesStateAdapter.ViewHolder>() {
    private var dataList: List<KSalesCustomerModel.StateCodeWise>? = data.stateCodeWise
    internal var data: KSalesCustomerModel.Data = data
    internal var mActivity: Activity = dashboardActivityContext
    internal var fromRSM: Boolean = fromRSM
    internal var fromSP: Boolean = fromSP
    internal var fromProduct: Boolean = fromProduct
    internal var userId: String = userId
    internal var level: String = level
    internal var inflter: LayoutInflater = (LayoutInflater.from(dashboardActivityContext));

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tviStateName: TextView
        internal var tviStateYTD: TextView
        internal var tviStateQTD: TextView
        internal var tviStateMTD: TextView
        internal var iviOption: ImageView

        init {
            this.tviStateName = itemView.findViewById<View>(R.id.tviStateName) as TextView
            this.tviStateYTD = itemView.findViewById<View>(R.id.tviStateYTD) as TextView
            this.tviStateQTD = itemView.findViewById<View>(R.id.tviStateQTD) as TextView
            this.tviStateMTD = itemView.findViewById<View>(R.id.tviStateMTD) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflter.inflate(R.layout.state_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviStateName.text = (position + 1).toString() + ". " + (dataList?.get(position)?.stateCode)
        holder.tviStateYTD.text = BAMUtil.getRoundOffValue(dataList?.get(position)?.ytd!!)
        holder.tviStateQTD.text = BAMUtil.getRoundOffValue(dataList?.get(position)?.qtd!!)
        holder.tviStateMTD.text = BAMUtil.getRoundOffValue(dataList?.get(position)?.mtd!!)
        holder.itemView.setOnClickListener {
            val salesCustomerModel = KSalesCustomerModel().Data()
            salesCustomerModel.customerName = data.customerName
            salesCustomerModel.ytd = data.ytd
            salesCustomerModel.qtd = data.qtd
            salesCustomerModel.mtd = data.mtd
            salesCustomerModel.position = data.position
            val selected = ArrayList<KSalesCustomerModel.StateCodeWise>()
            selected.add(dataList?.get(position)!!)
            salesCustomerModel.stateCodeWise = selected
            //SharedPreferencesController.getInstance(mActivity).setFrom("2");
            //SharedPreferencesController.getInstance(mActivity).setLocation(dataList.get(position).getCustName());
            //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.STATE_SELECT, salesCustomerModel));
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.STATE_ITEM, salesCustomerModel))
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
                        val rsmData = KSalesCustomerModel().Data()
                        rsmData.userId = userId
                        rsmData.customerName = data.customerName
                        rsmData.ytd = data.ytd
                        rsmData.qtd = data.qtd
                        rsmData.mtd = data.mtd
                        rsmData.position = data.position
                        val selected = ArrayList<KSalesCustomerModel.StateCodeWise>()
                        selected.add(dataList?.get(position)!!)
                        rsmData.stateCodeWise = selected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmData))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        //dataList.get(position).setUserId(userId);
                        val spData = KSalesCustomerModel().Data()
                        spData.userId = userId
                        spData.customerName = data.customerName
                        spData.ytd = data.ytd
                        spData.qtd = data.qtd
                        spData.mtd = data.mtd
                        spData.position = data.position
                        val spSelected = ArrayList<KSalesCustomerModel.StateCodeWise>()
                        spSelected.add(dataList?.get(position)!!)
                        spData.stateCodeWise = spSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, spData))
                    }
                    R.id.menu3 -> {
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        //dataList.get(position).setUserId(userId);
                        val productData = KSalesCustomerModel().Data()
                        productData.userId = userId
                        productData.customerName = data.customerName
                        productData.ytd = data.ytd
                        productData.qtd = data.qtd
                        productData.mtd = data.mtd
                        productData.position = data.position
                        val productSelected = ArrayList<KSalesCustomerModel.StateCodeWise>()
                        productSelected.add(dataList?.get(position)!!)
                        productData.stateCodeWise = productSelected
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, productData))
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }
    }

}