# 添加向上导航菜单
---
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
				* 为mainActivity添加launchMode属性为[singleTop](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0520/2897.html);
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
