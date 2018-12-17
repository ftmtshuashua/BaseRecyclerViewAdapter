package lfp.support.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import support.lfp.adapter.BaseLoonRecyclerViewAdapter;
import support.lfp.adapter.BaseLoonViewHolder;
import support.lfp.adapter.BaseRecyclerViewAdapter;
import support.lfp.adapter.BaseViewHolder;
import support.lfp.adapter.interior.AdapterObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2018/12/10 11:24
 * </pre>
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BaseRecyclerViewAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClick();

        RecyclerView recyclerView = findViewById(R.id.view_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
//        mAdapter.disableItemAnimation(); //禁用动画效果


//        recyclerView.setAdapter(new SimpleRecyclerViewAdapter(Class<? extends BaseViewHolder<D>>,layoutResId)); //懒人方式加载
        recyclerView.setAdapter(mAdapter = new MyAdapter());


        mAdapter.setOnItemClickListener(mOnItemClick);
        mAdapter.setOnItemLongClickListener(mOnItemLongClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_Clear:
//                mAdapter.removeSearchAll();
                mAdapter.removeAll();
                break;
            case R.id.view_ClearSearch:
//                mAdapter.removeSearchAll();
                break;
            case R.id.view_Set5:
                mAdapter.set(generateString("Set5:", 5));
                break;
            case R.id.view_S_Set5:
//                mAdapter.setSearch(generateString("搜索_Set5:", 5));
                break;
            case R.id.view_Set10:
                mAdapter.set(generateString("Set10:", 10));
                break;
            case R.id.view_S_Set10:
//                mAdapter.setSearch(generateString("搜索_Set10:", 10));
                break;
            case R.id.view_Add1:
                mAdapter.add(generateString("Add1:"));
                break;
            case R.id.view_S_Add1:
//                mAdapter.addSearch(generateString("搜索_Add1:"));
                break;
            case R.id.view_Add2:
                mAdapter.add(generateString("Add2:", 2));
                break;
            case R.id.view_S_Add2:
//                mAdapter.addSearch(generateString("搜索_Add2:", 2));
                break;
            case R.id.view_Insert1_1:
                if (mAdapter.getDataCount() == 0) {
                    Toast("请保证适配中至少有1条数据 !");
                } else mAdapter.insert(1, generateString("Insert1_1:"));
                break;
            case R.id.view_S_Insert1_1:
//                if (mAdapter.getSearchCount() == 0) {
//                    Toast("请保证适配中至少有1条数据 !");
//                } else mAdapter.insertSearch(1, generateString("搜索_Insert1_1:"));
                break;
            case R.id.view_Insert1_2:
                if (mAdapter.getDataCount() == 0) {
                    Toast("请保证适配中至少有1条数据 !");
                } else mAdapter.insert(1, generateString("Insert1_2:", 2));
                break;
            case R.id.view_S_Insert1_2:
//                if (mAdapter.getSearchCount() == 0) {
//                    Toast("请保证适配中至少有1条数据 !");
//                } else mAdapter.insertSearch(1, generateString("搜索_Insert1_2:", 2));
                break;
            case R.id.view_Remove0:
                if (mAdapter.getDataCount() == 0) {
                    Toast("适配器中已经无数据了 !");
                } else mAdapter.remove(0);
                break;
            case R.id.view_S_Remove0:
//                if (mAdapter.getSearchCount() == 0) {
//                    Toast("适配器中已经无Search数据了 !");
//                } else mAdapter.removeSearch(0);
                break;
            case R.id.view_Move1_3:
                if (mAdapter.getDataCount() < 4) {
                    Toast("数据不够无法将 （1 -> 3） !");
                } else mAdapter.move(1, 3);
                break;
            case R.id.view_S_Move1_3:
//                if (mAdapter.getSearchCount() < 4) {
//                    Toast("数据不够无法将 （1 -> 3） !");
//                } else mAdapter.moveSearch(1, 3);
                break;
        }
    }

    AdapterObservable.OnItemClickListener mOnItemClick = new AdapterObservable.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterObservable adapter, BaseViewHolder viewHolder, View view, int position) {
            Toast("点击 ：" + position);
        }
    };
    AdapterObservable.OnItemLongClickListener mOnItemLongClick = new AdapterObservable.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterObservable adapter, BaseViewHolder viewHolder, View view, int position) {
            Toast("长按 ：" + position);
            return true;
        }
    };

    //懒人版Adapter
    private static final class MyAdapter extends BaseLoonRecyclerViewAdapter<String, BaseLoonViewHolder> {
        public MyAdapter() {
            super(R.layout.layout_textview);
        }

        @Override
        public void convert(BaseLoonViewHolder<String> helper, String data) {
            if (TextUtils.isEmpty(data)) {
                helper.setText(R.id.view_Info, "IS  NULL  ！！");
            } else helper.setText(R.id.view_Info, data);
        }
    }


    //    <editor-fold desc="测试方法">
    void initClick() {

        findViewById(R.id.view_Clear).setOnClickListener(this);
        findViewById(R.id.view_ClearSearch).setOnClickListener(this);
        findViewById(R.id.view_Set5).setOnClickListener(this);
        findViewById(R.id.view_S_Set5).setOnClickListener(this);
        findViewById(R.id.view_Set10).setOnClickListener(this);
        findViewById(R.id.view_S_Set10).setOnClickListener(this);
        findViewById(R.id.view_Add1).setOnClickListener(this);
        findViewById(R.id.view_S_Add1).setOnClickListener(this);
        findViewById(R.id.view_Add2).setOnClickListener(this);
        findViewById(R.id.view_S_Add2).setOnClickListener(this);
        findViewById(R.id.view_Insert1_1).setOnClickListener(this);
        findViewById(R.id.view_S_Insert1_1).setOnClickListener(this);
        findViewById(R.id.view_Insert1_2).setOnClickListener(this);
        findViewById(R.id.view_S_Insert1_2).setOnClickListener(this);
        findViewById(R.id.view_Remove0).setOnClickListener(this);
        findViewById(R.id.view_S_Remove0).setOnClickListener(this);
        findViewById(R.id.view_Move1_3).setOnClickListener(this);
        findViewById(R.id.view_S_Move1_3).setOnClickListener(this);

        //暂时未实现搜索模式
        findViewById(R.id.view_ClearSearch).setVisibility(View.GONE);
        findViewById(R.id.view_S_Set5).setVisibility(View.GONE);
        findViewById(R.id.view_S_Set10).setVisibility(View.GONE);
        findViewById(R.id.view_S_Add1).setVisibility(View.GONE);
        findViewById(R.id.view_S_Add2).setVisibility(View.GONE);
        findViewById(R.id.view_S_Insert1_1).setVisibility(View.GONE);
        findViewById(R.id.view_S_Insert1_2).setVisibility(View.GONE);
        findViewById(R.id.view_S_Remove0).setVisibility(View.GONE);
        findViewById(R.id.view_S_Move1_3).setVisibility(View.GONE);
    }

    /*生成数据*/
    static String generateString(String tag) {
        return generateString(tag, 1).get(0);
    }

    static List<String> generateString(String tag, int count) {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            array.add(tag + i);
        }
        return array;
    }

    void Toast(String msg) {
        Log.e("Tag", msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
//    </editor-fold>

}
