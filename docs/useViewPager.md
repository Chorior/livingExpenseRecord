# 使用ViewPager
---
* 将Layout组件替换为ViewPager

	```xml
	<?xml version="1.0" encoding="utf-8"?>
	<android.support.v4.view.ViewPager
		xmlns:android="http://schemas.android.com/apk/res/android"
		android:id="@+id/viewPagerFinal"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		/>
	```

* 修改Activity.java

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
