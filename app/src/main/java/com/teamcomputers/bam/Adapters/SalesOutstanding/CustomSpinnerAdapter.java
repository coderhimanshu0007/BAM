package com.teamcomputers.bam.Adapters.SalesOutstanding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.teamcomputers.bam.Models.FiscalYearModel;
import com.teamcomputers.bam.R;

public class CustomSpinnerAdapter extends BaseAdapter {
    Context context;
    FiscalYearModel fiscalYearModel;
    LayoutInflater inflter;

    public CustomSpinnerAdapter(Context context, FiscalYearModel fiscalYearModel) {
        this.context = context;
        this.fiscalYearModel = fiscalYearModel;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return fiscalYearModel.getFascialYear().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView iviFiscalYear = (TextView) view.findViewById(R.id.iviFiscalYear);
        iviFiscalYear.setText("FY" + fiscalYearModel.getFascialYear().get(position).getYear().substring(0, 4));
        return view;
    }
}
