package com.simple.scroll

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

import kotlin.math.abs


class CustomBehaviour @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    val scroll = context.resources.getDimensionPixelSize(R.dimen.cell_height)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency).also { depends ->
            if (depends) {
                (dependency as AppBarLayout).addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbarLayout, verticalOffset ->
                    (child as CollapsedEventProvider).isCollapsed =
                        abs(verticalOffset) - appbarLayout.totalScrollRange == 0
                })
            }
        }
    }

//    override fun onDependentViewChanged(
//        parent: CoordinatorLayout,
//        child: View,
//        dependency: View
//    ): Boolean {
//        Log.d("fuck", "onDependentViewChanged")
//        if (collapsed) {
//            (child as NestedScrollView).smoothScrollBy(0, scroll)
//        }
//
//        return super.onDependentViewChanged(parent, child, dependency)
//    }

}