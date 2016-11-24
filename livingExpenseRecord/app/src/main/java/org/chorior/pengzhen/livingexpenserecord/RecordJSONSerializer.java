package org.chorior.pengzhen.livingexpenserecord;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by pengzhen on 21/10/16.
 */

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

    public void saveNaviSelection(NaviSelection selection)
            throws JSONException, IOException{
        //Build an array in JSON
        JSONArray array = new JSONArray();
        array.put(selection.toJSON());

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

    public NaviSelection loadNaviSelection()
            throws JSONException, IOException, ParseException{
        NaviSelection selection = null;
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
            selection = new NaviSelection(array.getJSONObject(0));
        }catch(FileNotFoundException e){
            // Ignore this one; it happens when starting fresh
            selection = new NaviSelection();
        }finally{
            if(null != reader){
                reader.close();
            }
        }
        return selection;
    }
}
