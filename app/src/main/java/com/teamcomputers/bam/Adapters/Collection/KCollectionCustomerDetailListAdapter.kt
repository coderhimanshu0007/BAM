package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Models.Collection.CollectionCustomerDetailsModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.*

class KCollectionCustomerDetailListAdapter(val mContext: Context, val from: String, val dataList: List<CollectionCustomerDetailsModel.Datum>) : RecyclerView.Adapter<KCollectionCustomerDetailListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, from: String, dataList: CollectionCustomerDetailsModel.Datum?) {
            if (from == "ECW") {
                dispEData(dataList)
            } else if (from == "ECM") {
                dispEData(dataList)
            } else if (from == "PCW") {
                dispPData(dataList)
            } else if (from == "PCM") {
                dispPData(dataList)
            }
            //itemView.tviValue11.setText(dataList?.expectedDate)
            //itemView.tviValue21.setText(dataList?.invoiceNo)
            itemView.tviValue12.setText(dataList?.bu)
            itemView.tviValue22.setText(dataList?.amount?.let { KBAMUtils.getRoundOffValue(it) })
        }

        private fun dispEData(dataList: CollectionCustomerDetailsModel.Datum?) {
            itemView.tviValue11.setText(dataList?.expectedDate)
            itemView.tviValue21.setText(dataList?.invoiceNo)
        }

        private fun dispPData(dataList: CollectionCustomerDetailsModel.Datum?) {
            itemView.tviValue11.setText(dataList?.postingDate)
            itemView.tviValue21.setText(dataList?.documentNo)
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
        holder.setData(mContext, from, dataList.get(position))
    }

}