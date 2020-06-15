package com.teamcomputers.bam.Adapters.OrderProcessing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.SPCSModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.*


class KSPCSubmissionAdapter(val dashboardActivityContext: DashboardActivity, val data: List<SPCSModel.Table>) : RecyclerView.Adapter<KSPCSubmissionAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: SPCSModel.Table?, mContext: Context) {
            itemView.tviValue1.setText(dataList?.getCustomerName())

            itemView.tviHeading2.setText("Count")
            val img1 = mContext.getResources().getDrawable(R.drawable.ic_count)
            itemView.tviHeading2.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null)
            itemView.tviValue2.setText(dataList?.getCount().toString())

            itemView.tviHeading3.setText("Hrs/Days")
            val img2 = mContext.getResources().getDrawable(R.drawable.ic_group_hrs_days)
            itemView.tviHeading3.setCompoundDrawablesWithIntrinsicBounds(img2, null, null, null)
            itemView.tviValue3.setText(dataList?.getCount().toString())

            itemView.llItem4.visibility = View.INVISIBLE

            itemView.tviValue5.setText(dataList?.getProjectHeadName())

            itemView.llItem6.visibility = View.INVISIBLE

            itemView.tviValue7.setText(dataList?.getProjectNo())

            itemView.tviHeading8.setText("Reason")
            itemView.tviHeading8.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_reason));
            val params = itemView.tviValue8.getLayoutParams() as LinearLayout.LayoutParams
            //val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(10, 0, 0, 0)
            itemView.tviValue8.setLayoutParams(params)
            itemView.tviHeading8.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

            itemView.tviValue8.setText(dataList?.getReasons())

            itemView.llMargin.visibility = View.INVISIBLE
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
        holder.setData(data.get(position), dashboardActivityContext)
    }

}