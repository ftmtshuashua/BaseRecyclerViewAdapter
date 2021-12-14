package lfp.support.adapter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
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
 * @date 2021/12/8 17:49
 * </pre>
 */
class ViewHolderMessageRecyclerViewActivity : AppCompatActivity() {
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DefaultItemDecoration(this))
        recyclerView.adapter = mAdapter


        mAdapter.addViewHolderMessageHandler { what, obj, layoutIndex ->
            toast("收到消息:${what},${obj},${layoutIndex}")
        }

        mAdapter.add(MessageTextViewModel("0"))

    }


    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

private class MessageTextViewModel(val mMsg: String) : MultipleViewModel(R.layout.layout_textview) {


    override fun onUpdate(holder: BaseViewHolder<*>) {
        holder.setText(R.id.view_Info, mMsg)

        //在 ViewHolder 中可以发送消息给外部
        holder.sendMessage(111, "来自ViewHodler的消息")
    }


}