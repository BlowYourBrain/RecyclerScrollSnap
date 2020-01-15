package com.simple.scroll

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


class CustomBehaviour @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    companion object {
        private const val ANIMATION_DURATION: Long = 150
        private const val VALUE_FROM_INDEX = 0
        private const val VALUE_TO_INDEX = 1
    }

    var allowFreeSpaceAtTop: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                updateShortcutsState(value)
            }
        }

    /**total scroll distance allowed for consume*/
    private val scrollDistance = context.resources.getDimensionPixelSize(R.dimen.cell_height)
    /**value between 0 and [scrollDistance]*/
    private var consumedScroll = 0
    private var appbarLayout: View? = null
    private var childViewPadding = Rect()

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return super.layoutDependsOn(parent, child, dependency).also { depends ->
            if (depends) {
                appbarLayout = child
                childViewPadding.left = child.paddingLeft
                childViewPadding.right = child.paddingRight
                childViewPadding.bottom = child.paddingBottom
            }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(
                    coordinatorLayout,
                    child,
                    directTargetChild,
                    target,
                    axes,
                    type
                )) && allowFreeSpaceAtTop
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val canConsume = if (dy > 0) {
            consumedScroll > 0
        } else {
            scrollDistance - consumedScroll > 0
        }

        if (!canConsume) {
            return
        }

        val consumedDx = 0
        val consumedDy: Int = when {
            dy > 0 -> {
                //scroll down
                val maxConsumeAmount = consumedScroll

                if (maxConsumeAmount == 0) {
                    //free space fully expanded, no need to consume scroll
                    return
                }

                if (dy > maxConsumeAmount) {
                    maxConsumeAmount
                } else {
                    dy
                }.also {
                    consumedScroll -= it
                }
            }
            dy < 0 -> {
                //scroll up
                val maxConsumeAmount = scrollDistance - consumedScroll

                if (maxConsumeAmount == 0) {
                    //free space fully collapsed, no need to consume scroll
                    return
                }

                if (abs(dy) > maxConsumeAmount) {
                    -maxConsumeAmount
                } else {
                    dy
                }.also {
                    consumedScroll += -it
                }
            }

            else -> {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
                return
            }
        }

        consumed[0] = consumedDx
        consumed[1] = consumedDy

        appbarLayout?.updatePadding(consumedScroll)

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    private fun updateShortcutsState(hasSpace: Boolean) {
        appbarLayout?.run {
            val pair = if (hasSpace) {
                consumedScroll = scrollDistance
                arrayOf(0, scrollDistance)
            } else {
                consumedScroll = 0
                arrayOf(scrollDistance, 0)
            }

            val valueTo = pair[VALUE_TO_INDEX]
            val valueFrom = if (pair[VALUE_FROM_INDEX] == consumedScroll) {
                pair[VALUE_FROM_INDEX]
            } else {
                consumedScroll
            }

            ValueAnimator.ofInt(valueFrom, valueTo).apply {
                removeAllUpdateListeners()
                addUpdateListener {
                    val updateValue = it.animatedValue as Int
                    updatePadding(updateValue)
                }
                duration = ANIMATION_DURATION
            }.start()
        }
    }

    private fun View.updatePadding(topPadding: Int) {
        setPadding(
            childViewPadding.left,
            topPadding,
            childViewPadding.right,
            childViewPadding.bottom
        )
    }

}