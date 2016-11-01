# 为ViewPager添加底部小圆点导航
---
* 添加底部导航小圆点
	* 首先去[网上](https://www.easyicon.net/language.en/iconsearch/dot/)或者github上下载两个圆点图,一个用于未选中状态,一个用于选中状态;
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

	* 然后添加一个`List<ImageView>`变量,用于显示小圆点;
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
