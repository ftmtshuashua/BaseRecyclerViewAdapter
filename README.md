BaseRecyclerViewAdapter是一个RecyclerView的万能适配器，它的内部维护一个数据集合，当数据发生变化的时候自动反映到UI上。

>ItemView点击长按事件监听

>Adapter数据变化监听

>ViewHolder消息

>懒人模式

>AndroidX


## BaseRecyclerViewAdapter
内部维护多个数据集合，它会根据数据的变化自动更新UI
>set()
>add()
>insert()
>move()
>replace()
>**Only() Only方法只改变数据源，需要手动刷新UI

## ItemView点击和长按监听
不需要ViewHolder做任何适配，完美实现点击事件监听
>Adapter.setOnItemClickListener()
>Adapter.setOnItemLongClickListener()

1.在RecyclerView中没有类似ListView的点击长按事件监听，需要我们在ViewHolder中自己处理点击事件，使用起来比较麻烦。而且直接给ViewHolder的itemView设置点击事件会出现一些问题。
2.我的处理方法是在itemView创建的时候给他设置点击监听器，然后在它外面包裹一层FragmentLayout用来当作真正的itemView，原来的itemView就变成了它的内容。
3.不需要对ViewHolder做任何改动完美实现点击事件监听

## 数据变化监听
对Adapter中数据变化做出响应
>Adapter.setOnAdapterDataChangeListener()

1.显示调用改变数据的方法 Set() |  Add()  |  Insert()  || Move() || replace() 会自动回调该方法
2.其他情况通过手动调用Adapter或Viewholder中的 notifyDataChange() 方法来通知数据变化

## ViewHolder消息

>Adapter.addViewHolderMessageHandler()

1.在Adapter与Viewholder中调用sendMessage()来发送一条消息到Adapter的消息监听器中


## 懒人模式 BaseLoonRecyclerViewAdapter 与 SimpleRecyclerViewAdapter
BaseLoonRecyclerViewAdapter允许不创建ViewHolder,只需要创建Adapter
```
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
```

SimpleRecyclerViewAdapter允许不自定义Adapter。
使用方式:
```
//Class<? extends BaseViewHolder<D>>   :设置被Adapter加载的ViewHolder
//layoutResId  :Viewholder使用的布局
recyclerView.setAdapter(new SimpleRecyclerViewAdapter(Class<? extends BaseViewHolder<D>>,layoutResId));
```



## 配置依赖

在项目的build.gradle中添加
```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```
在Model的build.gradle中添加 [![](https://jitpack.io/v/ftmtshuashua/BaseRecyclerViewAdapter.svg)](https://jitpack.io/#ftmtshuashua/BaseRecyclerViewAdapter)
```
dependencies {
    implementation 'com.github.ftmtshuashua:BaseRecyclerViewAdapter:version'
}
```
需要库
```
implementation 'androidx.recyclerview:recyclerview:version'
```

## 混淆配置

```
-keepclassmembers class * extends support.lfp.adapter.BaseViewHolder{
    public <init>(android.view.View);
}
```


##一个例子
```
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
```


## LICENSE

```
Copyright (c) 2018-present, BaseRecyclerViewAdapter Contributors.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
