# 使用Fragment
---
* 首先在Activity中托管fragment
	* 要想托管fragment,activity必须做到
		* 在布局中为fragment视图安排位置;
		* 管理fragment实例的生命周期;
	* 在activity中托管一个fragment,有两种方式
		* 添加fragment到activity布局中
			* 简单但不够灵活;
			* 在activity生命周期过程中,无法切换fragment视图;
		* 在activity代码中添加fragment
			* 比较复杂;
			* 可以在运行时控制fragment;
	* 我们选择第二种方式添加fragment;
	* 在RecordActivity布局中为RecordFragment视图安排位置
		* activity_record.xml

			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<FrameLayout
				xmlns:android="http://schemas.android.com/apk/res/android"
				android:id="@+id/fragmentContainer"
				android:layout_width="match_parent"
				android:layout_height="match_parent"
				/>
			```

* 然后创建fragment
	* 通过定义布局文件中的组件,组装界面;
	* 创建fragment类并设置其视图为定义的布局;
	* 通过代码的方式,连接布局文件中生成的组件;

* 接着添加Fragment到Activity
	* Activity类含有fragmentManager类,这个类专门负责管理fragment并将它们的视图添加到activity视图中;
	* FragmentManager类具体管理的是
		* fragment队列;
		* fragment事务的回退栈;
	* 直接调用activity的FragmentManager,添加fragment
		* fragment事务被用来添加、移除、附加、分离或替换fragment队列中的fragment;
		* fragmentContainer是`activity_record.xml`中FrameLayout组件的资源ID,其作用为
			* 告知FragmentManager,fragment视图应该出现在activity视图的位置;
			* 是FragmentManager队列中fragment的唯一标识符;

				```java
				public class RecordActivity extends Activity {

					@Override
					protected void onCreate(Bundle savedInstanceState) {
						super.onCreate(savedInstanceState);
						setContentView(R.layout.activity_record);

						FragmentManager fm = getFragmentManager();
						Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

						if(null == fragment)
						{
							fragment = new RecordFragment();
							fm.beginTransaction()
									.add(R.id.fragmentContainer,fragment)
									.commit();
						}
					}
				}
				```
