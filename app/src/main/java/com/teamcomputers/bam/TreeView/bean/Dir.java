package com.teamcomputers.bam.TreeView.bean;


import com.teamcomputers.bam.R;
import com.teamcomputers.bam.recyclertreeview_lib.LayoutItemType;

/**
 * Created by Sarvesh on 2020/01/14 :)
 */

public class Dir implements LayoutItemType {
    public String dirName, dirYtd, dirMtd;

    public Dir(String dirName, String dirYtd, String dirMtd) {
        this.dirName = dirName;
        this.dirYtd = dirYtd;
        this.dirMtd = dirMtd;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
