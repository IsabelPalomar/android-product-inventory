package android.example.com.productsinventory.dialogs;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.activities.DetailActivity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeleteProductDialog extends DialogFragment {

    Resources res;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(res.getString(R.string.app_message_deleteProduct_title))
                .setPositiveButton(res.getString(R.string.app_message_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.deleteCurrentProduct();
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