package android.example.com.productsinventory.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.activities.DetailActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

/**
 * Created by isabelpalomar on 7/4/16.
 */
public class UpdateProductQuantityDialog extends DialogFragment {

    View view;
    EditText etProductQuantity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getActivity().getLayoutInflater().inflate(R.layout.update_product, null);
        etProductQuantity = (EditText) view.findViewById(R.id.etProductQuantity);

        builder.setView(view);
        builder.setTitle("Update product quantity");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        int newQuantity = Integer.valueOf(etProductQuantity.getText().toString());
                        DetailActivity.updateProductQuantity(newQuantity);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
