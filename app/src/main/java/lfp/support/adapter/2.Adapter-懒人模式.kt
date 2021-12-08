package lfp.support.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.loon.BaseLoonRecyclerViewAdapter
import com.acap.adapter.loon.BaseLoonViewHolder
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import lfp.support.adapter.item.TextViewModel


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:49
 * </pre>
 */
class OnlyAdapterRecyclerViewActivity : AppCompatActivity() {
    private val mAdapter by lazy { MyAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter

        mAdapter.add("0")
        mAdapter.add("1")
        mAdapter.add("2")
        mAdapter.add("3")
        mAdapter.add("4")
        mAdapter.add("5")
    }


    class MyAdapter : BaseLoonRecyclerViewAdapter<String, BaseLoonViewHolder<String>>(R.layout.layout_textview) {
        override fun convert(holder: BaseLoonViewHolder<String>, data: String) {
            holder.setText(R.id.view_Info, data)
        }
    }

}


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:49
 * </pre>
 */
class OnlyViewHolderRecyclerViewActivity : AppCompatActivity() {
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<TextViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter

        mAdapter.add(TextViewModel("0"))
        mAdapter.add(TextViewModel("1"))
        mAdapter.add(TextViewModel("2"))
        mAdapter.add(TextViewModel("3"))
        mAdapter.add(TextViewModel("4"))
        mAdapter.add(TextViewModel("5"))

    }

}
