# MPAndroidChart库的基本使用
---
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
	* 发现要在一个activity上显示多个chart需要使用listView
	* 首先给自己的activity写个layout.xml

		```xml
		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			android:layout_width="match_parent"
			android:layout_height="match_parent"
			android:orientation="vertical" >

			<ListView
				android:id="@+id/listView_chart"
				android:layout_width="match_parent"
				android:layout_height="match_parent" >
			</ListView>

		</LinearLayout>
		```

	* 由于listView需要一个通用类来保存不同的图表,所以先写个通用图表类

		```java
		public abstract class ChartItem {

			protected static final int TYPE_BARCHART = 0;
			protected static final int TYPE_LINECHART = 1;
			protected static final int TYPE_PIECHART = 2;

			protected ChartData<?> mChartData;

			public ChartItem(ChartData<?> cd) {
				this.mChartData = cd;
			}

			public abstract int getItemType();

			public abstract View getView(int position, View convertView, Context c);
		}
		```

	* 然后写自己需要的图表类
		* GroupedBarChartItem.java

			```java
			public class GroupedBarChartItem extends ChartItem {
				private Context context;
				private float FromX;
				private float groupSpace;
				private float barSpace;

				public GroupedBarChartItem(ChartData<?> cd, Context context,
										   float FromX, float groupSpace, float barSpace){
					super(cd);

					this.context = context;
					this.FromX = FromX;
					this.groupSpace = groupSpace;
					this.barSpace = barSpace;
				}

				@Override
				public int getItemType() {
					return TYPE_BARCHART;
				}

				private static class ViewHolder {
					BarChart chart;
				}

				@Override
				public View getView(int position, View convertView, Context c) {
					ViewHolder holder = null;

					if (null == convertView) {

						holder = new ViewHolder();

						convertView = LayoutInflater.from(c).inflate(
								R.layout.list_item_groupedbarchart, null);
						holder.chart = (BarChart) convertView.findViewById(R.id.GroupedBarChart);

						convertView.setTag(holder);

					} else {
						holder = (ViewHolder) convertView.getTag();
					}

					holder.chart.getDescription().setEnabled(false);
					//holder.chart.setPinchZoom(false);
					holder.chart.setDrawBarShadow(false);
					holder.chart.setDrawValueAboveBar(true);

					IAxisValueFormatter xAxisFormatter = new MyXAxisValueFormatter(context);

					XAxis xAxis= holder.chart.getXAxis();
					xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
					xAxis.setDrawGridLines(true);
					xAxis.setAxisMinimum(0);
					xAxis.setAxisMaximum(4);
					xAxis.setGranularity(1f);
					xAxis.setLabelCount(5);
					xAxis.setValueFormatter(xAxisFormatter);
					xAxis.setCenterAxisLabels(true);

					YAxis leftAxis = holder.chart.getAxisLeft();
					leftAxis.setDrawGridLines(true); // no grid lines
					holder.chart.getAxisRight().setEnabled(false); // no right axis

					// set data
					holder.chart.setData((BarData) mChartData);

					holder.chart.animateXY(1000,1000);
					holder.chart.groupBars(FromX,groupSpace,barSpace);
					holder.chart.invalidate(); // refresh

					return convertView;
				}
			}
			```

		* LineChartItem.java

			```java
			public class LineChartItem extends ChartItem {
				private MyLineChartXAxisValueFormatter valueFormatter;

				public LineChartItem(ChartData<?> cd, MyLineChartXAxisValueFormatter valueFormatter) {
					super(cd);

					this.valueFormatter = valueFormatter;
				}

				@Override
				public int getItemType() {
					return TYPE_LINECHART;
				}

				private static class ViewHolder {
					LineChart chart;
				}

				@Override
				public View getView(int position, View convertView, Context c) {
					ViewHolder holder = null;

					if (null == convertView) {

						holder = new ViewHolder();

						convertView = LayoutInflater.from(c).inflate(
								R.layout.list_item_linechart, null);
						holder.chart = (LineChart) convertView.findViewById(R.id.lineChart);

						convertView.setTag(holder);

					} else {
						holder = (ViewHolder) convertView.getTag();
					}

					holder.chart.getDescription().setEnabled(false);

					XAxis xAxis = holder.chart.getXAxis();
					xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
					xAxis.setDrawGridLines(false);
					xAxis.setDrawAxisLine(true);
					xAxis.setAxisMinimum(0f);
					xAxis.setGranularity(1f);
					xAxis.setValueFormatter(valueFormatter);

					YAxis leftAxis = holder.chart.getAxisLeft();
					leftAxis.setDrawGridLines(true); // no grid lines
					leftAxis.setLabelCount(5);
					holder.chart.getAxisRight().setEnabled(false); // no right axis

					MyMarkerView mv = new MyMarkerView(valueFormatter.getMyContext(),
							R.layout.marker_view,
							valueFormatter.getMyIndex());
					mv.setChartView(holder.chart);
					holder.chart.setMarker(mv);

					// set data
					holder.chart.setData((LineData) mChartData);

					holder.chart.animateXY(1000, 1000);
					holder.chart.invalidate(); // refresh

					return convertView;
				}
			}
			```

		* 其中
			* GroupedBarChart中的MyXAxisValueFormatter和LineChartItem中的MyLineChartXAxisValueFormatter用于改变横坐标显示;
			* LineChartItem中的MyMarkerView用于高亮点击时,出现信息框,显示关于该点的相应信息
				* layout.xml(其中总的layout_width和layout_height根据显示的信息内容自行定制,可以先调整为wrap_content);

					```xml
					<?xml version="1.0" encoding="utf-8"?>
					<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
						android:layout_width="100dp"
						android:layout_height="72dp"
						android:background="@drawable/marker1" >

						<TextView
							android:id="@+id/tvContent"
							android:layout_width="wrap_content"
							android:layout_height="wrap_content"
							android:layout_centerHorizontal="true"
							android:layout_marginTop="5dp"
							android:layout_marginLeft="5dp"
							android:layout_marginRight="5dp"
							android:text=""
							android:textSize="12sp"
							android:textColor="@android:color/white"
							android:ellipsize="end"
							android:maxLines="3"
							android:textAppearance="?android:attr/textAppearanceSmall" />

					</RelativeLayout>
					```

				* MyMarkerView.java

					```java
					public class MyMarkerView extends MarkerView {
						private Context context;
						private int index;
						private TextView tvContent;

						public MyMarkerView(Context context, int layoutResource, int index){
							super(context,layoutResource);

							this.context = context;
							this.index = index;
							tvContent = (TextView)findViewById(R.id.tvContent);
						}

						@Override
						public MPPointF getOffset() {
							return new MPPointF(-(getWidth()/2),-getHeight());
						}

						@Override
						public void refreshContent(Entry e, Highlight highlight) {
							ArrayList<Record> records = RecordLab.get(context).getmRecords_month(index);

							int x = (int)highlight.getX();
							int total = records.get(records.size() - 1 - x).getmTotal_today();
							int others = records.get(records.size() - 1 - x).getmOthers();
							int meal = total - others;

							tvContent.setText("total: " + String.valueOf(total) +
												"\nmeal: " + String.valueOf(meal) +
												"\nothers: " + String.valueOf(others));

							super.refreshContent(e,highlight);
						}
					}
					```

	* 接着为自己的图表添加数据

		```java
		private static class GroupedChartParameters{
			float FromX;
			float groupSpace;
			float barSpace;
			float barWidth;
		}

		private BarData generateDataGroupedBar(GroupedChartParameters gcp ) {
			Utils.init(getApplicationContext());

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

			set0.setColor(ColorTemplate.JOYFUL_COLORS[0]);
			set1.setColor(ColorTemplate.JOYFUL_COLORS[1]);
			set2.setColor(ColorTemplate.JOYFUL_COLORS[2 ]);

			gcp.FromX = 0;
			gcp.groupSpace = 0.07f;
			gcp.barSpace = 0.03f; // x3 dataset
			gcp.barWidth = 0.28f; // x3 dataset
			// (0.03 + 0.28) * 3 + 0.07 = 1.00 -> interval per "group"

			BarData data = new BarData(set0,set1,set2);
			data.setBarWidth(gcp.barWidth);
			data.setValueFormatter(new IValueFormatter(){
				@Override
				public String getFormattedValue(float value, Entry entry, int dataSetIndex,
												ViewPortHandler viewPortHandler) {
					return String.valueOf(Math.round(value));
				}
			});

			return data;
		}

		private LineData generateDataLine(ArrayList<Record> mRecords_month, int index) {
			Utils.init(getApplicationContext());

			List<Entry> entries = new ArrayList<>();

			for(int i = 0; i < mRecords_month.size(); ++ i){
				entries.add(new Entry(
						i,
						mRecords_month.get(mRecords_month.size() - 1 - i).getmTotal_today()
				));
			}

			List<ILineDataSet> dataSets = new ArrayList<>();
			if(!entries.isEmpty()){
				LineDataSet set = new LineDataSet(entries,
						mRecords_month.get(0).getYearAndMonthDate());
				set.setColor(ColorTemplate.VORDIPLOM_COLORS[index]);
				set.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[index]);

				dataSets.add(set);
			}

			LineData data = new LineData(dataSets);
			data.setValueFormatter(new IValueFormatter(){
				@Override
				public String getFormattedValue(float value, Entry entry, int dataSetIndex,
												ViewPortHandler viewPortHandler) {
					return String.valueOf(Math.round(value));
				}
			});

			return data;
		}
		```

	* 设置自己的适配器

		```java
		/** adapter that supports 3 different item types */
		private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

			public ChartDataAdapter(Context context, List<ChartItem> objects) {
				super(context, 0, objects);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return getItem(position).getView(position, convertView, getContext());
			}

			@Override
			public int getItemViewType(int position) {
				// return the views type
				return getItem(position).getItemType();
			}

			@Override
			public int getViewTypeCount() {
				return 3; // we have 3 different item-types
			}
		}
		```

	* 最后设置ListView

		```java
		ListView lv = (ListView) findViewById(R.id.listView_chart);

		List<ChartItem> list = new ArrayList<>();

		GroupedChartParameters gcp = new GroupedChartParameters();
		BarData barData = generateDataGroupedBar(gcp);
		list.add(new GroupedBarChartItem(barData,getApplicationContext(),
				gcp.FromX,gcp.groupSpace,gcp.barSpace));

		for(int i = 0; i < 4; ++ i){
			if(1 < RecordLab.get(getApplication()).getmRecords_month(i).size()){
				list.add(new LineChartItem(
						generateDataLine(RecordLab.get(getApplication()).getmRecords_month(i),i),
						new MyLineChartXAxisValueFormatter(getApplicationContext(),i)
				));
			}
		}

		ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
		lv.setAdapter(cda);
		```
