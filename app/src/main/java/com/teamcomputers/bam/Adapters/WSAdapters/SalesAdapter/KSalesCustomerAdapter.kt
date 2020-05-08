package com.teamcomputers.bam.Adapters.WSAdapters.SalesAdapter

import android.app.Activity
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.BAMUtil
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class KSalesCustomerAdapter(dashboardActivityContext: DashboardActivity, userId: String, level: String, data: List<KSalesCustomerModel.Data>, fromRSM: Boolean, fromSP: Boolean, fromProduct: Boolean) : RecyclerView.Adapter<KSalesCustomerAdapter.ViewHolder>(), Filterable {
    private val dataList: List<KSalesCustomerModel.Data> = data
    private var dataListFiltered: List<KSalesCustomerModel.Data> = data
    internal var mActivity: Activity = dashboardActivityContext
    internal var dashboardActivity: DashboardActivity = dashboardActivityContext
    internal var fromRSM: Boolean = fromRSM
    internal var fromSP: Boolean = fromSP
    internal var fromProduct: Boolean = fromProduct
    private var layoutManager: LinearLayoutManager? = null

    internal var userId: String = userId
    internal var level: String = level

    internal var progress = 20

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var llExpand: LinearLayout
        internal var rlStateWise: RelativeLayout
        internal var tviName: TextView
        internal var tviYTD: TextView
        internal var tviQTD: TextView
        internal var tviMTD: TextView
        internal var tviSOAmount : TextView
        internal var rviStateCode: RecyclerView
        internal var pBar: ProgressBar
        internal var iviOption: ImageView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            rlStateWise = itemView.findViewById<View>(R.id.rlStateWise) as RelativeLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviYTD = itemView.findViewById<View>(R.id.tviYTD) as TextView
            this.tviQTD = itemView.findViewById<View>(R.id.tviQTD) as TextView
            this.tviMTD = itemView.findViewById<View>(R.id.tviMTD) as TextView
            this.tviSOAmount = itemView.findViewById<View>(R.id.tviSOAmount) as TextView
            this.llExpand = itemView.findViewById<View>(R.id.llExpand) as LinearLayout
            this.rviStateCode = itemView.findViewById<View>(R.id.rviStateCode) as RecyclerView
            this.pBar = itemView.findViewById<View>(R.id.pBar) as ProgressBar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_customer_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataListFiltered!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataListFiltered[position].open = 0
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
        var bar = 0
        bar = progress * (position + 1)
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
        holder.tviName.text = (position + 1).toString() + ". " + dataListFiltered[position].customerName
        holder.tviYTD.text = KBAMUtils.getRoundOffValue(dataListFiltered[position].ytd!!)
        holder.tviQTD.text = KBAMUtils.getRoundOffValue(dataListFiltered[position].qtd!!)
        holder.tviMTD.text = KBAMUtils.getRoundOffValue(dataListFiltered[position].mtd!!)
        holder.tviSOAmount.text = KBAMUtils.getRoundOffValue(dataListFiltered[position].soAmount!!)

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
                    popup.menu.getItem(3).isVisible = false
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
                        //dataListFiltered.get(position).setUserId(userId);
                        val rsmCustomerModel = KSalesCustomerModel().Data()
                        //rsmCustomerModel.userId = dataListFiltered[position].getUserId()
                        rsmCustomerModel.customerName = dataListFiltered[position].customerName
                        rsmCustomerModel.ytd = dataListFiltered[position].ytd
                        rsmCustomerModel.qtd = dataListFiltered[position].qtd
                        rsmCustomerModel.mtd = dataListFiltered[position].mtd
                        rsmCustomerModel.position = dataListFiltered[position].position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, rsmCustomerModel))
                    }
                    R.id.menu2 -> {
                        //handle menu2 click
                        //dataListFiltered.get(position).setUserId(userId);
                        val salesCustomerModel = KSalesCustomerModel().Data()
                        //salesCustomerModel.userId = dataListFiltered[position].getUserId()
                        salesCustomerModel.customerName = dataListFiltered[position].customerName
                        salesCustomerModel.ytd = dataListFiltered[position].ytd
                        salesCustomerModel.qtd = dataListFiltered[position].qtd
                        salesCustomerModel.mtd = dataListFiltered[position].mtd
                        salesCustomerModel.position = dataListFiltered[position].position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, salesCustomerModel))
                    }
                    R.id.menu3 -> {
                    }
                    R.id.menu4 -> {
                        //handle menu3 click
                        //dataListFiltered.get(position).setUserId(userId);
                        val productCustomerModel = KSalesCustomerModel().Data()
                        //productCustomerModel.userId = dataListFiltered[position].getUserId()
                        productCustomerModel.customerName = dataListFiltered[position].customerName
                        productCustomerModel.ytd = dataListFiltered[position].ytd
                        productCustomerModel.qtd = dataListFiltered[position].qtd
                        productCustomerModel.mtd = dataListFiltered[position].mtd
                        productCustomerModel.position = dataListFiltered[position].position
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, productCustomerModel))
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }

        holder.itemView.setOnClickListener {
            val salesCustomerModel = KSalesCustomerModel().Data()
            //salesCustomerModel.userId = dataListFiltered[position].getUserId()
            salesCustomerModel.customerName = dataListFiltered[position].customerName
            salesCustomerModel.ytd = dataListFiltered[position].ytd
            salesCustomerModel.qtd = dataListFiltered[position].qtd
            salesCustomerModel.mtd = dataListFiltered[position].mtd
            salesCustomerModel.position = dataListFiltered[position].position
            //dataListFiltered.get(position).setUserId(userId);
            EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_SELECT, salesCustomerModel))
        }
        val stateCodeWise = dataListFiltered[position].stateCodeWise
        dataListFiltered[position].position = position
        val aa = KSalesStateAdapter(dashboardActivity, userId, level, dataListFiltered[position], fromRSM, fromSP, fromProduct)
        layoutManager = LinearLayoutManager(dashboardActivity)
        holder.rviStateCode.layoutManager = layoutManager
        holder.rviStateCode.adapter = aa
        holder.rlStateWise.setOnClickListener {
            if (dataListFiltered[position].open == 0) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_expand))
                } else {
                    holder.llExpand.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_expand)
                }
                dataListFiltered[position].open = 1
                holder.rviStateCode.visibility = View.VISIBLE
            } else if (dataListFiltered[position].open == 1) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse))
                } else {
                    holder.llExpand.background = ContextCompat.getDrawable(mActivity, R.drawable.ic_colapse)
                }
                dataListFiltered[position].open = 0
                holder.rviStateCode.visibility = View.GONE
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KSalesCustomerModel.Data>()
                    for (row in dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.customerName?.toLowerCase()?.contains(charString.toLowerCase())!! || row.customerName?.contentEquals(charSequence)!!) {
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
                dataListFiltered = filterResults.values as ArrayList<KSalesCustomerModel.Data>
                notifyDataSetChanged()
            }
        }
    }

}