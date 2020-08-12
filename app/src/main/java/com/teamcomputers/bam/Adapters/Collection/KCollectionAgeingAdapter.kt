package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.Collection.AgeingModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.*

class KCollectionAgeingAdapter(val mContext: DashboardActivity, val dataList: List<AgeingModel.Table>) : RecyclerView.Adapter<KCollectionAgeingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: AgeingModel.Table?) {
            itemView.tviCustomer.setText(dataList?.customerName)
            itemView.tviTotal.setText(dataList?.total?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue1.setText(dataList?.c030?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue2.setText(dataList?.c3160?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue3.setText(dataList?.c6190?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue4.setText(dataList?.c91120?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue5.setText(dataList?.c121180?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue6.setText(dataList?.c181270?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue7.setText(dataList?.c270?.let { KBAMUtils.getRoundOffValue(it) })
            itemView.tviValue8.setText(dataList?.nOTDUE?.let { KBAMUtils.getRoundOffValue(it) })

            itemView.rlCustomerItem.setOnClickListener {
                if (dataList?.open == 0) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expand))
                    } else {
                        itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_expand)
                    }
                    dataList?.open = 1
                    itemView.llCustomerDetail.visibility = View.VISIBLE
                } else if (dataList?.open == 1) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_colapse))
                    } else {
                        itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_colapse)
                    }
                    dataList?.open = 0
                    itemView.llCustomerDetail.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.os_ageing_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}