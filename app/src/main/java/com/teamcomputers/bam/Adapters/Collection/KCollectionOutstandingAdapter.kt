package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.BAMConstant
import com.teamcomputers.bam.Models.Collection.OutstandingDataModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus

class KCollectionOutstandingAdapter(val mContext: DashboardActivity, val dataList: List<OutstandingDataModel.Datum>) : RecyclerView.Adapter<KCollectionOutstandingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currentDataItem: OutstandingDataModel.Datum? = null
        var currentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                EventBus.getDefault().post(EventObject(BAMConstant.ClickEvents.COLLECTION_CUSTOMER_SELECT, currentDataItem))
            }
        }

        fun setData(mContext: Context, dataList: OutstandingDataModel.Datum?, pos: Int) {
            currentDataItem = dataList
            currentPosition = pos
            itemView.tviCustomer.setText(dataList?.customerName)
            itemView.tviTotal.setText(dataList?.amount?.let { KBAMUtils.getRoundOffValue(it) })

            itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_navigate_next_black_24dp))

            /*itemView.tviValue11.setText(dataList?.financePerson)
            itemView.tviValue12.setText(dataList?.nod.toString())

            itemView.tviValue21.setText(dataList?.docSubmissionDate)
            itemView.tviValue22.setText(dataList?.dueDate)
            itemView.tviValue23.setText(dataList?.paymentTermsCode)

            itemView.tviValue31.setText(dataList?.expectedCollectionDate)
            itemView.tviValue32.setText(dataList?.paymentStageName)

            itemView.tviValue41.setText(dataList?.remarks)
            itemView.tviValue42.setText(dataList?.remarksByCollectionTeam)

            itemView.tviValue51.setText(dataList?.bu)
            itemView.tviValue52.setText(dataList?.customerName)

            itemView.tviValue61.setText(dataList?.invoiceNo)
            itemView.tviValue62.setText(dataList?.actualDeliveryDate)
            itemView.tviValue63.setText(dataList?.percentage.toString())*/

            /* val aa = dataList?.data?.let { KCollectionOutstandingDetailsAdapter(mContext, it) }
             var layoutManager: LinearLayoutManager? = LinearLayoutManager(mContext)
             itemView.rviData.layoutManager = layoutManager
             itemView.rviData.adapter = aa

             itemView.rlCustomerItem.setOnClickListener {
                 if (dataList?.open == 0) {
                     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                         itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expand))
                     } else {
                         itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_expand)
                     }
                     dataList?.open = 1
                     itemView.rviData.visibility = View.VISIBLE
                 } else if (dataList?.open == 1) {
                     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                         itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_colapse))
                     } else {
                         itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_colapse)
                     }
                     dataList?.open = 0
                     itemView.rviData.visibility = View.GONE
                 }
             }*/
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