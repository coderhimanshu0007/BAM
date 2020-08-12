package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.Collection.CollectionCustomerDetailsModel
import com.teamcomputers.bam.Models.Collection.CollectionWIPDetailModel
import com.teamcomputers.bam.Models.Collection.TotalOutstandingModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.*

class KCollectionCustomerDetailListAdapter(val mContext: Context, val dataList: List<CollectionCustomerDetailsModel.Datum>) : RecyclerView.Adapter<KCollectionCustomerDetailListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: CollectionCustomerDetailsModel.Datum?) {
            itemView.tviValue11.setText(dataList?.expectedDate)
            itemView.tviValue12.setText(dataList?.bu)

            itemView.tviValue21.setText(dataList?.invoiceNo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_detail_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}