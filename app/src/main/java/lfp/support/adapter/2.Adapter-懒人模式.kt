package lfp.support.adapter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.BaseViewHolder
import com.acap.adapter.SimpleRecyclerViewAdapter
import com.acap.adapter.loon.BaseLoonRecyclerViewAdapter
import com.acap.adapter.loon.BaseLoonViewHolder
import lfp.support.adapter.utils.DefaultItemDecoration


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
        recyclerView.addItemDecoration(DefaultItemDecoration(this))
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
    private val mAdapter by lazy { SimpleRecyclerViewAdapter(MyViewHolder::class.java, R.layout.layout_textview) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DefaultItemDecoration(this))
        recyclerView.adapter = mAdapter

        mAdapter.add("0")
        mAdapter.add("1")
        mAdapter.add("2")
        mAdapter.add("3")
        mAdapter.add("4")
        mAdapter.add("5")

    }


    class MyViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun onUpdateUI(data: String?) {
            setText(R.id.view_Info, data)
        }
    }

}
