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
import com.teamcomputers.bam.Models.WSModels.SalesModels.KSalesProductModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

class KSalesProductAdapter(dashboardActivityContext: DashboardActivity, level: String, type: String, data: List<KSalesProductModel.Data>, fromRSM: Boolean, fromSP: Boolean, fromCustomer: Boolean) : RecyclerView.Adapter<KSalesProductAdapter.ViewHolder>(), Filterable {
    private var dataList: List<KSalesProductModel.Data> = data
    private var dataListFiltered: List<KSalesProductModel.Data> = data
    internal var level: String = level
    internal var type: String = type
    internal var mActivity: Activity = dashboardActivityContext
    internal var fromRSM: Boolean = fromRSM
    internal var fromSP: Boolean = fromSP
    internal var fromCustomer: Boolean = fromCustomer

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var llRSMLayout: LinearLayout
        internal var tviName: TextView
        internal var tviYTD: TextView
        internal var tviQTD: TextView
        internal var tviMTD: TextView
        internal var pBar: ProgressBar
        internal var iviOption: ImageView
        internal var tviSOAmount: TextView

        init {
            llRSMLayout = itemView.findViewById<View>(R.id.llRSMLayout) as LinearLayout
            this.tviName = itemView.findViewById<View>(R.id.tviName) as TextView
            this.iviOption = itemView.findViewById<View>(R.id.iviOption) as ImageView
            this.tviYTD = itemView.findViewById<View>(R.id.tviYTD) as TextView
            this.tviQTD = itemView.findViewById<View>(R.id.tviQTD) as TextView
            this.tviMTD = itemView.findViewById<View>(R.id.tviMTD) as TextView
            this.pBar = itemView.findViewById<View>(R.id.pBar) as ProgressBar
            this.tviSOAmount = itemView.findViewById<View>(R.id.tviSOAmount) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_product_recyclerview_layout, parent, false)

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
        //if (fromSP || fromCustomer) {
        if (fromCustomer) {
            holder.tviYTD.text = KBAMUtils.getRoundOffValue(dataListFiltered!![position].ytd!!)
            holder.tviQTD.text = KBAMUtils.getRoundOffValue(dataListFiltered!![position].qtd!!)
            holder.tviMTD.text = KBAMUtils.getRoundOffValue(dataListFiltered!![position].mtd!!)
            holder.pBar.visibility = View.GONE
        } else {
            var target = ""
            var actual = ""
            var bar = 0
            if (type == "YTD") {
                target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetYTD!!)
                actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].ytd!!)
                bar = dataList!![position].ytdPercentage?.toInt()!!
            } else if (type == "QTD") {
                target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetQTD!!)
                actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].qtd!!)
                bar = dataList!![position].qtdPercentage?.toInt()!!
            } else if (type == "MTD") {
                target = KBAMUtils.getRoundOffValue(dataListFiltered!![position].targetMTD!!)
                actual = KBAMUtils.getRoundOffValue(dataListFiltered!![position].mtd!!)
                bar = dataList!![position].mtdPercentage?.toInt()!!
            }

            //holder.tviYTD.setText(KBAMUtils.getRoundOffValue(dataList.get(position).getYTD()));
            //holder.tviQTD.setText(KBAMUtils.getRoundOffValue(dataList.get(position).getQTD()));
            //holder.tviMTD.setText(KBAMUtils.getRoundOffValue(dataList.get(position).getMTD()));
            holder.tviYTD.text = target
            holder.tviQTD.text = actual
            holder.tviSOAmount.text = KBAMUtils.getRoundOffValue(dataListFiltered!![position].soAmount!!)

            //bar = (dataListFiltered.get(position).getYTDPercentage()).intValue();
            holder.tviMTD.text = "$bar%"
            holder.pBar.progress = bar

            /*if (bar < 35) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 35 && bar < 70) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_mid), PorterDuff.Mode.SRC_IN);
            } else if (bar >= 70) {
                holder.pBar.getProgressDrawable().setColorFilter(mActivity.getResources().getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN);
            }*/
            if (bar < 50) {
                holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_progress_start), PorterDuff.Mode.SRC_IN)
            } else if (bar >= 50 && bar < 80) {
                holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_orange), PorterDuff.Mode.SRC_IN)
            } else if (bar >= 80 && bar < 99) {
                holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_amber), PorterDuff.Mode.SRC_IN)
            } else if (bar >= 99) {
                holder.pBar.progressDrawable.setColorFilter(mActivity.resources.getColor(R.color.color_progress_end), PorterDuff.Mode.SRC_IN)
            }
        }

        if (level == "R1") {
            if (fromRSM && fromSP && fromCustomer) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R2" || level == "R3") {
            if (fromSP && fromCustomer) {
                holder.iviOption.visibility = View.GONE
            }
        } else if (level == "R4" && fromCustomer) {
            holder.iviOption.visibility = View.GONE
        } else if (level == "R4" && !fromCustomer) {
            holder.iviOption.visibility = View.VISIBLE
        }

        holder.iviOption.setOnClickListener {
            //creating a popup menu
            val popup = PopupMenu(mActivity, holder.iviOption)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            if (level == "R1") {
                popup.menu.getItem(3).isVisible = false
                if (fromSP && fromCustomer) {
                    popup.menu.getItem(1).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromSP && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer && fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                    popup.menu.getItem(2).isVisible = false
                } else if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                } else if (fromRSM) {
                    popup.menu.getItem(0).isVisible = false
                }
            } else if (level == "R2" || level == "R3") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(3).isVisible = false
                if (fromSP) {
                    popup.menu.getItem(1).isVisible = false
                } else if (fromCustomer) {
                    popup.menu.getItem(2).isVisible = false
                }
            } else if (level == "R4") {
                popup.menu.getItem(0).isVisible = false
                popup.menu.getItem(1).isVisible = false
                popup.menu.getItem(3).isVisible = false
            }
            //adding click listener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu1 ->
                        //handle menu1 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.RSM_MENU_SELECT, dataListFiltered!![position]))
                    R.id.menu2 ->
                        //handle menu2 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.SP_MENU_SELECT, dataListFiltered!![position]))
                    R.id.menu3 ->
                        //handle menu3 click
                        EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.CUSTOMER_MENU_SELECT, dataListFiltered!![position]))
                    R.id.menu4 -> {
                    }
                }//handle menu3 click
                false
            }
            //displaying the popup
            popup.show()
        }

        holder.itemView.setOnClickListener {
            //EventBus.getDefault().post(new EventObject(BAMConstant.ClickEvents.ACCOUNT_ITEM, position));
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    dataListFiltered = dataList
                } else {
                    val filteredList = ArrayList<KSalesProductModel.Data>()
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
                dataListFiltered = filterResults.values as ArrayList<KSalesProductModel.Data>
                notifyDataSetChanged()
            }
        }
    }
}