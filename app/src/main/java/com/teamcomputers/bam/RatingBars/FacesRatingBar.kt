package com.teamcomputers.bam.RatingBars

import android.content.Context
import android.util.AttributeSet
import com.inlacou.library.calendar.irisratingbar.IrisRatingBar
import com.inlacou.library.calendar.irisratingbar.IrisRatingBarMdl
import com.teamcomputers.bam.R

class FacesRatingBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : IrisRatingBar(context, IrisRatingBarMdl(value = 0, maxIcon = 5, minValue = 0, editable = true, singleSelection = true,
        icons = listOf(
                IrisRatingBarMdl.RatingBarItem(R.drawable.face_horrible, R.drawable.face_horrible_grey, "Horrible"),
                IrisRatingBarMdl.RatingBarItem(R.drawable.face_bad, R.drawable.face_bad_grey, "Bad"),
                IrisRatingBarMdl.RatingBarItem(R.drawable.face_just_ok, R.drawable.face_just_ok_grey, "Just Ok"),
                IrisRatingBarMdl.RatingBarItem(R.drawable.face_good, R.drawable.face_good_grey, "Good"),
                IrisRatingBarMdl.RatingBarItem(R.drawable.face_super, R.drawable.face_super_grey, "Super!")
        )), attrs, defStyleAttr)