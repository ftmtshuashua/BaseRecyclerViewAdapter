package lfp.support.adapter

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import com.acap.adapter.multiple.MultipleViewModel
import lfp.support.adapter.item.ButtonViewModel


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:44
 * </pre>
 */
class LaunchActivity : AppCompatActivity() {

    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        val mRecyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mRecyclerView.adapter = mAdapter

        mAdapter.add(ButtonViewModel("基础功能-增删查改") { startActivity(Intent(this, BasisRecyclerViewActivity::class.java)) })
        mAdapter.add(ButtonViewModel("基础功能-点击和长按监听") { startActivity(Intent(this, ClickRecyclerViewActivity::class.java)) })
        mAdapter.add(ButtonViewModel("基础功能-ViewHolder消息") { startActivity(Intent(this, ViewHolderMessageRecyclerViewActivity::class.java)) })
        mAdapter.add(ButtonViewModel("懒人模式-只要Adapter") { startActivity(Intent(this, OnlyAdapterRecyclerViewActivity::class.java)) })
        mAdapter.add(ButtonViewModel("懒人模式-只要ViewHolder") { startActivity(Intent(this, OnlyViewHolderRecyclerViewActivity::class.java)) })
        mAdapter.add(ButtonViewModel("高级功能-侧滑菜单") { startActivity(Intent(this, SlideRecyclerViewActivity::class.java)) })

    }
}

