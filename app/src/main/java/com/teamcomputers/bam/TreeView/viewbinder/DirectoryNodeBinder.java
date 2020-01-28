package com.teamcomputers.bam.TreeView.viewbinder;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamcomputers.bam.R;
import com.teamcomputers.bam.TreeView.bean.Dir;
import com.teamcomputers.bam.recyclertreeview_lib.TreeNode;
import com.teamcomputers.bam.recyclertreeview_lib.TreeViewBinder;


/**
 * Created by Sarvesh on 2020/01/14 :)
 */

public class DirectoryNodeBinder extends TreeViewBinder<DirectoryNodeBinder.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {
        holder.ivArrow.setRotation(0);
        holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp);
        int rotateDegree = node.isExpand() ? 90 : 0;
        holder.ivArrow.setRotation(rotateDegree);
        Dir dirNode = (Dir) node.getContent();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if (dirNode.pos.equals("1")) {
            holder.tvName.setEms(13);
        } else if (dirNode.pos.equals("2")) {
            holder.tvName.setEms(12);
            params.setMargins(40, 0, 0, 0);
            params.gravity = Gravity.CENTER_VERTICAL;
            holder.tvYtd.setLayoutParams(params);
        } else if (dirNode.pos.equals("3")) {
            holder.tvName.setEms(11);
            params.setMargins(50, 0, 0, 0);
            params.gravity = Gravity.CENTER_VERTICAL;
            holder.tvYtd.setLayoutParams(params);
        } else if (dirNode.pos.equals("4")) {
            holder.tvName.setEms(10);
            params.setMargins(65, 0, 0, 0);
            params.gravity = Gravity.CENTER_VERTICAL;
            holder.tvYtd.setLayoutParams(params);
        } else if (dirNode.pos.equals("5")) {
            holder.tvName.setEms(9);
            params.setMargins(80, 0, 0, 0);
            params.gravity = Gravity.CENTER_VERTICAL;
            holder.tvYtd.setLayoutParams(params);
        } else if (dirNode.pos.equals("6")) {
            holder.tvName.setEms(8);
            params.setMargins(95, 0, 0, 0);
            params.gravity = Gravity.CENTER_VERTICAL;
            holder.tvYtd.setLayoutParams(params);
        }
        holder.tvName.setText(dirNode.dirName);
        holder.tvMtd.setText(dirNode.dirMtd);
        holder.tvYtd.setText(dirNode.dirYtd);
        holder.tvSO.setText(dirNode.dirSO);
        if (node.isLeaf())
            holder.ivArrow.setVisibility(View.INVISIBLE);
        else holder.ivArrow.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {
        private LinearLayout llDir;
        private ImageView ivArrow;
        private TextView tvName, tvMtd, tvYtd, tvSO;

        public ViewHolder(View rootView) {
            super(rootView);
            this.llDir = (LinearLayout) rootView.findViewById(R.id.llDir);
            this.ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.tvMtd = (TextView) rootView.findViewById(R.id.tv_mtd);
            this.tvYtd = (TextView) rootView.findViewById(R.id.tv_ytd);
            this.tvSO = (TextView) rootView.findViewById(R.id.tv_so);
        }

        public LinearLayout getllDir() {
            return llDir;
        }

        public ImageView getIvArrow() {
            return ivArrow;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvMtd() {
            return tvMtd;
        }

        public TextView getTvYtd() {
            return tvYtd;
        }

        public TextView getTvSo() {
            return tvSO;
        }
    }
}
