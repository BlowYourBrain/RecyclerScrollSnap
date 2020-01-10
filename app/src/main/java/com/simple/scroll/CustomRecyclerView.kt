package com.simple.scroll

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), CollapsedEventProvider {

    val scrollOffset = context.resources.getDimensionPixelSize(R.dimen.cell_height)

    override var isCollapsed: Boolean = false
        get() = field
        set(value) {
            field = value
            Log.d("fuck", "collapsed = $value")
            if (value) {
                scrollIfNeeded(value)
            }
        }

    override fun onScrollStateChanged(state: Int) {
        Log.d("fuck", "scroll state changed!")
        scrollIfNeeded(isCollapsed)
    }

    private fun scrollIfNeeded(collapsed: Boolean) {
        if (scrollState != SCROLL_STATE_IDLE || !collapsed) {
            return
        }

        layoutManager?.let {
            (it as LinearLayoutManager).run {
                //                val offset = this.computeVerticalScrollExtent(State())
                val offset = computeVerticalScrollOffset()
                val diff = scrollOffset - offset

                if (diff > 0) {
                    smoothScrollBy(0, diff)
                }
            }
        }
    }
}