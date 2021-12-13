package lfp.support.adapter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.BaseViewHolder
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import com.acap.adapter.multiple.MultipleViewModel
import com.acap.adapter.slide.SlideMenu


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

        for (i in 0..100) {
            mAdapter.add(TextViewModel("菜单 $i"))
        }

        mAdapter.addSlideMenu(MyLefSlideMenu1())
        mAdapter.addSlideMenu(MyRightSlideMenu())

    }


    // 左菜单
    inner class MyLefSlideMenu1 : SlideMenu(0, Place.LEFT, R.layout.menu_left_1) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            vh.notifyDataChange()
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    // 左菜单
    inner class MyLefSlideMenu2 : SlideMenu(1, Place.LEFT, R.layout.menu_left_2) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    // 右菜单
    inner class MyRightSlideMenu : SlideMenu(2, Place.RIGHT, R.layout.menu_right_1) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    class TextViewModel(val mMsg: String) : MultipleViewModel(R.layout.layout_textview) {
//        private var mMenuIds: Array<Int>? = null
        override fun onUpdate(holder: BaseViewHolder<*>) {
//            holder.setSlideMenuIds(null)
            holder.setText(R.id.view_Info, mMsg)
        }

    }


}
