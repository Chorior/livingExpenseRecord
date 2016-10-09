# livingExpenseRecord
an Android app for recording living expense

---
* 需求
	* 每次打开app显示当天未更新的费用使用界面,包含
		* 本月已使用费用总计数额;
		* 早中晚餐费用;
		* 可多次添加的自定义费用登记,包含
			* 一个title;
			* 一个数字费用;
			* 一个详细说明文本(需要么?);
		* 文本日记(需要么?);
	* 滑动界面可查看本月过去时间的任意一天费用使用记录,以多层list列出(可修改?);
	* 每月一号推送上月的生活费使用记录图表(好像挺难的样子😃);
	* 考虑到本地存储容量的问题,只保存最近三个月的费用使用情况(但是可以保存图表?);
* 工具
	* android studio 2.2
	* yED Grahp Editor 3.16.1

* 示意图

![diagram](https://github.com/Chorior/livingExpenseRecord/blob/master/image/diagram.png)
