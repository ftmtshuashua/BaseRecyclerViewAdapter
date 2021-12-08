package lfp.support.adapter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acap.adapter.multiple.MultipleRecyclerViewAdapter
import com.acap.adapter.multiple.MultipleViewModel
import lfp.support.adapter.item.ButtonViewModel
import lfp.support.adapter.item.TextViewModel


/**
 * <pre>
 * Tip:
 *
 * @author A·Cap
 * @date 2021/12/8 17:49
 * </pre>
 */
class BasisRecyclerViewActivity : AppCompatActivity() {
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }
    private val mMenuAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basis)

        val recyclerView = findViewById<RecyclerView>(R.id.view_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mAdapter

        initMenu()
    }

    /* 菜单配置 */
    fun initMenu() {
        val recyclerView = findViewById<RecyclerView>(R.id.view_MenuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = mMenuAdapter

        //生成item项
        val generate: (String, Int) -> List<TextViewModel> = { title, count ->
            val array = mutableListOf<TextViewModel>()
            for (i in 0 until count) {
                array.add(TextViewModel("$title$i"))
            }
            array
        }

        mMenuAdapter.add(ButtonViewModel("CLEAR") { mAdapter.removeAll() })
        mMenuAdapter.add(ButtonViewModel("SET(5)") { mAdapter.set(generate("Set5:", 5)) })
        mMenuAdapter.add(ButtonViewModel("SET(10)") { mAdapter.set(generate("Set10:", 10)) })
        mMenuAdapter.add(ButtonViewModel("ADD(1)") { mAdapter.add(generate("Add1:", 1)) })
        mMenuAdapter.add(ButtonViewModel("ADD(2)") { mAdapter.add(generate("Add2:", 2)) })
        mMenuAdapter.add(ButtonViewModel("INSERT(1,1)") {
            if (mAdapter.dataCount == 0) {
                toast("请保证适配中至少有1条数据 !")
            } else mAdapter.insert(1, generate("Insert1_1:", 1))
        })
        mMenuAdapter.add(ButtonViewModel("INSERT(1,2)") {
            if (mAdapter.dataCount == 0) {
                toast("请保证适配中至少有1条数据 !")
            } else mAdapter.insert(1, generate("Insert1_2:", 2))
        })
        mMenuAdapter.add(ButtonViewModel("REMOVE(0)") {
            if (mAdapter.dataCount == 0) {
                toast("适配器中已经无数据了 !")
            } else mAdapter.remove(0)
        })
        mMenuAdapter.add(ButtonViewModel("MOVE(1,3)") {
            if (mAdapter.dataCount < 4) {
                toast("数据不够无法将 （1 -> 3） !")
            } else mAdapter.move(1, 3)
        })
    }


    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
