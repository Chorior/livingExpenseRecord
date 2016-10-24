package org.chorior.pengzhen.livingexpenserecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by pengzhen on 24/10/16.
 */

public class CustomRecordFragment extends Fragment {
    private CustomRecord mCustomRecord;
    private EditText mTitleField;
    private EditText mCostField;
    private EditText mTextField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomRecord = new CustomRecord();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mTitleField = (EditText)v.findViewById(R.id.custom_record_title);
        mCostField = (EditText)v.findViewById(R.id.custom_record_cost);
        mTextField = (EditText)v.findViewById(R.id.custom_record_text);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_custom_record_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_save_record:
                if(0 != mTitleField.getText().length()){
                    mCustomRecord.setTitle(mTitleField.getText().toString());
                }else{
                    mCustomRecord.setTitle("");
                }
                if(0 != mCostField.getText().length()){
                    mCustomRecord.setCost(
                            Integer.parseInt(mCostField.getText().toString())
                    );
                }else{
                    mCustomRecord.setCost(0);
                }
                if(0 != mTextField.getText().length()){
                    mCustomRecord.setText(mTextField.getText().toString());
                }else{
                    mCustomRecord.setText("");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
