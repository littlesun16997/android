package fi.jamk.shoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ModifyShoppingListDialogFragment extends DialogFragment {

    // The activity that creates an instance of this dialog fragment must
    // implement this interface in order to receive event callbacks.
    // Each method passes the DialogFragment in case the host needs to query it.
    public interface DialogListener {
        void onDialogPositiveClick(DialogFragment dialog, String name, int count, double unit_price);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = context instanceof Activity ? (Activity) context : null;

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.add_list, null);
        builder.setView(dialogView)
                .setTitle("Add/Update product")
                        // Add action buttons
                .setPositiveButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editName = (EditText) dialogView.findViewById(R.id.name);
                        String name = editName.getText().toString();

                        EditText editCount = (EditText) dialogView.findViewById(R.id.count);
                        int count = Integer.valueOf(editCount.getText().toString());

                        EditText editUnit = (EditText) dialogView.findViewById(R.id.unit);
                        double unit = Double.valueOf(editUnit.getText().toString());

                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(ModifyShoppingListDialogFragment.this, name, count, unit);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(ModifyShoppingListDialogFragment.this);
                    }
                });
        return builder.create();
    }
}