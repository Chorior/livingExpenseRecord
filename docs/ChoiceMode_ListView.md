# ListView的上下文操作实现批量删除
---
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

* 重写onCreateView方法

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
