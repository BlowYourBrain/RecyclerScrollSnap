package com.simple.scroll

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val viewCount: Int) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val context = parent.context
        return MyViewHolder(createView(context))
    }

    override fun getItemCount(): Int = viewCount

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind("$position. Вот это вьюха!")
    }

    private fun createView(context: Context) = AppCompatTextView(context).apply {
        val height = resources.getDimensionPixelSize(R.dimen.cell_height)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
    }

    class MyViewHolder(val layout: AppCompatTextView) : RecyclerView.ViewHolder(layout) {

        fun bind(text: String) {
            layout.text = text
        }

    }

}