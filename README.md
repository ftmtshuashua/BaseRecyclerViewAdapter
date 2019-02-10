# BaseRecyclerViewAdapter
>BaseRecyclerViewAdapter内部维护多个数据集合，当数据发生变化的时候会自动更行Adapter。

>Adapter数据变化监听器OnAdapterDataChangeListener

>ViewHolder消息

>已适配AndroidX

## BaseRecyclerViewAdapter
内部维护多个数据集合，它会根据数据的变化自动更新UI
>set()

>add()

>insert()

>move()

>replace()

>**Only() Only方法只改变数据源，需要手动刷新UI

## 数据变化监听
当Adapter数据源变化的时候会通知Adapter.
也可以在Adapter或者ViewHolder中调用notifyDataChange来显示的通知数据变化.

>Adapter.OnAdapterDataChangeListener()

## Item点击和长按监听
完全实现类似与ListView中的Item点击事件，不需要ViewHolder做任何多余的操作
要使用该功能请保证BaseRecyclerViewConfig.IsEnableItemViewPackage == true,它默认就true
>Adapter.setOnItemClickListener()

>Adapter.setOnItemLongClickListener()



## ViewHolder消息
当ViewHolder中执行某些动作希望Activity或Fragment感知。
在ViewHolder中调用sendMessage()发送消息广播

>Adapter.addViewHolderMessageHandler()


## 懒人版 BaseLoonRecyclerViewAdapter于SimpleRecyclerViewAdapter
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
使用方式:
```
recyclerView.setAdapter(new MyAdapter());
```


SimpleRecyclerViewAdapter允许不自定义Adapter。
使用方式:
```
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
该项目在AndroidX基础上搭建,需要以下库
```
implementation 'androidx.recyclerview:recyclerview:version'
implementation 'androidx.appcompat:appcompat:version'
```

使用了SimpleRecyclerViewAdapter需要添加混淆配置

```
-keep class support.lfp.adapter.BaseRecyclerViewAdapter$BaseViewHolder {* ;}
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
