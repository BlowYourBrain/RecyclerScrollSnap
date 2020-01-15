package com.simple.scroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val cellCount = 15
    var customBehaviour: CustomBehaviour? = null

    private var moreSpace = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        customBehaviour =
            ((rvContainer.layoutParams as CoordinatorLayout.LayoutParams).behavior as CustomBehaviour)

        fab.setOnClickListener {
            if (moreSpace) {
                toast("remove some space ")
                customBehaviour?.allowFreeSpaceAtTop = false
                moreSpace = false
            } else {
                toast("add some space")
                customBehaviour?.allowFreeSpaceAtTop = true
                moreSpace = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        customBehaviour?.allowFreeSpaceAtTop = moreSpace
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView() {
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = RecyclerAdapter(cellCount)
            setHasFixedSize(true)
        }
    }
}
