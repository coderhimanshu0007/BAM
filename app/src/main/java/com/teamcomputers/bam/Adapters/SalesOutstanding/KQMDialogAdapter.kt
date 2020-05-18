package com.teamcomputers.bam.Adapters.SalesOutstanding

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Models.NewYTDQTDModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.qmdialog_recyclerview_layout.view.*
import java.text.DecimalFormat

class KQMDialogAdapter(val mContext: Activity, val dataList: List<NewYTDQTDModel.QTD>) : RecyclerView.Adapter<KQMDialogAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun setData(context: Activity, data: NewYTDQTDModel.QTD?) {
            itemView.tviType.setText((position + 1).toString() + ". " + data?.getName())
            itemView.tviTarget.setText(KBAMUtils.getRoundOffValue(data?.getTarget()!!))
            itemView.tviActual.setText(KBAMUtils.getRoundOffValue(data?.getActual()!!))
            val df2 = DecimalFormat("#.##")
            itemView.tviPercent.setText(df2.format(data?.getPercentage()) + "%")
            val bar = data?.getPercentage()!!.toInt()
            if (bar < 49) {
                itemView.tviPercent.setTextColor(context.getResources().getColor(R.color.color_progress_start))
            } else if (bar >= 49 && bar < 79) {
                itemView.tviPercent.setTextColor(context.getResources().getColor(R.color.color_orange))
            } else if (bar >= 79 && bar < 99) {
                itemView.tviPercent.setTextColor(context.getResources().getColor(R.color.color_amber))
            } else if (bar >= 99) {
                itemView.tviPercent.setTextColor(context.getResources().getColor(R.color.color_progress_end))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.qmdialog_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(mContext, dataList[position])
    }

}