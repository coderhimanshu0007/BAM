package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Models.Collection.TotalOutstandingModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.total_outstanding_details_recyclerview_layout.view.*

class KCollectionOutstandingDetailsAdapter(val mContext: Context, val from: String, val dataList: List<TotalOutstandingModel.Datum>) : RecyclerView.Adapter<KCollectionOutstandingDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: TotalOutstandingModel.Datum?) {
            itemView.tviValue11.setText(dataList?.financePerson)
            itemView.tviValue12.setText(dataList?.nod.toString())

            itemView.tviValue21.setText(dataList?.submissionDate)
            itemView.tviValue22.setText(dataList?.dueDate)
            itemView.tviValue23.setText(dataList?.paymentTermsCode)

            itemView.tviValue31.setText(dataList?.expectedDate)
            itemView.tviValue32.setText(dataList?.paymentStage)

            itemView.tviValue41.setText(dataList?.remarks1)
            itemView.tviValue42.setText(dataList?.remarks2)

            itemView.tviValue51.setText(dataList?.bu)
            itemView.tviValue52.setText(dataList?.customerPONo)

            itemView.tviValue61.setText(dataList?.invoiceNo)
            itemView.tviValue62.setText(dataList?.dueDate)
            itemView.tviValue63.setText(dataList?.percentage.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.total_outstanding_details_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}