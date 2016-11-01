# 添加选项菜单
---
* AppCompatActivity是FragmentActivity的继承类,它包含actionBar的实现,将RecordActivity的超类修改为AppCompatActivity即可显示标题栏;
* 首先添加菜单栏
	* 为res添加android resource directory,选择resource type为menu;
	* 然后在menu目录下新增menu resource file,名称设为fragment_record_menu.xml
		* 这里要注意的是,使用了Support库,你必须声明自己的XML命名空间(就是`xmlns:app="http://schemas.android.com/apk/res-auto"`);
		* 虽然icon和title都被设置了,但是默认会显示图标,除非你设置`always|withText`;

		```xml
		<?xml version="1.0" encoding="utf-8"?>
		<menu xmlns:android="http://schemas.android.com/apk/res/android"
			xmlns:app="http://schemas.android.com/apk/res-auto">
		<item
			android:id="@+id/menu_item_save_record"
			android:icon="@android:drawable/ic_menu_save"
			android:title="@string/save_record"
			app:showAsAction="ifRoom|withText"
			/>
		</menu>
		```

	* 复制sdk目录下`/platforms/android-24/data/res/drawable-hdpi/ic_menu_save.png`到`app/src/main/res/drawable/ic_menu_save.png`,这样会使得所有设备上的选项图标统一;
	* 创建选项菜单和响应菜单项选择事件的两个回调方法
		* `public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)`;
		* `public boolean onOptionsItemSelected(MenuItem item)`;
	* 在RecordFragment.java中生成选项菜单;

		```java
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.fragment_record_menu,menu);
		}
		```

	* Fragment的onCreateOptionsMenu方法由FragmentManager负责调用;
	* 当activity受到该请求时,FragmentManager需要知道其管理的Fragment应接受onCreateOptionsMenu方法的调用指令;
	* 所以添加`setHasOptionsMenu(true);`到RecordFragment.java

		```java
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);

			mRecord = new Record();
		}
		```

* 接下来响应菜单项选择;
	* 使用onOptionsItemSelected方法响应菜单项选择事件(RecordFragment.java)

		```java
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()){
				case R.id.menu_item_save_record:
					...
					return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		}
		```
