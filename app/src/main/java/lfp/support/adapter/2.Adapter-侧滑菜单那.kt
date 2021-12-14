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
import lfp.support.adapter.utils.DefaultItemDecoration


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:49
 * </pre>
 */
class SlideRecyclerViewActivity : AppCompatActivity() {
    private val MENU_LEFT_1 = 0
    private val MENU_LEFT_2 = 1
    private val MENU_RIGHT_1 = 2
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DefaultItemDecoration(this))
        recyclerView.adapter = mAdapter

        mAdapter.addSlideMenu(MyLefSlideMenu1())
        mAdapter.addSlideMenu(MyLefSlideMenu2())
        mAdapter.addSlideMenu(MyRightSlideMenu())

        for (i in 0..100) {
            when (i % 4) {
                0 -> mAdapter.add(MenuViewModel("$i - 菜单:LEFT1").setMenu(MENU_LEFT_1))
                1 -> mAdapter.add(MenuViewModel("$i - 菜单:LEFT2").setMenu(MENU_LEFT_2))
                2 -> mAdapter.add(MenuViewModel("$i - 菜单:LEFT+RIGHT").setMenu(MENU_LEFT_1, MENU_RIGHT_1))
                3 -> mAdapter.add(MenuViewModel("$i - 菜单:不使用菜单").setMenu())
            }

        }

    }


    // 左菜单
    inner class MyLefSlideMenu1 : SlideMenu(MENU_LEFT_1, Place.LEFT, R.layout.menu_left_1) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    // 左菜单
    inner class MyLefSlideMenu2 : SlideMenu(MENU_LEFT_2, Place.LEFT, R.layout.menu_left_2) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    // 右菜单
    inner class MyRightSlideMenu : SlideMenu(MENU_RIGHT_1, Place.RIGHT, R.layout.menu_right_1) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }

    class MenuViewModel(val mMsg: String) : MultipleViewModel(R.layout.layout_slide_menu) {
        private var mMenuIds: IntArray? = null
        override fun onUpdate(holder: BaseViewHolder<*>) {
            if (mMenuIds == null) {
                holder.setSlideMenuIds()   // 清空绑定的菜单列表后，默认应用所有已添加的菜单
            } else {
                holder.setSlideMenuIds(*mMenuIds!!)
            }

            holder.setText(R.id.view_Info, mMsg)
        }

        fun setMenu(vararg int: Int): MenuViewModel {
            mMenuIds = int
            return this
        }

    }


}
