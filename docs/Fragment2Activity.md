# 从fragment启动Activity并传递信息
---
* 实现从RecordListFragment.java中启动finalRecordActivity,这里一定要注意
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

		* 最后RecordListFragment.java中添加Date信息的方法与方法一相同;
