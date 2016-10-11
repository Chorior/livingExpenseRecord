package org.chorior.pengzhen.livingexpenserecord;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by pengzhen on 11/10/16.
 */

public class RecordFragment extends Fragment {
    private Record mRecord;
    private EditText mBreakfastField;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mRecord = new Record();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_record,parent,false);

        mBreakfastField = (EditText)v.findViewById(R.id.record_breakfast);
        mBreakfastField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // wait to update
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mRecord.setmBreakfast(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // wait to update
            }
        });
        return v;
    }
}
