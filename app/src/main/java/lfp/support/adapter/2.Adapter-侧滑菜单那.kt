package lfp.support.adapter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import com.acap.adapter.slide.SlideMenu
import lfp.support.adapter.item.TextViewModel


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:49
 * </pre>
 */
class SlideRecyclerViewActivity : AppCompatActivity() {
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


        mAdapter.addSlideMenu(MySlideMenu)

//        mAdapter.
    }


    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    class MySlideMenu : SlideMenu(R.layout.layout_button) {
        override fun onViewBind(menu: View?, position: Int) {

        }
    }
}
