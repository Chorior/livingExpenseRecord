# 本地数据的存储和读取
---
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
