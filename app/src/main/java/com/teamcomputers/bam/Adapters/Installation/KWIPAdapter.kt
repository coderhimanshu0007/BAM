package com.teamcomputers.bam.Adapters.Installation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.WIPModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.installation_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.tviValue1

class KWIPAdapter(val dashboardActivityContext: DashboardActivity, val data: List<WIPModel.Table>) : RecyclerView.Adapter<KWIPAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: WIPModel.Table?) {
            itemView.tviValue1.setText(dataList?.getCustomerName())
            itemView.tviValue21.setText(dataList?.getAmount().toString())
            itemView.tviValue22.setText(dataList?.getZoneName())
            itemView.tviValue23.setText(dataList?.getNOD())
            itemView.tviValue31.setText(dataList?.getCity())
            itemView.tviValue32.setText(dataList?.getCount().toString())
            itemView.tviValue41.setText(dataList?.getRemainingAmount().toString())
            itemView.tviValue42.setText(dataList?.getReason())
            itemView.tviValue51.setText(dataList?.getInvoiceNo())
            itemView.tviValue52.setText(dataList?.getPaymentStatus())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.installation_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data.get(position))
    }

}