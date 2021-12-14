package lfp.support.adapter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.BaseViewHolder
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import com.acap.adapter.multiple.MultipleViewModel
import lfp.support.adapter.utils.DefaultItemDecoration


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/13 17:05
 * </pre>
 */


class DiffRecyclerViewActivity : AppCompatActivity() {
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diff)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DefaultItemDecoration(this))
        recyclerView.adapter = mAdapter

        mAdapter.setDiffEnable(true)    //启动差分算法
//        mAdapter.setDiffAsync(true)     //异步执行差分算法

        for (i in 0..20) {
            mAdapter.add(DiffViewModel("Diff - $i"))
        }

        findViewById<View>(R.id.view_Submit).setOnClickListener(::initClick)
    }

    var count = 0
    fun initClick(v: View) {
        count++
        val array = mAdapter.data.toMutableList()
        for (i in 0..5) {
            val size = array.size
            val type = random(0, 3)
            val index = random(0, array.size - 1)
            val to = random(0, array.size - 1)

            when (type) {
                0 -> {
                    Log.i("XXXX", "添加:$index")
                    array.add(index, DiffViewModel("第 $count 次 -> 添加:$index"))
                }
                1 -> {
                    if (size > 0) {
                        Log.i("XXXX", "删除:$index")
                        array.removeAt(index)
                    }
                }
                2 -> {
                    if (size > 0 && index != to) {
                        Log.i("XXXX", "移动:$index -> $to")
                        val removeAt = array.removeAt(index)
                        array.add(to, removeAt)
                    }
                }
                3 -> {
                    if (size > 0) {
                        Log.i("XXXX", "替换:$index")
                        array.removeAt(index)
                        array.add(index, DiffViewModel("第 $count 次 -> 替换:$index"))
                    }
                }
            }
        }
        mAdapter.set(array)
    }

    fun random(start: Int, end: Int): Int {
        val i = end - start
        val d = (Math.random() * i).toInt()
        return d + start
    }


    class DiffViewModel(val msg: String) : MultipleViewModel(R.layout.layout_slide_menu) {
        override fun onUpdate(holder: BaseViewHolder<*>) {
            holder.setText(R.id.view_Info, msg)
        }
    }

}
