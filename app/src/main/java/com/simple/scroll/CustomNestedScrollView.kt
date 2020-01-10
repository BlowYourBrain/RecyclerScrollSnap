package com.simple.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

class CustomNestedScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr), CollapsedEventProvider {

    override var isCollapsed: Boolean = true
        get() = field
        set(value) {
            field = value
        }

    override fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
    }
}