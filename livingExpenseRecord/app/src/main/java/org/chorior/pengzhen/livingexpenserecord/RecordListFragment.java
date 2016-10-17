package org.chorior.pengzhen.livingexpenserecord;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pengzhen on 13/10/16.
 */

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
        getActivity().setTitle(R.string.record_list_title);

        RecordAdapter adapter = new RecordAdapter(mRecords);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Record record = ((RecordAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(),finalRecordActivity.class);
        i.putExtra(finalRecordFragment.EXTRA_RECORD_DATE,record.getmDate());
        startActivity(i);
    }
}
