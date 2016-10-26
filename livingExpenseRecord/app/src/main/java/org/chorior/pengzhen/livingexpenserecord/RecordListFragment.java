package org.chorior.pengzhen.livingexpenserecord;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pengzhen on 13/10/16.
 */

public class RecordListFragment extends ListFragment {
    private ArrayList<Record> mRecords;
    private RecordAdapter adapter;

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
            dateTextView.setText(DateFormat.format("yyyy-MM-dd",record.getmDate()));

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
        adapter = new RecordAdapter(mRecords);
        setListAdapter(adapter);
    }

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
                                RecordLab.get(getActivity()).updateRecords();
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Record record = ((RecordAdapter)getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(),finalRecordActivity.class);
        i.putExtra(finalRecordFragment.EXTRA_RECORD_DATE,record.getmDate());
        startActivity(i);
    }

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

    public void refreshData(){
        if(null != adapter){
            adapter.notifyDataSetChanged();
        }
    }

}
