# livingExpenseRecord
an Android app for recording living expense
---

* [livingExpenseRecord]((https://github.com/Chorior/livingExpenseRecord/blob/release/image/demo.gif))是一个简单的主要用于记录三餐消费情况的android app;
	* 由于[演示gif](https://github.com/Chorior/livingExpenseRecord/blob/release/image/demo.gif)内容过大,所以请自行下载查看;
* 优点
	* 简单无广告,不用联网,拿出手机就能用;
	* 最多记录过去三个月的消费情况,绝对不会有垃圾数据占用存储量;
	* 有图表统计显示数据分布,每月每天的三餐消费一目了然;
* 缺点
	* 可以通过修改系统时间回到过去修改消费记录;
	* 底部的小圆点在打开输入法的时候会升上来;
	* 界面不够优美;
* 资料、依赖库及IDE
	* Android编程权威指南
	* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)
	* Android studio 2.2
	* yED Grahp Editor 3.16.1
* 该app涉及到的android知识
	* [如何使用Fragment][Fragment];
	* [ListView的使用][ListView];
	* [如何从fragment启动Activity并传递信息][Fragment2Acticity];
	* [ViewPager的使用][ViewPager];
	* [添加选项菜单][OptionsMenu];
	* [菜单栏添加向上导航菜单][NavUtils];
	* [本地数据的存储和读取][SaveDataLocally];
	* [ListView的上下文操作实现批量删除][ChoiceMode_ListView];
	* [为ViewPager添加底部小圆点导航][dots];
	* [MPAndroidChart库的基本使用][MPAndroidChart];
* 后记
	* 这个app最初的需求是这样的

		![requirement](https://github.com/Chorior/livingExpenseRecord/blob/release/image/requirements.png)

	* 后来因为自定义项与三餐冲突了,所以改成了others;
	* 文本日记应该没人会写,所以取消了;
	* 推送感觉挺讨厌的,不过做了图表;
	* 我不是专业的android程序员,写的不好的地方请见凉;

[Fragment]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/useFragment.md
[ListView]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/useListView.md
[Fragment2Acticity]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/Fragment2Activity.md
[ViewPager]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/useViewPager.md
[OptionsMenu]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/addOptionsMenu.md
[NavUtils]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/useNavUtils.md
[SaveDataLocally]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/SaveDataLocally.md
[ChoiceMode_ListView]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/ChoiceMode_ListView.md
[dots]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/addDots.md
[MPAndroidChart]:https://github.com/Chorior/livingExpenseRecord/blob/release/docs/useMPAndroidChart.md

# License

```
Copyright 2016 Chorior

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
