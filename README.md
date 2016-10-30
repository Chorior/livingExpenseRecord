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
	* 为fragment_record添加菜单栏,设置保存按钮并且删除editText监听事件处理;
	* 删除RecordLab中随机生成的Record实例,改为用户保存的真实Record;
	* 结果

		![ui_step7_0](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step7_0.png)

		![ui_step7_1](https://github.com/Chorior/livingExpenseRecord/blob/master/image/ui_step7_1.png)

	* 首先添加菜单栏
		* 为res添加android resource directory,选择resource type为menu;
		* 然后在menu目录下新增menu resource file,名称设为fragment_record_menu.xml
			* 这里要注意的是,使用了Support库,你必须声明自己的XML命名空间,然后使用showAsAction;
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

		* 复制sdk目录下`/platforms/android-24/data/res/drawable-hdpi/ic_menu_save.png`到`app/src/main/res/drawable/ic_menu_save.png`;
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
		* 再次修改RecordFragment.java			

			```java
			@Override
		    public void onCreate(Bundle savedInstanceState)
		    {
		        super.onCreate(savedInstanceState);
		        setHasOptionsMenu(true);

		        mRecord = new Record();
		    }
			```

		* 发现菜单栏没有显示,正在处理中;
		* AppCompatActivity是FragmentActivity的继承类,它包含actionBar的实现,将RecordActivity的超类修改为AppCompatActivity即可显示标题栏;
	* 接下来响应菜单项选择;
		* 先为RecordLab添加新增方法,删除原来的自定义Record实例

			```java
			public void addRecord(Record record)
		    {
				if(0 != mRecords.size()){
		            for(int i = mRecords.size() - 1; i >= 0; -- i){
		                if(mRecords.get(i).getmDate().equals(record.getmDate())){
		                    mRecords.get(i).setmBreakfast(record.getmBreakfast());
		                    mRecords.get(i).setmLunch(record.getmLunch());
		                    mRecords.get(i).setmDinner(record.getmDinner());
		                    return;
		                }
		            }
		        }

		        mRecords.add(record);
		    }
			```

		* 然后使用onOptionsItemSelected方法响应菜单项选择事件(RecordFragment.java)

			```java
			@Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		        switch(item.getItemId()){
		            case R.id.menu_item_save_record:

						mRecord.setmDate(new Date());

		                if(0 != mBreakfastField.getText().length()) {
		                    mRecord.setmBreakfast(
		                            Integer.parseInt(mBreakfastField.getText().toString()));
		                } else {
		                    mRecord.setmBreakfast(0);
		                }
		                if(0 != mLunchField.getText().length()) {
		                    mRecord.setmLunch(
		                            Integer.parseInt(mLunchField.getText().toString()));
		                } else {
		                    mRecord.setmLunch(0);
		                }
		                if(0 != mDinnerField.getText().length()) {
		                    mRecord.setmDinner(
		                            Integer.parseInt(mDinnerField.getText().toString()));
		                } else {
		                    mRecord.setmDinner(0);
		                }
		                mTotal_today.setText(str_total_today_prefix + "" +
		                        mRecord.getmTotal_today());

		                RecordLab.get(getActivity()).addRecord(mRecord);
		                return true;
		            default:
		                return super.onOptionsItemSelected(item);
		        }
		    }
			```

	* 最后为不同的fragment显示不同的title(RecordActivity.java);

		```java
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        Date date = new Date();
                        setTitle(DateFormat.format("yyyy-MM-dd",date));
                        break;
                    case 1:
                        setTitle(R.string.record_list_title);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
		```

	* 测试发现保存菜单按下后listFragment数据没有立即刷新,所以在RecordActivity中onPageSelected方法里添加滑动更新

		```java
		public void refreshData(){
	        adapter.notifyDataSetChanged();
	    }
		```

		```java
		case 1:
            setTitle(R.string.record_list_title);
            mRecordListFragment.refreshData();
            break;
		```

* step8
	* 删除finalRecordFragment视图的Date按钮,因为它实在是太丑了,换为显示在标题栏上;
	* 设置滑动显示列表项详细记录时,标题栏日期跟随变换;
	* 为finalRecordActivity添加标题栏向上导航菜单;
	* 为空list视图设置空视图;
	* 删除finalRecordFragment视图的Date按钮,标题栏日期滑动变换
		* 首先将finalRecordActivity的超类替换为AppCompatActivity;
		* 接着删除fragment_record_final的button组件和finalRecordFragment的mDateButton;
		* 然后在finalRecordActivity中重写addOnPageChangeListener方法

			```java
			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
	            @Override
	            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	            }

	            @Override
	            public void onPageSelected(int position) {
	                setTitle(DateFormat.format("yyyy-MM-dd",
	                            mRecords.get(position).getmDate()));
	            }

	            @Override
	            public void onPageScrollStateChanged(int state) {

	            }
	        });
			```

		* 最后在finalRecordActivity的onCreate方法中添加

		 	```java
			Date recordDate = (Date)getIntent()
                .getSerializableExtra(finalRecordFragment.EXTRA_RECORD_DATE);
	        for(int i = 0; i < mRecords.size(); ++ i){
	            if(mRecords.get(i).getmDate().equals(recordDate)){
	                mViewPager.setCurrentItem(i);
	                setTitle(DateFormat.format("yyyy-MM-dd",recordDate));
	                break;
	            }
	        }
			```

	* 为finalRecordActivity添加标题栏向上导航菜单
		* 首先在onCreate方法中启用向上导航图标(只有当api >= 11且activity有父activity才能启用)

			```java
			View v = inflater.inflate(R.layout.fragment_record_final,parent,false);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
	            if(null != NavUtils.getParentActivityName(getActivity())){
	                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	            }
	        }
			```

		* 响应向上按钮(finalRecordActivity.java)
			* 首先在onCreate方法中添加`setHasOptionsMenu(true);`
			* 然后利用NavUtils与mainfest实现层级式导航
				* 修改AndroidMainfest.xml
					* 为mainActivity添加launchMode属性为singleTop;
					* 为finalRecordActivity声明添加新的meta-data属性,指定父类为RecordActivity

						```xml
						<activity
				            android:name=".RecordActivity"
				            android:label="@string/app_name"
				            android:launchMode="singleTop">
				            <intent-filter>
				                <action android:name="android.intent.action.MAIN" />

				                <category android:name="android.intent.category.LAUNCHER" />
				            </intent-filter>
				        </activity>
				        <activity
				            android:name=".finalRecordActivity">
				            <meta-data
				                android:name="android.support.PARENT_ACTIVITY"
				                android:value=".RecordActivity"/>
				        </activity>
						```
				* 重写onOptionsItemSelected方法

				```java
				@Override
			    public boolean onOptionsItemSelected(MenuItem item) {
			        switch(item.getItemId()){
			            case android.R.id.home:
			                if(null != NavUtils.getParentActivityName(this)){
			                    NavUtils.navigateUpFromSameTask(this);
			                }
			                return true;
			            default:
			                return super.onOptionsItemSelected(item);
			        }
			    }
				```

	* 为空list视图设置空视图

		```java
		private TextView noItems(String text) {
	        TextView emptyView = new TextView(getActivity());
	        //Make sure you import android.widget.LinearLayout.LayoutParams;
	        emptyView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
	                LinearLayout.LayoutParams.MATCH_PARENT));
	        emptyView.setText(text);
	        emptyView.setTextSize(12);
	        emptyView.setVisibility(View.GONE);
	        emptyView.setGravity(Gravity.CENTER_VERTICAL
	                | Gravity.CENTER_HORIZONTAL);

	        //Add the view to the list view. This might be what you are missing
	        ((ViewGroup) getListView().getParent()).addView(emptyView);

	        return emptyView;
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	        getListView().setEmptyView(
	                noItems(getResources().getString(R.string.empty_text)));
	    }
		```

* step9
	* 发现一个bug: 当修改系统时间时,记录标题时间没有更改,保存记录后,原记录会被覆盖;
	* 给RecordActivity的ViewPager添加显示存储列表中最后三月的消费记录;
	* bug修复
		* `ArrayList<E>`存储的是对象的引用,所以修改原来添加的对象,会使存储的对象也发生改变,所以添加对象改为临时对象即可;
		* Date里面居然有具体时间!修改RecordLab的addRecord方法

			```java
			public void addRecord(Record record)
		    {
		        if(!mRecords.isEmpty()) {
		            for(int i = mRecords.size() - 1; i >= 0; -- i){
		                if(mRecords.get(i).toString().equals(record.toString())){
		                    mRecords.remove(i);
		                    break;
		                }
		            }
		        }

		        mRecords.add(new Record(record));
		        Collections.sort(mRecords,Record.DateComparator);
		    }
			```

	* 添加显示存储列表中最后三月的消费记录
		* 首先在RecordLab中添加updateRecords方法

			```java
			// only record the past 3 months and this month's cost at most
			private void updateRecords(){
				if(!mRecords.isEmpty()){

		            Collections.sort(mRecords,Record.DateComparator);
		            for(int i = 0; i < total_month.length; ++ i){
		                total_month[i] = 0;
		                record_months[i] = "";
		            }

		            int index_totalMonth = 3;
		            String str_temp = mRecords.get(mRecords.size() - 1).getYearAndMonthDate();
		            for(int i = mRecords.size() - 1; i >= 0; -- i){
		                if(!str_temp.equals(mRecords.get(i).getYearAndMonthDate())){
		                    -- index_totalMonth;
		                    str_temp = mRecords.get(i).getYearAndMonthDate();
		                    if(0 > index_totalMonth){
		                        mRecords.subList(0,i + 1).clear();
		                        break;
		                    }
		                }
		                total_month[index_totalMonth] += mRecords.get(i).getmTotal_today();
		                record_months[index_totalMonth] = mRecords.get(i).getYearAndMonthDate();
		            }
		        }
		    }
			```

		* 将这个方法在addRecord最后调用

			```java
			public void addRecord(Record record)
		    {
		        ...

		        mRecords.add(new Record(record));
		        updateRecords();
		    }
			```

		* 添加fragment_total_month.xml视图

			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical"
			    >
			    <TextView
			        android:id="@+id/record_month0"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:gravity="start"
			        android:textStyle="bold"
			        android:paddingLeft="16dp"
			        android:paddingRight="16dp"
			        />
			    <TextView
			        android:id="@+id/record_month0_total"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:gravity="end"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_month1"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:gravity="start"
			        android:textStyle="bold"
			        android:paddingLeft="16dp"
			        android:paddingRight="16dp"
			        />
			    <TextView
			        android:id="@+id/record_month1_total"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:gravity="end"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_month2"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:gravity="start"
			        android:textStyle="bold"
			        android:paddingLeft="16dp"
			        android:paddingRight="16dp"
			        />
			    <TextView
			        android:id="@+id/record_month2_total"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:gravity="end"
			        style="?android:listSeparatorTextViewStyle"
			        />
			    <TextView
			        android:id="@+id/record_month3"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:gravity="start"
			        android:textStyle="bold"
			        android:paddingLeft="16dp"
			        android:paddingRight="16dp"
			        />
			    <TextView
			        android:id="@+id/record_month3_total"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_marginLeft="16dp"
			        android:layout_marginRight="16dp"
			        android:gravity="end"
			        style="?android:listSeparatorTextViewStyle"
			        />

			</LinearLayout>
			```

		* 添加fragment_total_month.java

			```java
			public class fragment_total_month extends Fragment {
			    private TextView record_month3;
			    private TextView record_month2;
			    private TextView record_month1;
			    private TextView record_month0;

			    private TextView record_month3_total;
			    private TextView record_month2_total;
			    private TextView record_month1_total;
			    private TextView record_month0_total;

			    @Nullable
			    @Override
			    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			                             Bundle savedInstanceState)
			    {
			        View v = inflater.inflate(R.layout.fragment_total_month,parent,false);
			        record_month3 = (TextView)v.findViewById(R.id.record_month3);
			        record_month2 = (TextView)v.findViewById(R.id.record_month2);
			        record_month1 = (TextView)v.findViewById(R.id.record_month1);
			        record_month0 = (TextView)v.findViewById(R.id.record_month0);

			        record_month3_total = (TextView)v.findViewById(R.id.record_month3_total);
			        record_month2_total = (TextView)v.findViewById(R.id.record_month2_total);
			        record_month1_total = (TextView)v.findViewById(R.id.record_month1_total);
			        record_month0_total = (TextView)v.findViewById(R.id.record_month0_total);

			        updateTextView();

			        return v;
			    }

			    public void updateTextView(){
			        if(null != record_month3){
			            if(RecordLab.get(getActivity()).getRecord_Months(3).equals("")){
			                Calendar cal = Calendar.getInstance();

			                record_month3.setText(DateFormat.format("yyyy-MM",cal.getTime()));
			            }else{
			                record_month3.setText(RecordLab.get(getActivity()).getRecord_Months(3));
			            }
			        }
			        if(null != record_month2){
			            if(RecordLab.get(getActivity()).getRecord_Months(2).equals("")){
			                Calendar cal = Calendar.getInstance();
			                cal.add(Calendar.MONTH,-1);

			                record_month2.setText(DateFormat.format("yyyy-MM",cal.getTime()));
			            }else{
			                record_month2.setText(RecordLab.get(getActivity()).getRecord_Months(2));
			            }
			        }
			        if(null != record_month1){
			            if(RecordLab.get(getActivity()).getRecord_Months(1).equals("")){
			                Calendar cal = Calendar.getInstance();
			                cal.add(Calendar.MONTH,-2);

			                record_month1.setText(DateFormat.format("yyyy-MM",cal.getTime()));
			            }else{
			                record_month1.setText(RecordLab.get(getActivity()).getRecord_Months(1));
			            }
			        }
			        if(null != record_month0){
			            if(RecordLab.get(getActivity()).getRecord_Months(0).equals("")){
			                Calendar cal = Calendar.getInstance();
			                cal.add(Calendar.MONTH,-3);

			                record_month0.setText(DateFormat.format("yyyy-MM",cal.getTime()));
			            }else{
			                record_month0.setText(RecordLab.get(getActivity()).getRecord_Months(0));
			            }
			        }

			        if(null != record_month3_total){
			            record_month3_total.setText(String.valueOf(
			                    RecordLab.get(getActivity()).getTotal_month(3)
			            ));
			        }
			        if(null != record_month2_total){
			            record_month2_total.setText(String.valueOf(
			                    RecordLab.get(getActivity()).getTotal_month(2)
			            ));
			        }
			        if(null != record_month1_total){
			            record_month1_total.setText(String.valueOf(
			                    RecordLab.get(getActivity()).getTotal_month(1)
			            ));
			        }
			        if(null != record_month0_total){
			            record_month0_total.setText(String.valueOf(
			                    RecordLab.get(getActivity()).getTotal_month(0)
			            ));
			        }
			    }
			}
			```

		* 在RecordActivity的ViewPager中添加这个Fragment

			```java
			...
			Fragment fragment0 = mRecord_fragment;
	        Fragment fragment1 = mRecordListFragment;
	        Fragment fragment2 = mFragment_total_month;

	        mFragmentList.add(fragment0);
	        mFragmentList.add(fragment1);
	        mFragmentList.add(fragment2);
			...
			case 2:
                setTitle(R.string.total_month_title);
                mFragment_total_month.updateTextView();
                break;
            default:
			...
			```

* step10
	* 发现bug: 当滑动到列表时,程序崩溃;
	* 本地存储数据,开启时,读取数据;
	* bug修复
		* setText里不能是数字,必须换成CharSequence或者String;
	* 本地存储数据		
		* 首先在Record.java中实现Record类的JSON序列化功能和接受JSONObject对象的构造方法

			```java
			private static final String JSON_DATE = "date";
		    private static final String JSON_BREAKFAST = "breakfast";
		    private static final String JSON_LUNCH = "lunch";
		    private static final String JSON_DINNER = "dinner";

		    public JSONObject toJSON() throws JSONException {
		        JSONObject json = new JSONObject();

		        json.put(JSON_DATE,Integer.parseInt(toString()));
		        json.put(JSON_BREAKFAST,mBreakfast);
		        json.put(JSON_LUNCH,mLunch);
		        json.put(JSON_DINNER,mDinner);

		        return json;
		    }

			public Record(JSONObject json) throws JSONException, ParseException {
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		        mDate =  df.parse(json.getString(JSON_DATE));
		        mBreakfast = json.getInt(JSON_BREAKFAST);
		        mLunch = json.getInt(JSON_LUNCH);
		        mDinner = json.getInt(JSON_DINNER);
		    }
			```

		* 然后创建新类RecordJSONSerializer.java用于创建和解析json数据

			```java
			public class RecordJSONSerializer extends Object {
			    private Context mContext;
			    private String mFilename;

			    public RecordJSONSerializer(Context c, String f){
			        mContext = c;
			        mFilename = f;
			    }

			    public void saveRecords(ArrayList<Record> records)
			        throws JSONException, IOException{
			        //Build an array in JSON
			        JSONArray array = new JSONArray();
			        for(Record record : records){
			            array.put(record.toJSON());
			        }

			        //Write the file to disk
			        Writer writer = null;
			        try{
			            OutputStream out = mContext
			                    .openFileOutput(mFilename, Context.MODE_PRIVATE);
			            writer = new OutputStreamWriter(out);
			            writer.write(array.toString());
			        }finally {
			            if(null != writer){
			                writer.close();
			            }
			        }
			    }

			    public ArrayList<Record> loadRecords()
			            throws JSONException, IOException, ParseException{
			        ArrayList<Record> records = new ArrayList<Record>();
			        BufferedReader reader = null;
			        try{
			            //open and read the file into a StringBuilder
			            InputStream in = mContext.openFileInput(mFilename);
			            reader = new BufferedReader(new InputStreamReader(in));
			            StringBuilder jsonString = new StringBuilder();
			            String line = null;
			            while(null != (line = reader.readLine())){
			                // Line breaks are omitted and irrelevant
			                jsonString.append(line);
			            }
			            // Parse the JSON using JSONTokener
			            JSONArray array = (JSONArray)(new JSONTokener(jsonString.toString()).nextValue());
			            // Build the array of records from JSONObjects
			            for(int i = 0; i < array.length(); ++ i){
			                records.add(new Record(array.getJSONObject(i)));
			            }
			        }catch(FileNotFoundException e){
			            // Ignore this one; it happens when starting fresh
			        }finally{
			            if(null != reader){
			                reader.close();
			            }
			        }
			        return records;
			    }
			}
			```

		* 在RecordLab中读取数据并实现保存方法

			```java
			private static final String FILENAME = "records.json";
		    private RecordJSONSerializer mSerializer;

		    private RecordLab(Context appContext){
		        mAppContext = appContext;
		        total_month = new int[4];
		        record_months = new String[4];
		        mSerializer = new RecordJSONSerializer(mAppContext,FILENAME);
		        try{
		            mRecords = mSerializer.loadRecords();
		        }catch(Exception e){
		            mRecords = new ArrayList<Record>();
		        }

				for(int i = 0; i < total_month.length; ++ i){
		            total_month[i] = 0;
		            record_months[i] = "";
		        }
		        updateRecords();
		    }

		    public boolean saveRecords(){
		        try{
		            mSerializer.saveRecords(mRecords);
		            return true;
		        }catch(Exception e){
		            return false;
		        }
		    }
			```

		* 在RecordActivity的onPause()方法中保存数据

			```java
			@Override
		    protected void onPause() {
		        super.onPause();
		        RecordLab.get(getApplicationContext()).saveRecords();
		    }
			```
* step11
	* 现在大致的框架已经做好了,可以投入使用了;
	* 但是还是想要添加上下文操作添加删除功能;
	* 想要添加拍照记录功能;
	* 想要添加可自定义背景图片功能;
	* 想要打开app的时候,能有一张自定义图片显示一会儿;
	* 想要UI美化一点;
	* 先实现上下文操作模式
		* 首先创建用于上下文的菜单资源文件menu/record_list_item_context.xml

			```xml
			<?xml version="1.0" encoding="utf-8"?>
			<menu xmlns:android="http://schemas.android.com/apk/res/android">
			<item
			    android:id="@+id/menu_item_delete_record"
			    android:icon="@android:drawable/ic_menu_delete"
			    android:title="@string/delete_record"
			    />
			</menu>
			```

		* 重写RecordListFragment.java的onCreateView方法

			```java
			@Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                             Bundle savedInstanceState) {
		        View v = super.onCreateView(inflater, container, savedInstanceState);
		        if(null != v.findViewById(android.R.id.list)){
		            ListView listView = (ListView)v.findViewById(android.R.id.list);
		            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
		                // Use contextual action bar on device
		                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		                listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
		                    @Override
		                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

		                    }

		                    @Override
		                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		                        MenuInflater inflater = mode.getMenuInflater();
		                        inflater.inflate(R.menu.record_list_item_context,menu);
		                        return true;
		                    }

		                    @Override
		                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		                        return false;
		                    }

		                    @Override
		                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		                        switch (item.getItemId()){
		                            case R.id.menu_item_delete_record:
		                                for(int i = adapter.getCount() - 1; i >= 0; -- i){
		                                    if(getListView().isItemChecked(i)){
		                                        RecordLab.get(getActivity()).deleteRecord(
		                                                adapter.getItem(i)
		                                        );
		                                    }
		                                }
		                                mode.finish();
		                                refreshData();
		                                return true;
		                            default:
		                                return false;
		                        }
		                    }

		                    @Override
		                    public void onDestroyActionMode(ActionMode mode) {

		                    }
		                });
		            }
		        }
		        return v;
		    }
			```

		* 为RecordLab.java增添deleteRecord方法

			```java
			public void deleteRecord(Record record)
		    {
		        if(!mRecords.isEmpty()) {
		            for(int i = mRecords.size() - 1; i >= 0; -- i){
		                if(mRecords.get(i).toString().equals(record.toString())){
		                    mRecords.remove(i);
		                    break;
		                }
		            }
		        }
		    }
			```

		* 改变已选中视图的显示背景
			* 新建drawable/background_activated.xml

				```xml
				<?xml version="1.0" encoding="utf-8"?>
				<selector xmlns:android="http://schemas.android.com/apk/res/android">
				    <item
				        android:state_activated="true"
				        android:drawable="@android:color/darker_gray"
				        />
				</selector>
				```

			* 为list_item_record.xml添加background

				```xml
				<?xml version="1.0" encoding="utf-8"?>
				<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
				    android:layout_width="match_parent"
				    android:layout_height="match_parent"
				    android:background="@drawable/background_activated">
					...
				</RelativeLayout>
				```
	* 为RecordActivity添加底部导航小圆点
		* 首先去网上或者github上下载两个圆点图,一个用于未选中状态,一个用于选中状态;
		* 把这两个圆点图放在drawable下面;
		* 在要添加导航小圆点的视图xml中添加LinearLayout组件

			```xml
			<RelativeLayout
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:layout_alignParentBottom="true"
		        android:layout_centerHorizontal="true"
		        android:layout_marginBottom="45dip" >
		        <LinearLayout
		            android:orientation="horizontal"
		            android:layout_width="wrap_content"
		            android:layout_height="wrap_content"
		            android:id="@+id/dots"
		            />
		    </RelativeLayout>
			```

		* 然后在RecordActivity中添加一个`List<ImageView>`变量,用于显示小圆点;
		* 接着为LinearLayout添加imageView元素;
		* 最后在onPageSelected中为imageView选择相应的圆点图即可

			```java
			private List<ImageView> dots;

			public void addDots() {
		        dots = new ArrayList<>();
		        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.dots);

		        for (int i = 0; i < mFragmentList.size(); i++) {
		            ImageView dot = new ImageView(this);
		            dot.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dot_normal));

		            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		                    LinearLayout.LayoutParams.WRAP_CONTENT,
		                    LinearLayout.LayoutParams.WRAP_CONTENT
		            );
		            dotsLayout.addView(dot, params);

		            dots.add(dot);
		        }
		    }

			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
	            @Override
	            public void onPageSelected(int position) {
	                switch(position){
	                    case 0:
	                        ...
	                        dots.get(0).setImageResource(R.drawable.ic_dot_selected);
	                        dots.get(1).setImageResource(R.drawable.ic_dot_normal);
	                        dots.get(2).setImageResource(R.drawable.ic_dot_normal);
	                        break;
	                    case 1:
	                        ...
	                        dots.get(0).setImageResource(R.drawable.ic_dot_normal);
	                        dots.get(1).setImageResource(R.drawable.ic_dot_selected);
	                        dots.get(2).setImageResource(R.drawable.ic_dot_normal);
	                        break;
	                    case 2:
	                        ...
	                        dots.get(0).setImageResource(R.drawable.ic_dot_normal);
	                        dots.get(1).setImageResource(R.drawable.ic_dot_normal);
	                        dots.get(2).setImageResource(R.drawable.ic_dot_selected);
	                        break;
	                    default:
	                }
	            }
	        });
			```
	* 为总记录生成图表
		* 思路
			* 生成一个分组条形图和一个折线图;
			* 分组条形图显示记录的四个月的总消费情况,每组包含
				* 当月总消费;
				* 当月三餐消费;
				* 当月其他消费;
			* 折线图每条线显示当月每天的总消费情况;
		* 使用MPAndroidChart库<https://github.com/PhilJay/MPAndroidChart>
		* 首先在android studio里为MPAndroidChart库添加依赖项
			* 在project层的build.gradle中添加

				```gradle
				allprojects {
				    repositories {
				        maven { url "https://jitpack.io" }
				    }
				}
				```

			* 在app层的build.gradle中添加

				```gradle
				dependencies {
					compile 'com.github.PhilJay:MPAndroidChart:v3.0.0'
				}
				```

		* 先做一个简单的条形图试验
			* 为你的BarChart生成视图
				* xml

					```xml
				    <com.github.mikephil.charting.charts.BarChart
				        android:id="@+id/chart0"
				        android:layout_width="match_parent"
				        android:layout_height="match_parent"
				        />
					```

				* java

					```java
					BarChart mBarChart = (BarChart) findViewById(R.id.chart0);
					```

			* 添加数据

				```java
				List<BarEntry> entries = new ArrayList<>();
			        entries.add(new BarEntry(0f, 30f));
			        entries.add(new BarEntry(1f, 80f));
			        entries.add(new BarEntry(2f, 60f));
			        entries.add(new BarEntry(3f, 50f));
			        // gap of 2f
			        entries.add(new BarEntry(5f, 70f));
			        entries.add(new BarEntry(6f, 60f));

		        BarDataSet set = new BarDataSet(entries, "total");

		        BarData data = new BarData(set);
		        data.setBarWidth(0.9f); // set custom bar width
		        mBarChart.setPinchZoom(false);
		        mBarChart.setDrawGridBackground(false);
		        mBarChart.setDrawBarShadow(false);
		        mBarChart.setDrawValueAboveBar(true);
		        mBarChart.setData(data);
		        mBarChart.setFitBars(true); // make the x-axis fit exactly all bars
		        mBarChart.invalidate(); // refresh
				```
		* 开始做分组条形图
			* 添加数据

				```java
				List<BarEntry> entries0 = new ArrayList<>();
		        List<BarEntry> entries1 = new ArrayList<>();
		        List<BarEntry> entries2 = new ArrayList<>();
		        for(int i = 0; i< 4; ++ i){
		            entries0.add(new BarEntry(i,RecordLab.get(getApplicationContext()).getTotal_month(i)));
		            entries1.add(new BarEntry(i,
		                    RecordLab.get(getApplicationContext()).getTotal_month(i) -
		                    RecordLab.get(getApplicationContext()).getTotal_others(i)));
		            entries2.add(new BarEntry(i,RecordLab.get(getApplicationContext()).getTotal_others(i)));
		        }

		        BarDataSet set0 = new BarDataSet(entries0, "total");
		        BarDataSet set1 = new BarDataSet(entries1, "meal");
		        BarDataSet set2 = new BarDataSet(entries2, "others");
				```

			* 设置组间距离,条形图间距,条形图宽度(需要计算)

				![grouped_barChart](https://github.com/PhilJay/MPAndroidChart/blob/master/screenshots/grouped_barchart_wiki.png)

				```java
				float groupSpace = 0.07f;
		        float barSpace = 0.03f; // x3 dataset
		        float barWidth = 0.28f; // x3 dataset
		        // (0.03 + 0.28) * 3 + 0.07 = 1.00 -> interval per "group"

		        BarData data = new BarData(set0,set1,set2);
		        data.setBarWidth(barWidth);
		        data.setValueFormatter(new IValueFormatter(){
		            @Override
		            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
		                                            ViewPortHandler viewPortHandler) {
		                return String.valueOf(Math.round(value));
		            }
		        });
		        mBarChart.setData(data);
		        mBarChart.groupBars(0,groupSpace,barSpace);
		        mBarChart.invalidate(); // refresh
				```

			* 设置横纵轴(其中MyXAxisValueFormatter用于格式化横轴显示值)

				```java
				IAxisValueFormatter xAxisFormatter = new MyXAxisValueFormatter(getApplicationContext());

		        XAxis xAxis= mBarChart.getXAxis();
		        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		        xAxis.setDrawGridLines(true);
		        xAxis.setAxisMinimum(0);
		        xAxis.setAxisMaximum(4);
		        xAxis.setGranularity(1f);
		        xAxis.setLabelCount(5);
		        xAxis.setValueFormatter(xAxisFormatter);
		        xAxis.setCenterAxisLabels(true);

		        YAxis leftAxis = mBarChart.getAxisLeft();
		        leftAxis.setDrawGridLines(true); // no grid lines
		        mBarChart.getAxisRight().setEnabled(false); // no right axis
				```

			* 为各个条形图设置颜色
				* 方法一

					```java
					set0.setColors(ColorTemplate.COLORFUL_COLORS);
			        set1.setColors(ColorTemplate.COLORFUL_COLORS);
			        set2.setColors(ColorTemplate.COLORFUL_COLORS);
					```

				* 方法二(各原色数值可以在<http://www.w3schools.com/colors/colors_rgb.asp>根据喜好得到)

					```java
					set0.setColor(Color.rgb(104, 241, 175));
					...
					```

			* 为图表设置动画

				```java
				mBarChart.animateXY(1000,1000);
				```

			* 更多设置见<https://github.com/PhilJay/MPAndroidChart/wiki>

		* 开始做折线图
			* xml

				```xml
				<com.github.mikephil.charting.charts.LineChart
		        android:id="@+id/chart1"
		        android:layout_width="match_parent"
		        android:layout_height="wrap_content"
		        />
				```

			* 发现要在一个activity上显示多个chart需要使用listView,明天再写



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
