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

* step1
	* 最后效果

		![UI_step1](https://github.com/Chorior/livingExpenseRecord/blob/master/image/UI_step1.png)

	* mvc图解

		![mvc_step1](https://github.com/Chorior/livingExpenseRecord/blob/master/image/mvc_step1.png)

	* 图中,要开发的类为
		* Record: 代表某一天的生活费记录单,暂时只包含早餐费用和一个标识ID,标识ID用来区分记录单实例;
		* RecordFragment;
		* RecordActivity;
	* 第一步我只使用一个Record实例,并将其放在RecordFragment类的成员mRecord中;
	* RecordActivity视图由FrameLayout组件组成,FrameLayout组件为RecordFragment要显示的视图安排了存放位置;
	* RecordFragment视图由一个LinearLayout组件及一个EditText组件组成;
	* RecordFragment类中有一个存储EditText的成员变量(mBreakfastField),这个变量监听EditText,当其发生变化时,用来更新模型层数据;
	* 实现
		* activity默认支持API11级及以上版本中的fragment;
		* 首先修改RecordActivity.java

			```java
			public class RecordActivity extends Activity {

			    @Override
			    protected void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        setContentView(R.layout.activity_record);
			    }
			}
			```

		* 创建Record类

			```java
			public class Record extends Object {
			    private UUID mId;
			    private short mBreakfast;

			    public UUID getmId() {
			        return mId;
			    }

			    public short getmBreakfast() {
			        return mBreakfast;
			    }

			    public void setmBreakfast(short mBreakfast) {
			        this.mBreakfast = mBreakfast;
			    }

			    public Record()
			    {
			        mId = UUID.randomUUID();
			    }
			}
			```

		* 托管UI fragment
			* 要想托管UI fragment,activity必须做到
				* 在布局中为fragment视图安排位置;
				* 管理fragment实例的生命周期;
			* 在activity中托管一个UI fragment,有两种方式
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

		* 创建UI fragment
			* 创建步骤
				* 通过定义布局文件中的组件,组装界面;
				* 创建fragment类并设置其视图为定义的布局;
				* 通过代码的方式,连接布局文件中生成的组件;
			* 首先定义RecordFragment布局(显示包含在Record类实例中的信息)
				* fragment_record.xml

					```xml
					<?xml version="1.0" encoding="utf-8"?>
					<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
					    android:layout_width="match_parent"
					    android:layout_height="match_parent"
					    android:orientation="vertical"
					    >
					    <EditText
					        android:id="@+id/record_breakfast"
					        android:layout_width="match_parent"
					        android:layout_height="wrap_content"
					        android:hint="@string/record_breakfast_hint"
					        />
					</LinearLayout>
					```

				* 添加`record_breakfast_hint`字符串资源

					```xml
					<resources>
					    <string name="app_name">livingExpenseRecord</string>
					    <string name="record_breakfast_hint">Enter a cost for your breakfast.</string>
					</resources>
					```

			* 然后创建RecordFragment类
				* 通过onCreateView()生成fragment的视图;
				* 视图生成后,引用EditText组件并添加对应的监听器方法;

					```java
					public class RecordFragment extends Fragment {
					    private Record mRecord;
					    private EditText mBreakfastField;

					    @Override
					    public void onCreate(Bundle savedInstanceState)
					    {
					        super.onCreate(savedInstanceState);
					        mRecord = new Record();
					    }

					    @Override
					    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
					                             Bundle savedInstanceState)
					    {
					        View v = inflater.inflate(R.layout.fragment_record,parent,false);

					        mBreakfastField = (EditText)v.findViewById(R.id.record_breakfast);
					        mBreakfastField.addTextChangedListener(new TextWatcher() {
					            @Override
					            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					                // wait to update
					            }

					            @Override
					            public void onTextChanged(CharSequence s, int start, int before, int count) {
					                mRecord.setmBreakfast(s.toString());
					            }

					            @Override
					            public void afterTextChanged(Editable s) {
					                // wait to update
					            }
					        });
					        return v;
					    }
					}
					```

		* 添加UI fragment到activity
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
* step2
	* 将EditText设为数字框;
	* 修改mBreakfast类型;
	* 修改EditText监听方法;
	* 添加当日生活费总计TextView;
	* 添加中晚餐费用数字框;

		![step2](https://github.com/Chorior/livingExpenseRecord/blob/master/image/step2.png)

* step3
	* 添加日期textView(或者button日期,然后可以根据选择的日期显示那日生活费使用记录);
	* 添加滑屏功能,用于下一步显示记录列表;
	* 结果

		![ui_step3_0](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step3_0.png)

		![ui_step3_1](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step3_1.png)

	* 实现
		* 首先添加日期button显示当日天气,格式示例为"2016 十月 13 星期四",暂时使其不可用;
		* 为Record.java添加日期变量,并生成getter and setter;

			```java
			private Date mDate;
			public Date getmDate() {
		        return mDate;
		    }

		    public void setmDate(Date mDate) {
		        this.mDate = mDate;
		    }
			```

		* 在fragment_record.xml里添加button组件(`record_date_label`为"DATE")

			```xml			
		    <Button
		        android:id="@+id/record_date"
		        android:layout_width="match_parent"
		        android:layout_height="wrap_content"
		        android:layout_marginLeft="16dp"
		        android:layout_marginRight="16dp"
				style="@style/Base.TextAppearance.AppCompat.Menu"
		        />
			```

		* 在RecordFragment.java中显示date button

			```java
			private Button mDateButton;
			mDateButton = (Button)v.findViewById(R.id.record_date);
	        mDateButton.setText(DateFormat.format("yyyy MMMM dd EEEE",mRecord.getmDate()));
	        mDateButton.setEnabled(false);
			```

		* 要实现滑屏显示功能,首先要构建RecordListFragment
			* 先添加RecordListFragment类,ListFragment默认生成一个全屏ListView布局,暂时先不重写onCreateView()方法

				```java
				public class RecordListFragment extends ListFragment {
				    @Override
				    public void onCreate(Bundle savedInstanceState) {
				        super.onCreate(savedInstanceState);
				    }
				}
				```

			* 然后重写RecordActivity.java,由于FragmentPagerAdapter要使用android.support.v4.app.Fragment
				* 修改RecordFragment.java中android.app.Fragment为android.support.v4.app.Fragment;
				* 修改RecordListFragment.java中android.app.ListFragment为android.support.v4.app.ListFragment;
				* 修改RecordActivity.java中
					* android.app.Activity为android.support.v4.app.FragmentActivity;
					* android.app.FragmentManager为android.support.v4.app.FragmentManager;
					* getFragmentManager()为getSupportFragmentManager();
				* 重写后的RecordActivity.java如下

					```java
					public class RecordActivity extends FragmentActivity {

					    private ViewPager mViewPager;
					    private List<Fragment> mFragmentList = new ArrayList<>();;

					    @Override
					    protected void onCreate(Bundle savedInstanceState) {
					        super.onCreate(savedInstanceState);
					        setContentView(R.layout.activity_record);

					        mViewPager = (ViewPager)findViewById(R.id.viewPager);
					        Fragment fragment0 = new RecordFragment();
					        Fragment fragment1 = new RecordListFragment();
					        mFragmentList.add(fragment0);
					        mFragmentList.add(fragment1);

					        FragmentManager fm = getSupportFragmentManager();
					        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
					            @Override
					            public Fragment getItem(int position) {
					                return mFragmentList.get(position);
					            }

					            @Override
					            public int getCount() {
					                return mFragmentList.size();
					            }
					        });
					    }
					}
					```

* step4
	* 实现Record的列表显示fragment;
	* 点击某一天的实例时,出现那一天的详细记录,并且可以左右滑动显示上一天或下一天的记录;
