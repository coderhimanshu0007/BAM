package com.teamcomputers.bam.ExpandableRecyclerview.expandables;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamcomputers.bam.Activities.DashboardActivity;
import com.teamcomputers.bam.Activities.KDashboardActivity;
import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.viewholder.ChildViewHolder;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem;
import com.teamcomputers.bam.R;


public class NavigationItemChildViewHolder extends ChildViewHolder {

    private ImageView imageViewNavigation;
    private TextView textViewNavigation;
    private LinearLayout childRootLinearLayout;
    private Context context;

    public NavigationItemChildViewHolder(View view, final Context context) {
        super(view);
        this.context = context;
        imageViewNavigation =  view.findViewById(R.id.iv_nav_item);
        textViewNavigation =  view.findViewById(R.id.tv_nav_item);
        childRootLinearLayout =  view.findViewById(R.id.ll_child_root);
    }

    public void bind(final NavigationItem navigationItem) {
        imageViewNavigation.setImageResource(navigationItem.getNavigationImage());
//        textViewNavigation.setText(BAMUtil.fromHtml(navigationItem.getNavigationTitle()));
        textViewNavigation.setText(new SpannableString(navigationItem.getNavigationTitle()));
        childRootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((KDashboardActivity) context).replaceFragment(navigationItem.getFragment(), new Bundle());
            }
        });
    }

}
