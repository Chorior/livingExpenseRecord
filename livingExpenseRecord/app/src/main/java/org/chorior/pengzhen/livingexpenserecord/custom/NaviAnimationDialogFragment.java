package org.chorior.pengzhen.livingexpenserecord.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.chorior.pengzhen.livingexpenserecord.R;
import org.chorior.pengzhen.livingexpenserecord.RecordActivity;

/**
 * Created by pengzhen on 24/11/16.
 */

public class NaviAnimationDialogFragment extends DialogFragment {
    public static final String EXTRA_ANIMATION =
            "org.chorior.pengzhen.livingexpenserecord.animation";
    private int checkedItem;

    public static NaviAnimationDialogFragment newInstance(int checkedItem){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ANIMATION,checkedItem);

        NaviAnimationDialogFragment fragment = new NaviAnimationDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        checkedItem = (int)getArguments().getSerializable(EXTRA_ANIMATION);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.animation)
                .setSingleChoiceItems(R.array.translation_effect, checkedItem,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkedItem = which;
                                getArguments().putSerializable(EXTRA_ANIMATION,checkedItem);
                            }
                        })
                .setPositiveButton(R.string.animation_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((RecordActivity)getActivity()).setAnimation(checkedItem);
                    }
                })
                .setNegativeButton(R.string.animation_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
