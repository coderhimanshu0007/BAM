package com.teamcomputers.bam.Adapters.Installation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.DOAIRModel
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.installation_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.*
import kotlinx.android.synthetic.main.orderprocesing_recyclerview_layout.view.tviValue1

class KDOAIRAdapter(val dashboardActivityContext: DashboardActivity, val data: List<DOAIRModel.Table>) : RecyclerView.Adapter<KDOAIRAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(dataList: DOAIRModel.Table?, mContext: Context) {
            itemView.tviValue1.setText(dataList?.getCustomerName())
            itemView.tviHeading21.setText("City")
            val img1 = mContext.getResources().getDrawable(R.drawable.ic_shape_location)
            itemView.tviHeading21.setCompoundDrawablesWithIntrinsicBounds(img1, null, null, null)
            itemView.tviValue21.setText(dataList?.getCity())
            itemView.tviValue22.setText(dataList?.getZoneName())
            itemView.tviValue23.setText(dataList?.getCount().toString())
            itemView.tviValue41.visibility = GONE
            itemView.tviValue42.setText(dataList?.getReason())
            itemView.tviValue51.setText(dataList?.getInvoiceNo())
            itemView.tviValue52.setText(dataList?.getPaymentStatus())
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
        holder.setData(data.get(position), dashboardActivityContext)
    }

}