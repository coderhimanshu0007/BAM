package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.Collection.CollectionWIPDetailModel
import com.teamcomputers.bam.Models.Collection.TotalOutstandingModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.*

class KCollectionWIPDetailListAdapter(val mContext: Context, val dataList: List<CollectionWIPDetailModel.Datum>) : RecyclerView.Adapter<KCollectionWIPDetailListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: CollectionWIPDetailModel.Datum?) {
            itemView.tviValue11.setText(dataList?.invoiceDate)
            itemView.tviValue12.setText(dataList?.deliveryDate)

            itemView.tviValue21.setText(dataList?.installationDate)
            itemView.tviValue22.setText(dataList?.nod.toString())
            itemView.tviValue23.setText(dataList?.flag)

            //itemView.tviValue31.setText(dataList?.)

            itemView.tviValue41.setText(dataList?.bu)
            itemView.tviValue42.setText(dataList?.customerPONo)

            itemView.tviValue51.setText(dataList?.invoiceNo)
            itemView.tviValue52.setText(dataList?.percentage.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wip_details_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}