package com.teamcomputers.bam.ExpandableRecyclerview.expandables;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.adapter.ExpandableRecyclerAdapter;
import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.model.ParentListItem;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItemParentModel;
import com.teamcomputers.bam.R;

import java.util.List;

public class NavigationExpandable extends ExpandableRecyclerAdapter<NavigationItemParentViewHolder, NavigationItemChildViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public NavigationExpandable(@NonNull List<? extends ParentListItem> parentItemList, Context mContext) {
        super(parentItemList);
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }

    @Override
    public NavigationItemParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_list_parent, parentViewGroup, false);
        return new NavigationItemParentViewHolder(view, mContext);
    }

    @Override
    public NavigationItemChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View view = mLayoutInflater.inflate(R.layout.item_list_child, childViewGroup, false);
        return new NavigationItemChildViewHolder(view, mContext);
    }

    @Override
    public void onBindParentViewHolder(NavigationItemParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        NavigationItemParentModel head = (NavigationItemParentModel) parentListItem;
        parentViewHolder.bind(head);

    }

    @Override
    public void onBindChildViewHolder(NavigationItemChildViewHolder childViewHolder, int position, Object childListItem) {
        NavigationItem child = (NavigationItem) childListItem;
        childViewHolder.bind(child);
    }
}
