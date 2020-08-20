package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.Collection.WIPCustomerModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus

class KCollectionWIPDetailsAdapter(val mContext: DashboardActivity, val dataList: List<WIPCustomerModel.Datum>) : RecyclerView.Adapter<KCollectionWIPDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentDataItem: WIPCustomerModel.Datum? = null
        var currentPosition: Int = 0
        init {
            itemView.setOnClickListener {
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.WIP_CUSTOMER_SELECT, currentDataItem))
            }
        }

        fun setData(mContext: Context, dataList: WIPCustomerModel.Datum?, pos: Int) {
            currentDataItem = dataList
            currentPosition = pos
            itemView.tviCustomer.setText(dataList?.customerName)
            itemView.tviTotal.setText(dataList?.amount?.let { KBAMUtils.getRoundOffValue(it) })

            itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_navigate_next_black_24dp))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.total_outstanding_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position), position)
    }
}