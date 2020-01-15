package com.inlacou.library.calendar.irisratingbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.teamcomputers.bam.R

/**
 * Created by Sarvesh on 2020/01/14 :)
 */
open class IrisRatingBar @JvmOverloads constructor(context: Context, model: IrisRatingBarMdl? = null, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var valueSetListener: ((value: Int) -> Unit)? = null
    var valueChangeListener: ((value: Int) -> Unit)? = null

    private var surfaceLayout: LinearLayout? = null
    //private var viewLayout: LinearLayout? = null
    internal val imageViews = mutableListOf<ImageView>()
    internal val textViews = mutableListOf<TextView>()

    /**
     * Change full model: no problem
     * Change only some fields on the model: probably problems. There are wrappers on this class for this use case and no problems.
     */
    var model: IrisRatingBarMdl = IrisRatingBarMdl(value = 5, editable = true, singleSelection = false,
            icons = listOf(
                    IrisRatingBarMdl.RatingBarItem(R.drawable.iris_star_filled, R.drawable.iris_star_empty, "None")
            ))
        set(value) {
            field = value
            populate()
        }
    //I went the extra mile and added the way to change only individual fields of the model
    var maxIcon: Int
        get() = model.maxIcon
        set(value) {
            if (model.maxIcon != value) {
                model.maxIcon = value
                populate()
            }
        }
    var minIcon: Int
        get() = model.minIcon
        set(value) {
            if (model.minIcon != value) {
                model.minIcon = value
                populate()
            }
        }
    /**
     * Yes, you can set value (manually) to below min.
     * If the user input must be from 1 to 5, but you want to start at 0 as "empty field", for example.
     */
    var value: Int
        get() = model.value
        set(value) {
            if (model.value != value) {
                model.value = value
                controller.update()
            }
        }
    var singleSelection: Boolean
        get() = model.singleSelection
        set(value) {
            if (model.singleSelection != value) {
                model.singleSelection = value
                controller.update()
            }
        }
    var icons: List<IrisRatingBarMdl.RatingBarItem>
        get() = model.icons
        set(value) {
            if (model.icons != value) {
                model.icons = value
                controller.update()
            }
        }
    var editable: Boolean
        get() = model.editable
        set(value) {
            if (model.editable != value) {
                model.editable = value
                updateListeners()
            }
        }
    lateinit var controller: IrisRatingBarCtrl

    init {
        View.inflate(context, R.layout.view_custom_rating_bar, this)
        surfaceLayout = findViewById(R.id.view_base_layout_surface)
        //View.inflate(context, R.layout.view_custom_item_bar, this)
        //viewLayout = findViewById(R.id.view_base_layout)
        attrs?.let { readAttrs(it) }
        model.let { if (it != null) this.model = it else populate() }
    }

    private fun readAttrs(attrs: AttributeSet) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.IrisRatingBar, 0, 0)
        try {
            if (ta.hasValue(R.styleable.IrisRatingBar_editable)) {
                model.editable = ta.getBoolean(R.styleable.IrisRatingBar_editable, model.editable)
            }
            if (ta.hasValue(R.styleable.IrisRatingBar_singleSelection)) {
                model.singleSelection = ta.getBoolean(R.styleable.IrisRatingBar_singleSelection, model.singleSelection)
            }
            if (ta.hasValue(R.styleable.IrisRatingBar_value)) {
                model.value = ta.getInteger(R.styleable.IrisRatingBar_value, model.value)
            }
        } finally {
            ta.recycle()
        }
    }

    private fun populate() {
        controller = IrisRatingBarCtrl(this, this.model)
        //TODO make it compatible for model.imageViews.size!=max (I know max is not defined, but should once this is done, and be the maximum value, like minValue works)
        removeListeners()
        surfaceLayout?.removeAllViews()
        //viewLayout?.removeAllViews()
        imageViews.clear()
        textViews.clear()
        (0 until model.iconNumber).forEach { addIcon() }
        controller.update()
        setListeners()
    }

    private fun addIcon() {
        val newIcon = ImageView(context)
        val newText = TextView(context)
        imageViews.add(newIcon)
        textViews.add(newText)
        val parent = LinearLayout(context)
        val lptv1 = LinearLayout.LayoutParams(150,
                LinearLayout.LayoutParams.WRAP_CONTENT)
                .apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

        lptv1.setMargins(5, 10, 5, 0)
        lptv1.gravity = Gravity.CENTER_HORIZONTAL
        newText.layoutParams = lptv1
        newText.setGravity(Gravity.CENTER);

        val lptv2 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

        lptv2.setMargins(10, 0, 10, 0)
        lptv2.gravity = Gravity.CENTER_HORIZONTAL
        parent.layoutParams = lptv2
        /*parent.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)*/

        parent.orientation = LinearLayout.VERTICAL


        parent.removeAllViews()
        parent.addView(newIcon)
        parent.addView(newText)
        /*viewLayout?.addView(newIcon)
        viewLayout?.addView(newText)*/
        surfaceLayout?.addView(parent)
        //surfaceLayout?.addView(newText)
    }

    private fun updateListeners() {
        if (model.editable) {
            setListeners()
        } else {
            removeListeners()
        }
    }

    private fun removeListeners() {
        surfaceLayout?.setOnTouchListener(null)
    }

    private fun setListeners() {
        if (model.editable) {
            val touchListener = OnTouchListener { _, event ->
                val realWidth = surfaceLayout?.width ?: width
                when {
                    event.x <= 0 -> {
                        value = model.minValue
                    }
                    event.x >= realWidth -> {
                        value = model.iconNumber
                    }
                    else -> {
                        val relativePosition = event.x
                        val maxNumber = model.iconNumber + 1
                        val stepSize = realWidth / model.iconNumber
                        (model.minValue..maxNumber)
                                .filter { currentPosition ->
                                    when (currentPosition) {
                                        model.minValue -> relativePosition < (currentPosition) * stepSize
                                        maxNumber -> relativePosition >= currentPosition * stepSize
                                        else -> relativePosition >= (currentPosition - 1) * stepSize && relativePosition < (currentPosition) * stepSize
                                    }
                                }
                                .forEach { value = it }
                    }
                }
                when (event.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        attemptClaimDrag()
                        true
                    }
                    android.view.MotionEvent.ACTION_CANCEL -> false
                    android.view.MotionEvent.ACTION_UP -> {
                        controller.onActionUp()
                        false
                    }
                    android.view.MotionEvent.ACTION_MOVE -> true
                    else -> false
                }
            }
            surfaceLayout?.setOnTouchListener(touchListener)
        } else {
            surfaceLayout?.setOnTouchListener(null)
        }
    }

    override fun hasFocusable(): Boolean {
        return true
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any
     * ancestors from stealing events in the drag.
     */
    private fun attemptClaimDrag() {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }
}