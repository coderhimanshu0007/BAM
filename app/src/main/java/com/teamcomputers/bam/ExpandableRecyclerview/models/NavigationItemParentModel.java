package com.teamcomputers.bam.ExpandableRecyclerview.models;


import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.model.ParentListItem;

import java.util.List;


public class NavigationItemParentModel implements ParentListItem {

    private int navImageParent;
    private String navTitleParent;
    private List<NavigationItem> navigationItems;

    public NavigationItemParentModel(int navImageParent, String navTitleParent, List<NavigationItem> navigationItems) {
        this.navImageParent = navImageParent;
        this.navTitleParent = navTitleParent;
        this.navigationItems = navigationItems;
    }

    public NavigationItemParentModel() {
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
