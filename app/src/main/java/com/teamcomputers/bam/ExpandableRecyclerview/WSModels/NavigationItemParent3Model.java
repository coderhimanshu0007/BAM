package com.teamcomputers.bam.ExpandableRecyclerview.WSModels;


import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.model.ParentListItem;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItem;

import java.util.List;


public class NavigationItemParent3Model implements ParentListItem {

    private int navImageParent;
    private String navTitleParent;
    private List<NavigationItem> navigationItems;

    public NavigationItemParent3Model(int navImageParent, String navTitleParent, List<NavigationItem> navigationItems) {
        this.navImageParent = navImageParent;
        this.navTitleParent = navTitleParent;
        this.navigationItems = navigationItems;
    }

    public NavigationItemParent3Model() {
    }

    public int getNavImageParent() {
        return navImageParent;
    }

    public void setNavImageParent(int navImageParent) {
        this.navImageParent = navImageParent;
    }

    public String getNavTitleParent() {
        return navTitleParent;
    }

    public void setNavTitleParent(String navTitleParent) {
        this.navTitleParent = navTitleParent;
    }

    public List<NavigationItem> getNavigationItems() {
        return navigationItems;
    }

    public void setNavigationItems(List<NavigationItem> navigationItems) {
        this.navigationItems = navigationItems;
    }

    @Override
    public List<?> getChildItemList() {
        return navigationItems;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
