package com.teamcomputers.bam.TreeView.bean;


import com.teamcomputers.bam.R;
import com.teamcomputers.bam.recyclertreeview_lib.LayoutItemType;

/**
 * Created by Sarvesh on 2020/01/14 :)
 */

public class File implements LayoutItemType {
    public String fileName;

    public File(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_file;
    }
}
