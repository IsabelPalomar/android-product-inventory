package android.example.com.productsinventory.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.activities.DetailActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

public class UpdateProductQuantityDialog extends DialogFragment {

    Resources res;
    View view;
    EditText etProductQuantity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.update_product, null);
        etProductQuantity = (EditText) view.findViewById(R.id.etProductQuantity);

        //Build the Dialog
        builder.setView(view)
                .setTitle(res.getString(R.string.app_message_updateProduct_title))
                .setPositiveButton(res.getString(R.string.app_message_update), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int newQuantity = Integer.valueOf(etProductQuantity.getText().toString());
                        DetailActivity.updateProductQuantity(newQuantity);
                    }
                })
                .setNegativeButton(res.getString(R.string.app_message_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
