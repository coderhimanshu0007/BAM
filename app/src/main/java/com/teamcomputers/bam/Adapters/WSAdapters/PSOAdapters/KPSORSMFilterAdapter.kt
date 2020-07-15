package com.teamcomputers.bam.Adapters.WSAdapters.PSOAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Interface.KBAMConstant
import com.teamcomputers.bam.Models.WSModels.PSOModels.KPSORSMModel
import com.teamcomputers.bam.Models.common.EventObject
import com.teamcomputers.bam.R
import kotlinx.android.synthetic.main.filter_recyclerview_layout.view.*
import org.greenrobot.eventbus.EventBus


class KPSORSMFilterAdapter(val dashboardActivityContext: DashboardActivity, val data: List<KPSORSMModel.Datum>) : RecyclerView.Adapter<KPSORSMFilterAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
        }

        fun showData(filterData: KPSORSMModel.Datum, position: Int) {
            itemView.tviName.setText(filterData.name)
            if (filterData.isSelected)
                itemView.cbSelect.isChecked = true

            itemView.cbSelect.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.ITEM_SELECTED, position))
                } else {
                    EventBus.getDefault().post(EventObject(KBAMConstant.Events.ITEM_UNSELECTED, position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filter_recyclerview_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showData(data.get(position), position)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}