package com.teamcomputers.bam.ExpandableRecyclerview.models;

import android.text.SpannableStringBuilder;

public class NavigationItem {

    private int navigationImage;
    private SpannableStringBuilder navigationTitle;
    private int fragment;

    public NavigationItem() {
    }

    public NavigationItem(SpannableStringBuilder navigationTitle, int fragment) {
        this.navigationTitle = navigationTitle;
        this.fragment = fragment;
    }

    public NavigationItem(int navigationImage, SpannableStringBuilder navigationTitle, int fragment) {
        this.navigationImage = navigationImage;
        this.navigationTitle = navigationTitle;
        this.fragment = fragment;
    }

    public int getNavigationImage() {
        return navigationImage;
    }

    public void setNavigationImage(int navigationImage) {
        this.navigationImage = navigationImage;
    }

    public SpannableStringBuilder getNavigationTitle() {
        return navigationTitle;
    }

    public void setNavigationTitle(SpannableStringBuilder navigationTitle) {
        this.navigationTitle = navigationTitle;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
