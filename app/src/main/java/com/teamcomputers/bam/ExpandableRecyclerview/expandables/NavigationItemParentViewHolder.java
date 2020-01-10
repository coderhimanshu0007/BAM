package com.teamcomputers.bam.ExpandableRecyclerview.expandables;

import android.content.Context;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.teamcomputers.bam.CustomView.TextViewCustom;
import com.teamcomputers.bam.ExpandableRecyclerview.libmoduleExpandable.viewholder.ParentViewHolder;
import com.teamcomputers.bam.ExpandableRecyclerview.models.NavigationItemParentModel;
import com.teamcomputers.bam.R;


public class NavigationItemParentViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;


    private ImageView imageViewParent;
    private TextViewCustom textViewParent;
    private ImageView ivCollapse;
    private LinearLayout llItem;
    private Context context;


    public NavigationItemParentViewHolder(View itemView, Context context) {
        super(itemView);

        imageViewParent = (ImageView) itemView.findViewById(R.id.iv_parent_image);
        textViewParent = (TextViewCustom) itemView.findViewById(R.id.tv_parent_title);
        ivCollapse = (ImageView) itemView.findViewById(R.id.image_view_collapse);
        llItem = (LinearLayout) itemView.findViewById(R.id.ll_item);
        this.context = context;
    }

    public void bind(NavigationItemParentModel navigationItemParentModel) {

        imageViewParent.setImageResource(navigationItemParentModel.getNavImageParent());

        textViewParent.setText(navigationItemParentModel.getNavTitleParent());

        int position = getAdapterPosition();

//        if (position % 2 == 0) {
//            llItem.setBackgroundColor(Color.parseColor("#ffffff"));
//        } else {
//            llItem.setBackgroundColor(Color.parseColor("#f1f1f1"));
//        }

    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded) {
            ivCollapse.setImageResource(R.drawable.ic_chevron_up_blue);
        } else {
            ivCollapse.setImageResource(R.drawable.ic_chevron_down_blue);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        RotateAnimation rotateAnimation;
        if (expanded) { // rotate clockwise
            rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else { // rotate counterclockwise
            rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        ivCollapse.startAnimation(rotateAnimation);
    }
}
