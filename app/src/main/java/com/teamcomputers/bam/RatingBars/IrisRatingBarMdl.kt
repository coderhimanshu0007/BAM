package com.inlacou.library.calendar.irisratingbar

import org.w3c.dom.Text

/**
 * Created by Sarvesh on 2020/01/09 :)
 */
class IrisRatingBarMdl(
		var icons: List<RatingBarItem>,
		var maxIcon: Int = 5,
		var minIcon: Int = 0,
		var minValue: Int = minIcon,
		var value: Int = minValue,
		var editable: Boolean = true,
		var singleSelection: Boolean = false
){
	val iconNumber
		get() = maxIcon-minIcon

	data class RatingBarItem(
			val activeResId: Int,
			val inActiveResId: Int,
			val resourceName: String)
}