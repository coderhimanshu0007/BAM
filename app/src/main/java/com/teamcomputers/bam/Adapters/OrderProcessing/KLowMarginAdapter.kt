package com.teamcomputers.bam.Adapters.OrderProcessing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.LowMarginModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.*

class KLowMarginAdapter(val dashboardActivityContext: DashboardActivity, val data: List<LowMarginModel.Table>) : RecyclerView.Adapter<KLowMarginAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: LowMarginModel.Table?) {
            itemView.tviValue1.setText(dataList?.getCustomerName())
            itemView.tviValue2.setText(dataList?.getBU())
            itemView.tviValue3.setText(dataList?.getCount().toString())
            itemView.tviValue4.setText(dataList?.getHoursDays())

            itemView.llrow3.visibility = View.INVISIBLE

            itemView.tviValue7.setText(dataList?.getProjectNo())

            itemView.tviHeading8.setText("Amount")
            itemView.tviValue8.setText(dataList?.getAmount().toString())

            itemView.tviValue9.setText(dataList?.getMargin().toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orderprocesing_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data.get(position))
    }

}