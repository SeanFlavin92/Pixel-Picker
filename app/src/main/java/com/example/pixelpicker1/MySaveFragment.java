package com.example.pixelpicker1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.widget.EditText;

/**
 * Created by Sean on 21/07/2015.
 */
public class MySaveFragment extends DialogFragment
{
    public static MySaveFragment newInstance(String messageTitle) {
        MySaveFragment frag = new MySaveFragment();
        Bundle args = new Bundle();
        args.putString("title", messageTitle);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final String messageTitle = getArguments().getString("title");
        final EditText editText = new EditText(this.getActivity()) ;

        return new AlertDialog.Builder(getActivity())
                .setTitle(messageTitle)
                .setView(editText)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!editText.getText().toString().isEmpty()) {
                                    ((MainActivity) getActivity()).doPositiveClick(editText.getText().toString());
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();
    }
}
