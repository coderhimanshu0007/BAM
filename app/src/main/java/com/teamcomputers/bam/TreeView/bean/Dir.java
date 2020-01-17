package com.teamcomputers.bam.TreeView.bean;


import com.teamcomputers.bam.R;
import com.teamcomputers.bam.recyclertreeview_lib.LayoutItemType;

/**
 * Created by Sarvesh on 2020/01/14 :)
 */

public class Dir implements LayoutItemType {
    public String dirName, dirYtd, dirMtd, dirSO;

    public Dir(String dirName, String dirYtd, String dirMtd, String dirSO) {
        this.dirName = dirName;
        this.dirYtd = dirYtd;
        this.dirMtd = dirMtd;
        this.dirSO = dirSO;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
