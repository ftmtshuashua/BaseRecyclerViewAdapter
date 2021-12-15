BaseRecyclerViewAdapter
=====
[![](https://jitpack.io/v/ftmtshuashua/BaseRecyclerViewAdapter.svg)](https://jitpack.io/#ftmtshuashua/BaseRecyclerViewAdapter)
![](https://img.shields.io/badge/android-4.0%2B-blue)
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

BaseRecyclerViewAdapter是一个RecyclerView的万能适配器，它的内部维护一个数据集合，当数据发生变化的时候自动反映到UI上。

> ItemView点击长按事件监听 ： Adapter.setOnItemClickListener()

> Adapter数据变化监听 ： Adapter.setOnAdapterDataChangeListener()

> ViewHolder消息 ： Adapter.addViewHolderMessageHandler()

> 懒人模式 ： BaseLoonRecyclerViewAdapter 与 SimpleRecyclerViewAdapter

> 多种布局 ：MultipleRecyclerViewAdapter + MultipleViewModel

> 数据差分器 ：无需更新整个列表,只对变化做处理,某些情况下能极大的提升性能

> 侧滑菜单 ：SlideMenu

## BaseRecyclerViewAdapter

内部维护多个数据集合，它会根据数据的变化自动更新UI
> set()
> add()
> insert()
> move()
> replace()
> **Only() Only方法只改变数据源，需要手动刷新UI

## ItemView点击和长按监听

不需要ViewHolder做任何适配，完美实现点击事件监听
> Adapter.setOnItemClickListener()
> Adapter.setOnItemLongClickListener()

1.在RecyclerView中没有类似ListView的点击长按事件监听，需要我们在ViewHolder中自己处理点击事件，使用起来比较麻烦。而且直接给ViewHolder的itemView设置点击事件会出现一些问题。
2.我的处理方法是在itemView创建的时候给他设置点击监听器，然后在它外面包裹一层FragmentLayout用来当作真正的itemView，原来的itemView就变成了它的内容。 3.不需要对ViewHolder做任何改动完美实现点击事件监听

## 普通使用

```
    class MyRecyclerViewAdapter : BaseRecyclerViewAdapter<String>() {
        override fun onCreateViewHolder(parent: ViewGroup, var2: Int): BaseViewHolder<String> {
            return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_textview, parent, false))
        }
    }

    class MyViewHolder(itemView: View) : BaseViewHolder<String>(itemView) {
        override fun onUpdateUI(data: String?) {
            setText(R.id.view_Info, data ?: "null")
        }
    }

    private val mAdapter by lazy { MyRecyclerViewAdapter() }
 
    mAdapter.set("data1")
    mAdapter.add("data2")


```

## 多种布局 MultipleRecyclerViewAdapter + MultipleViewModel

```
//绑定布局与数据
class TextViewModel(val mMsg: String) : MultipleViewModel(R.layout.layout_textview) {
    override fun onUpdate(holder: BaseViewHolder<*>) { // 这里处理布局与数据的关系
        holder.setText(R.id.view_Info, mMsg)
    }
}
class ButtonViewModel(val mMsg: String, val mOnClick: () -> Unit) : MultipleViewModel(R.layout.layout_button) {
    override fun onUpdate(holder: BaseViewHolder<*>) {
        holder.setText(R.id.view_Button, mMsg)
    }
    override fun onClick(context: Context) {
        mOnClick()
    }
}

//使用时传入绑定号的数据对象
private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

mAdapter.add(TextViewModel("layout_textview 布局") 
mAdapter.add(ButtonViewModel("layout_button 布局") { })

```

## 侧滑菜单

```
val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }

//创建菜单
class MyLefSlideMenu1 : SlideMenu(MENU_ID_1, Place.LEFT, R.layout.menu_left_1) {
        override fun onViewBind(menu: View, vh: BaseViewHolder<*>) {
            menu.findViewById<View>(R.id.view_Button).setOnClickListener { mAdapter.remove(vh.dataPosition) }
        }
    }
    
//使用菜单
mAdapter.addSlideMenu(MyLefSlideMenu1())

//调用 ViewHolder 的 setSlideMenuIds() 方法配置当前 ViewHolder 应用的菜单，不调用则应用全部菜单
class MenuViewModel() : MultipleViewModel(R.layout.layout_slide_menu) {
        override fun onUpdate(holder: BaseViewHolder<*>) {
            holder.setSlideMenuIds(MENU_ID_1)
        }
    }
```

## 差分算法

无需更新整个列表,只对变化做处理,某些情况下能极大的提升性能

```
    private val mAdapter by lazy { MultipleRecyclerViewAdapter<MultipleViewModel>() }
    
    //启动差分算法
    mAdapter.setDiffEnable(true)    
    
    //异步执行差分算法
    mAdapter.setDiffAsync(true)    
    
    //更新数据时，直接设置新数据源即可。差分算法内部通过 equals() 方法判断是否为同一个数据源
    mAdapter.set(datas)
```

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

SimpleRecyclerViewAdapter允许不自定义Adapter。 使用方式:

```
//Class<? extends BaseViewHolder<D>>   :设置被Adapter加载的ViewHolder
//layoutResId  :Viewholder使用的布局
recyclerView.setAdapter(new SimpleRecyclerViewAdapter(Class<? extends BaseViewHolder<D>>,layoutResId));
```

## 数据变化监听

对Adapter中数据变化做出响应
> Adapter.setOnAdapterDataChangeListener()

1.显示调用改变数据的方法 Set() | Add()  | Insert()  || Move() || replace() 会自动回调该方法 2.其他情况通过手动调用Adapter或Viewholder中的 notifyDataChange() 方法来通知数据变化

## ViewHolder消息

> Adapter.addViewHolderMessageHandler()

1.在Adapter与Viewholder中调用sendMessage()来发送一条消息到Adapter的消息监听器中

## 配置依赖

在项目的build.gradle中添加

```
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

```
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'com.github.ftmtshuashua:BaseRecyclerViewAdapter:1.2.0'
}
``` 

 