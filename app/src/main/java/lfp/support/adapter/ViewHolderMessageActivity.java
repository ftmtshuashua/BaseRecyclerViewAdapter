package lfp.support.adapter;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import support.lfp.adapter.BaseLoonRecyclerViewAdapter;
import support.lfp.adapter.BaseLoonViewHolder;

import java.util.List;

/**
 * <pre>
 * Tip:
 *
 * Function:
 *
 * Created by LiFuPing on 2019/2/13 10:31
 * </pre>
 */
public class ViewHolderMessageActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListAdapter mAdapter = new ListAdapter();
        new RecyclerView(this).setAdapter(mAdapter);
        mAdapter.setOnAdapterDataChangeListener(adapter -> {
            /* 数据变化监听 */
            final List<Model> data = adapter.getData();//当数据发生变化的时候拿到数据源
        });
        mAdapter.setOnItemClickListener((adapter, viewHolder, view, position) -> {
            /*点击监听*/
        });
        mAdapter.setOnItemLongClickListener((adapter, viewHolder, view, position) -> {
            /*长按监听*/
            return true;
        });
        mAdapter.addViewHolderMessageHandler((what, obj, layoutIndex) -> {
            /*ViewHolder消息处理器*/
            if (what == 666) {
                Model data = obj;
            }
        });


        mAdapter.add(new Model("数据"));
    }

    /*自定义Adapter*/
    static final class ListAdapter extends BaseLoonRecyclerViewAdapter<Model, BaseLoonViewHolder> {
        public ListAdapter() {
            super(R.layout.activity_main);//这里是ViewHolder加载的布局文件
        }

        @Override
        public void convert(BaseLoonViewHolder<Model> holper, Model data) {
            sendMessage(666, data, holper.getDataPosition());//在Adapter或ViewHolder中广播一条消息
            notifyDataChange();//在Adapter或ViewHolder中显示通知数据变化，它会回调数据变化监听器
        }
    }

    /*数据模型*/
    static final class Model {
        String name;

        public Model(String name) {
            this.name = name;
        }
    }
}
