package com.teamcomputers.bam.Adapters.OrderProcessing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.SOAModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.*


class KSOAuthorizationAdapter(val dashboardActivityContext: DashboardActivity, val data: List<SOAModel.Table>) : RecyclerView.Adapter<KSOAuthorizationAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: SOAModel.Table?, mContext: Context) {
            itemView.tviHeading1.setText("Number")
            val img1 = mContext.getResources().getDrawable(R.drawable.ic_forma_invoice)
            img1.setColorFilter(ContextCompat.getColor(mContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
            itemView.tviHeading1.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null)
            itemView.tviValue1.setText(dataList?.getNo())

            itemView.tviHeading2.setText("Value")
            val img2 = mContext.getResources().getDrawable(R.drawable.ic_group_amount)
            img2.setColorFilter(ContextCompat.getColor(mContext, R.color.end_header_color_bg), android.graphics.PorterDuff.Mode.SRC_IN);
            itemView.tviHeading2.setCompoundDrawablesWithIntrinsicBounds(img2, null, null, null)
            itemView.tviValue2.setText(dataList?.getValue().toString())
            itemView.tviValue3.setText(dataList?.getCount().toString())
            itemView.tviValue4.setText(dataList?.getHoursDays())
            itemView.tviValue5.setText(dataList?.salesCordinator)

            itemView.llItem6.visibility = View.INVISIBLE
            itemView.llrow4.visibility = View.GONE
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