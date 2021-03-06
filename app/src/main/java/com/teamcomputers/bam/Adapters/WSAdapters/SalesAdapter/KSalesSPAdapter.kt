package com.teamcomputers.bam.Adapters.WSAdapters.SalesAdapter

import android.app.Activity
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesRSMModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class KSalesSPAdapter(dashboardActivityContext: DashboardActivity, type: String, level: String, data: List<KSalesRSMModel.Data>, fromRSM: Boolean, fromCustomer: Boolean, fromProduct: Boolean) : RecyclerView.Adapter<KSalesSPAdapter.ViewHolder>(), Filterable {
    private var mActivity: Activity = dashboardActivityContext
    private var type: String = type
    private var level: String = level
    private var dataList: List<KSalesRSMModel.Data> = data
    private var dataListFiltered: List<KSalesRSMModel.Data>? = data
    private var fromRSM: Boolean = fromRSM
    private var fromCustomer: Boolean = fromCustomer
    private var fromProduct: Boolean = fromProduct

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sales_person_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_first_item_value))
        } else if (position == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_second_item_value))
        } else if (position == 2) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_third_item_value))
        } else if (position % 2 == 0) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.color_white))
        } else if (position % 2 == 1) {
            holder.llRSMLayout.setBackgroundColor(mActivity.resources.getColor(R.color.login_bg))
        }
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered!![position].name
        var target = ""
        var actual = ""
        var bar = 0
        if (type == "YTD") {
            target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetYTD!!)
            actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].ytd!!)
            //bar = (dataList.get(position).getYTDPercentage()).intValue();
        } else if (type == "QTD") {
            target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetQTD!!)
            actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].qtd!!)
            //bar = (dataList.get(position).getQTDPercentage()).intValue();
        } else if (type == "MTD") {
            target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetMTD!!)
            actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].mtd!!)
            //bar = (dataList.get(position).getMTDPercentage()).intValue();
        }
        holder.tviTarget.text = target
        holder.tviActual.text = actual
        holder.tviSOAmount.text = KBAMUtils.getRoundOffValue(dataListFiltered!![position].soAmount!!)

        bar = dataListFiltered!![position].ytdPercentage?.toInt()!!
        holder.tviACH.text = "$bar%"
        holder.pBar.progress = bar

        if (bar < 50) {
            holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN)
        } else if (bar >= 50 && bar < 80) {
            holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN)
        } else if (bar >= 80 && bar < 99) {
            holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN)
        } else if (bar >= 99) {
            holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN)
        }
        if (level == "R0" || level == "R1") {
            if (fromRSM && fromCustomer && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromCustomer && fromProduct) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R4" && fromCustomer) {
            holder.iviOption.visibility = View.GONE
        }
        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mActivity, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            popup.menu.getItem(1).isVisible = false
            if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
            } else if (level == "R4") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
            }
            if (fromRSM) {
                popup.menu.getItem(0).isVisible = false
            }
            if (fromCustomer) {
                popup.menu.getItem(2).isVisible = false
            }
            if (fromProduct) {
                popup.menu.getItem(3).isVisible = false
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 ->
                        //handle menu1 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataListFiltered!![position]))
                    R.id.menu2 -> {
                    }
                    R.id.menu3 ->
                        //handle menu3 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered!![position]))
                    R.id.menu4 ->
                        //handle menu3 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.PRODUCT_MENU_SELECT, dataListFiltered!![position]))
                }//handle menu2 click
                false
            }
            //displaying the popup
            popup.show()
        }
        holder.itemView.setOnClickListener {
            //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.RSM_CLICK, position));
            dataListFiltered!![position].position = position
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_CLICK, dataListFiltered!![position]))
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KSalesRSMModel.Data>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase()?.contains(charString.toLowerCase())!! || row.name?.contentEquals(charSequence)!!) {
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
                dataListFiltered = filterResults.values as ArrayList<KSalesRSMModel.Data>
                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var tviName: TextView
        internal var tviTarget: TextView
        internal var tviActual: TextView
        internal var tviACH: TextView
        internal var pBar: ProgressBar
        internal var iviOption: ImageView
        internal var tviSOAmount: TextView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviTarget = itemView.findViewById<View>(R.id.tviTarget) as TextView
            this.tviActual = itemView.findViewById<View>(R.id.tviActual) as TextView
            this.tviACH = itemView.findViewById<View>(R.id.tviACH) as TextView
            this.pBar = itemView.findViewById<View>(R.id.pBar) as ProgressBar
            this.tviSOAmount = itemView.findViewById<View>(R.id.tviSOAmount) as TextView
        }
    }
}