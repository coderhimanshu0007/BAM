package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Models.Collection.TotalOutstandingModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.tviValue11
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.tviValue12
import kotlinx.android.synthetic.main.total_outstanding_invoice_detail_layout.view.*

class KCollectionTotalOutstandingInvoiceDetailListAdapter(val mContext: Context, val dataList: List<TotalOutstandingModel.Datum>) : RecyclerView.Adapter<KCollectionTotalOutstandingInvoiceDetailListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: TotalOutstandingModel.Datum?) {
            itemView.tviValue11.setText(dataList?.postingDate)
            itemView.tviValue12.setText(dataList?.dueDate)
            itemView.tviValue13.setText(dataList?.bu)
            itemView.tviValue221.setText(dataList?.documentNo)
            itemView.tviValue222.setText(dataList?.amount?.let { KBAMUtils.getRoundOffValue(it) })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.total_outstanding_invoice_detail_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}