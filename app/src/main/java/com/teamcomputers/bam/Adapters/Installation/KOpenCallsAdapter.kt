package com.teamcomputers.bam.Adapters.Installation

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.OpenCallsModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.installation_recyclerview_layout.view.*

class KOpenCallsAdapter(val dashboardActivityContext: DashboardActivity, val data: List<OpenCallsModel.Table>) : RecyclerView.Adapter<KOpenCallsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: OpenCallsModel.Table?) {
            itemView.tviValue1.setText(dataList?.getCustomerName())
            itemView.tviValue21.setText(dataList?.getAmount().toString())
            itemView.tviValue22.setText(dataList?.getZoneName())
            itemView.tviValue23.setText(dataList?.getNOD().toString())
            itemView.tviValue41.setText(dataList?.getRemainingAmount().toString())
            itemView.tviValue42.setText(dataList?.getReason())
            itemView.tviValue51.setText(dataList?.getInvoiceNo())
            itemView.tviValue52.visibility = INVISIBLE
            itemView.llrowi3.visibility = GONE
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