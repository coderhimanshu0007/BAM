package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.Collection.CollectionWIPDetailModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.llExpand
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.rlCustomerItem
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.tviCustomer
import kotlinx.android.synthetic.main.os_ageing_recyclerview_layout.view.tviTotal
import kotlinx.android.synthetic.main.total_outstanding_recyclerview_layout.view.*

class KCollectionWIPDetailsAdapter(val mContext: DashboardActivity, val dataList: List<CollectionWIPDetailModel.Table>) : RecyclerView.Adapter<KCollectionWIPDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
        }

        fun setData(mContext: Context, dataList: CollectionWIPDetailModel.Table?) {
            itemView.tviCustomer.setText(dataList?.name)
            itemView.tviTotal.setText(dataList?.amount?.let { KBAMUtils.getRoundOffValue(it) })

            /*itemView.tviValue11.setText(dataList?.financePerson)
            itemView.tviValue12.setText(dataList?.nod.toString())

            itemView.tviValue21.setText(dataList?.docSubmissionDate)
            itemView.tviValue22.setText(dataList?.dueDate)
            itemView.tviValue23.setText(dataList?.paymentTermsCode)

            itemView.tviValue31.setText(dataList?.expectedCollectionDate)
            itemView.tviValue32.setText(dataList?.paymentStageName)

            itemView.tviValue41.setText(dataList?.remarks)
            itemView.tviValue42.setText(dataList?.remarksByCollectionTeam)

            itemView.tviValue51.setText(dataList?.bu)
            itemView.tviValue52.setText(dataList?.customerName)

            itemView.tviValue61.setText(dataList?.invoiceNo)
            itemView.tviValue62.setText(dataList?.actualDeliveryDate)
            itemView.tviValue63.setText(dataList?.percentage.toString())*/

            val aa = dataList?.data?.let { KCollectionWIPDetailListAdapter(mContext, it) }
            var layoutManager: LinearLayoutManager? = LinearLayoutManager(mContext)
            itemView.rviData.layoutManager = layoutManager
            itemView.rviData.adapter = aa

            itemView.rlCustomerItem.setOnClickListener {
                if (dataList?.open == 0) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_expand))
                    } else {
                        itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_expand)
                    }
                    dataList?.open = 1
                    itemView.rviData.visibility = View.VISIBLE
                } else if (dataList?.open == 1) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        itemView.llExpand.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_colapse))
                    } else {
                        itemView.llExpand.background = ContextCompat.getDrawable(mContext, R.drawable.ic_colapse)
                    }
                    dataList?.open = 0
                    itemView.rviData.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.total_outstanding_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList.get(position))
    }

}