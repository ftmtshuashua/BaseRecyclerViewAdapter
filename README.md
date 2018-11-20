# BaseRecyclerViewAdapter
一个RecyclerView适配器
>已适配AndroidX

## 使用
请参考：[ARDF](https://github.com/ftmtshuashua/ARDF)的适配器部分



## 配置(暂时未上传成功)
```
//implementation 'support.lfp:BaseRecyclerViewAdapter:1.0.0'
```


在项目的build.gradle中添加kotlin支持
```
buildscript {
    ext.kotlin_version = 'version'
    repositories {
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```


如果启用了混淆
```
-keep class support.lfp.adapter.SimpleRecyclerViewAdapter$NotProguardViewHolder {* ;}
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
