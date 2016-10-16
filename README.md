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
	* 列表项需要显示对应的日期和消费金额;
	* 结果

		![ui_step4_1](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step4_1.png)

	* mvc图

		![mvc_step4](https://github.com/Chorior/livingExpenseRecord/blob/master/image/mvc_step4.png)

	* 采用`ArrayList<E>`保存Record实例,将该数组存储在一个单例里;
		* 单例是特殊的java类,创建实例时,一个类仅允许创建一个实例;
		* 应用能够在内存里存在多久,单例就能存在多久;
	* 首先创建RecordLab.java(发现id没什么软用,删了)

		```java
		public class RecordLab {
		    private static RecordLab sRecordLab;
		    private Context mAppContext;
		    private ArrayList<Record> mRecords;

		    private RecordLab(Context appContext){
		        mAppContext = appContext;
		        mRecords = new ArrayList<Record>();

		        Calendar cal = Calendar.getInstance();
		        cal.set(Calendar.DAY_OF_MONTH,1);

		        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		        for(int i = 0; i < days; ++ i,cal.add(Calendar.DATE,1)){
					Record r = new Record();
		            r.setmDate(cal.getTime());
		            r.setmBreakfast(12 + i);
		            r.updatemTotal_today();
		            mRecords.add(r);
		        }
		    }

		    public static RecordLab get(Context c){
		        if(null == sRecordLab){
		            sRecordLab = new RecordLab(c.getApplicationContext());
		        }
		        return sRecordLab;
		    }

		    public ArrayList<Record> getRecords(){
		        return mRecords;
		    }

		    public Record getRecord(Date date){
		        for(Record r : mRecords){
		            if(r.getmDate().equals(date)){
		                return r;
		            }
		        }
		        return null;
		    }
		}
		```

	* 修改RecordListFragment.java获取Record列表

		```java
		public class RecordListFragment extends ListFragment {
		    private ArrayList<Record> mRecords;

		    @Override
		    public void onCreate(Bundle savedInstanceState) {
		        super.onCreate(savedInstanceState);
		        mRecords = RecordLab.get(getActivity()).getRecords();
		    }
		}
		```

	* 通过RecordListFragment的ListView将Record列表显示出来;
		* ListView是ViewGroup的子类,每个列表项都是作为ListView的一个View子对象显示的;
		* 每个Record列表只需要显示对应的日期,View对象是一个简单的TextView;
		* 当ListView需要显示某个列表项时,它才会去申请一个可用的视图对象;
		* adapter是一个控制器对象,从模型层获取数据,并将其提供给ListView显示,它有三个作用
			* 创建必要的视图对象;
			* 用模型层数据填充视图对象;
			* 将准备好的视图对象返回给ListView;
		* 当ListView需要显示视图对象时,会与其adapter展开会话沟通
			* 首先调用adapter.getCount()询问数组列表对象个数;
			* 然后调用adapter.getView(int,View,ViewGroup);
		* RecordListFragment.java

			```java
			public class RecordListFragment extends ListFragment {
			    private ArrayList<Record> mRecords;

			    @Override
			    public void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        mRecords = RecordLab.get(getActivity()).getRecords();

			        ArrayAdapter<Record> adapter =
			                new ArrayAdapter<Record>(getActivity(),
			                        android.R.layout.simple_list_item_1,
			                        mRecords);
			        setListAdapter(adapter);
			    }
			}
			```

		* 覆盖Record.toString()方法

			```java
			@Override
		    public String toString() {
		        return DateFormat.format("MMMM dd",mDate).toString();
		    }
			```

		* 响应列表项点击事件(暂时不进行处理),RecordListFragment.java

			```java
			@Override
		    public void onListItemClick(ListView l, View v, int position, long id) {
		        //Record r = (Record)(getListAdapter()).getItem(position);
		        //wait to update
		    }
			```

		* 定制列表项,显示对应的日期和消费金额
			* 首先创建列表项视图的XML布局文件;
				* RelativeLayout子视图相对于根布局及子视图相对于子视图的布置排列,需要使用一些布局参数加以控制;
				* 我想要消费金额对齐RelativeLayout布局的右手边,日期相对于消费金额左对齐;
				* list_item_record.XML

					```XML
					<?xml version="1.0" encoding="utf-8"?>
					<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
					    android:layout_width="match_parent"
					    android:layout_height="match_parent">

					    <TextView
					        android:id="@+id/record_list_item_dateTextView"
					        android:layout_width="match_parent"
					        android:layout_height="wrap_content"
					        android:gravity="start"
					        android:textStyle="bold"
					        android:paddingLeft="4dp"
					        android:paddingRight="4dp"
					        android:text="@string/record_list_item_dateText"
					        />
					    <TextView
					        android:id="@+id/record_list_item_totalTodayTextView"
					        android:layout_width="match_parent"
					        android:layout_height="wrap_content"
					        android:layout_below="@id/record_list_item_dateTextView"
					        android:gravity="end"
					        android:layout_alignParentRight="true"
					        android:layout_alignParentEnd="true"
					        android:paddingLeft="4dp"
					        android:paddingRight="4dp"
					        android:text="@string/record_list_item_totalTodayText"
					        />

					</RelativeLayout>
					```

			* 修改RecordListFragment.java

				```java
				public class RecordListFragment extends ListFragment {
				    private ArrayList<Record> mRecords;

				    private class RecordAdapter extends ArrayAdapter<Record>{
				        public RecordAdapter(ArrayList<Record> records){
				            super(getActivity(),0,records);
				        }

				        @NonNull
				        @Override
				        public View getView(int position, View convertView, ViewGroup parent) {
				            // if we weren't given a view, inflate one
				            if(null == convertView){
				                convertView = getActivity().getLayoutInflater()
				                        .inflate(R.layout.list_item_record, null);
				            }

				            // configure the view for this record
				            Record record = getItem(position);

				            TextView dateTextView =
				                    (TextView)convertView.findViewById(R.id.record_list_item_dateTextView);
				            dateTextView.setText(DateFormat.format("MMMM dd",record.getmDate()));

				            TextView totalTodayTextView =
				                    (TextView)convertView.findViewById(R.id.record_list_item_totalTodayTextView);
				            totalTodayTextView.setText(String.valueOf(record.getmTotal_today()));

				            return convertView;
				        }
				    }

				    @Override
				    public void onCreate(Bundle savedInstanceState) {
				        super.onCreate(savedInstanceState);
				        mRecords = RecordLab.get(getActivity()).getRecords();

				        RecordAdapter adapter = new RecordAdapter(mRecords);
				        setListAdapter(adapter);
				    }

				    @Override
				    public void onListItemClick(ListView l, View v, int position, long id) {
				        //Record r = ((RecordAdapter)getListAdapter()).getItem(position);
				        //wait to update
				    }
				}
				```

* step5
	* 响应列表点击事件,显示相应详细记录,不可修改;
	* 结果

		![ui_step5_2](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step5_2.png)

	* 要实现记录的不可修改,需要修改一下布局,将EditText改为TextView,因为当日的可修改界面还是需要的,所以再做一个activity和fragment;
		* 首先是`activity_record_final.xml`

			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:id="@+id/finalFragmentContainer"
			    >
			</FrameLayout>
			```

		* 然后是`fragment_record_final.xml`

			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical"
			    >
			    <Button
			        android:id="@+id/record_date"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        style="@style/Base.TextAppearance.AppCompat.Menu"
			        />
			    <TextView
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:text="@string/record_breakfast_label"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_breakfast"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:layout_gravity="end"
			        />
			    <TextView
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:text="@string/record_lunch_label"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_lunch"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:layout_gravity="end"
			        />
			    <TextView
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:text="@string/record_dinner_label"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_dinner"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:layout_gravity="end"
			        />
			    <TextView
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:id="@+id/record_total_today"
			        android:gravity="end"
					android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        style="?android:listSeparatorTextViewStyle"
			        />
			</LinearLayout>
			```

		* 接着是finalRecordFragment.java

			```java
			public class finalRecordFragment extends Fragment {
			    private Record mRecord;
			    private Button mDateButton;
			    private TextView mBreakfast;
			    private TextView mLunch;
			    private TextView mDinner;
			    private TextView mTotal_today;
			    private final String str_total_today_prefix = "TOTAL ";

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
			        View v = inflater.inflate(R.layout.fragment_record_final,parent,false);

			        mDateButton = (Button)v.findViewById(R.id.record_date_final);
			        mDateButton.setText(DateFormat.format("EEEE, MMMM dd",mRecord.getmDate()));
			        mDateButton.setEnabled(false);

			        mBreakfast = (TextView)v.findViewById(R.id.record_breakfast_final);
			        mBreakfast.setText(String.valueOf(mRecord.getmBreakfast()));

			        mLunch = (TextView)v.findViewById(R.id.record_lunch_final);
			        mLunch.setText(String.valueOf(mRecord.getmLunch()));

			        mDinner = (TextView)v.findViewById(R.id.record_dinner_final);
			        mDinner.setText(String.valueOf(mRecord.getmDinner()));

			        mTotal_today = (TextView)v.findViewById(R.id.record_total_today_final);
			        mTotal_today.setText(str_total_today_prefix + "" +
			                String.valueOf(mRecord.getmTotal_today()));

			        return v;
			    }
			}
			```

		* 最后是finalRecordActivity.java

			```java
			public class finalRecordActivity extends FragmentActivity {
			    @Override
			    public void onCreate(Bundle savedInstanceState) {
			        super.onCreate(savedInstanceState);
			        setContentView(R.layout.activity_record_final);
			        FragmentManager fm = getSupportFragmentManager();
			        Fragment fragment = fm.findFragmentById(R.id.finalFragmentContainer);

			        if(null == fragment){
			            fragment = new finalRecordFragment();
			            fm.beginTransaction()
			                    .add(R.id.finalFragmentContainer, fragment)
			                    .commit();
			        }
			    }
			}
			```

	* 下面实现从RecordListFragment.java中启动finalRecordActivity,这里一定要注意
		* 一定要在mainfest.xml里面登记你要启动的activity,默认是不会登记的

			```java
			@Override
		    public void onListItemClick(ListView l, View v, int position, long id) {
		        //Record record = ((RecordAdapter)getListAdapter()).getItem(position);
		        Intent i = new Intent(getActivity(),finalRecordActivity.class);
		        startActivity(i);
		    }
			```

	* 启动的finalRecordActivity显示的是新new出来的record,这不是我们需要的,我们需要的是点击record的信息;
		* 方法一: 通过将Record.mdate附加到Intent的extra上,可以告知RecordFragment应该显示的Record;
			* 缺点: finalRecordFragment不再可复用,finalRecordActivity的Intent也定义了一个extra;
			* 优点: 快速,简单;
			* 首先在finalRecordFragment.java中添加extra值

				```java
				public static final String EXTRA_RECORD_DATE =
	            	"org.chorior.pengzhen.recordIntent.record_date";
					@Override
			    public void onCreate(Bundle savedInstanceState)
			    {
			        super.onCreate(savedInstanceState);
			        Date recordDate = (Date)getActivity().getIntent()
			                .getSerializableExtra(EXTRA_RECORD_DATE);
			        mRecord = RecordLab.get(getActivity()).getRecord(recordDate);
			    }
				```

			* 然后在RecordListFragment.java中向extra值附加Serializable信息

				```java
				@Override
			    public void onListItemClick(ListView l, View v, int position, long id) {
			        Record record = ((RecordAdapter)getListAdapter()).getItem(position);

			        Intent i = new Intent(getActivity(),finalRecordActivity.class);
			        i.putExtra(finalRecordFragment.EXTRA_RECORD_DATE,record.getmDate());
			        startActivity(i);
			    }
				```

		* 方法二: 使用arguments bundle
			* 每个fragment实例都可附带一个Bundle对象,该bundle包含键值对,一个key-value对即是一个argument;
			* 缺点: 比较复杂;
			* 优点: 可重复使用,没有限制;
			* 首先是创建fragment argument的方法

				```java
				Bundle args = new Bundle();
				args.putSerializable(EXTRA_MY_OBJECT,myObject);
				args.putInt(EXTRA_MY_INT,myInt);
				args.putCharSequence(EXTRA_MY_STRING,myString);
				```

			* 调用Fragment.setArgument(Bundle)方法附加argument bundle给finalRecordFragment

				```java
				public static finalRecordFragment newInstance(Date recordDate){
			        Bundle args = new Bundle();
			        args.putSerializable(EXTRA_RECORD_DATE,recordDate);

			        finalRecordFragment finalFragment = new finalRecordFragment();
			        finalFragment.setArguments(args);

			        return finalFragment;
			    }
				```

			* 修改finalRecordActivity创建finalRecordFragment的方法

				```java
				if(null == fragment){
		            Date recordDate = (Date)getIntent()
		                    .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
		            fragment = finalRecordFragment.newInstance(recordDate);

		            fm.beginTransaction()
		                    .add(R.id.finalFragmentContainer, fragment)
		                    .commit();
		        }
				```

			* 修改finalRecordFragment.java

				```java
				@Override
			    public void onCreate(Bundle savedInstanceState)
			    {
			        super.onCreate(savedInstanceState);

			        Date recordDate = (Date)getArguments().getSerializable(EXTRA_RECORD_DATE);
			        mRecord = RecordLab.get(getActivity()).getRecord(recordDate);
			    }
				```

			* 最后java,RecordListFragment.java中添加Date信息的方法与方法一相同;

* step6
	* 点击列表项后,可使用ViewPager左右滑动,显示上一天或下一天的记录信息
	* 与RecordActivity类似,我们将activity_record_final中的FrameLayout组件替换为ViewPager

		```xml
		<?xml version="1.0" encoding="utf-8"?>
		<android.support.v4.view.ViewPager
		    xmlns:android="http://schemas.android.com/apk/res/android"
		    android:id="@+id/viewPagerFinal"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    />
		```

	* 修改finalRecordActivity.java

		```java
		public class finalRecordActivity extends FragmentActivity {
		    private ViewPager mViewPager;
		    private ArrayList<Record> mRecords;

		    @Override
		    protected void onCreate(@Nullable Bundle savedInstanceState) {

		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.activity_record_final);

		        mViewPager = (ViewPager)findViewById(R.id.viewPagerFinal);
		        mRecords = RecordLab.get(this).getRecords();

		        FragmentManager fm = getSupportFragmentManager();

		        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
		            @Override
		            public Fragment getItem(int position) {
		                Record record = mRecords.get(position);
		                return finalRecordFragment.newInstance(record.getmDate());
		            }

		            @Override
		            public int getCount() {
		                return mRecords.size();
		            }
		        });

		        Date recordDate = (Date)getIntent()
		                .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
		        for(int i = 0; i < mRecords.size(); ++ i){
		            if(mRecords.get(i).getmDate().equals(recordDate)){
		                mViewPager.setCurrentItem(i);
		                break;
		            }
		        }
		    }
		}
		```

* step7
	* 删除RecordLab随机生成的Record实例,改为获取用户添加的Record实例;
