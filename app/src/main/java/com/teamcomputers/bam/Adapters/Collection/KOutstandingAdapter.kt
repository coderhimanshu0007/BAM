package com.teamcomputers.bam.Adapters.Collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.teamcomputers.bam.Activities.DashboardActivity
import com.teamcomputers.bam.Models.Collection.AgeingModel
import com.teamcomputers.bam.Models.Collection.OutstandingModel
import com.teamcomputers.bam.R
import com.teamcomputers.bam.Utils.KBAMUtils
import kotlinx.android.synthetic.main.outstanding_gridview_layout.view.*

class KOutstandingAdapter(val mContext: DashboardActivity, val dataList: List<OutstandingModel.Table>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflator = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var gridItem = inflator.inflate(R.layout.outstanding_gridview_layout, null)
        gridItem.tviValue1.text = dataList[position].totalOutStandingInvoice!!.toString()
        gridItem.tviValue2.text = KBAMUtils.getRoundOffValue(dataList[position].totalOutStandingAmount!!)
        return gridItem
    }

    override fun getItem(position: Int): Any? {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataList?.size!!
    }

}