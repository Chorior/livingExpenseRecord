# 使用ListView
---
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
* RecordListFragment.java(simple_list_item_1是Android SDK提供的预定义布局资源)

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

* 默认的`ArrayAdapter<T>.getView(...)`实现依赖于toString()方法,所以需要覆盖Record.toString()方法

	```java
	@Override
	public String toString() {
		return DateFormat.format("MMMM dd",mDate).toString();
	}
	```
* 当数据更新时,需要手动进行ListView的刷新,否则自行刷新会很慢

	```java
	adapter.notifyDataSetChanged();
	```

* 响应列表项点击事件(自行处理),RecordListFragment.java

	```java
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//Record r = (Record)(getListAdapter()).getItem(position);
		//wait to update
	}
	```

* 预定义的列表项显示的可能不是我们想要的信息,所以需要定制列表项
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

	* 构建自己的ListView适配器RecordAdapter并使用

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

* 最后,空的list视图一点也不好看,所以为空list视图设置空视图

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
