package android.example.com.productsinventory;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.com.productsinventory.data.Product;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class AddProductDialog extends DialogFragment{

    private ImageView ivProductName;
    private Button btnSelectImage;
    View view;
    private static int RESULT_LOAD_IMAGE = 1;


    public AddProductDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        view = getActivity().getLayoutInflater().inflate(R.layout.new_product, null);
        btnSelectImage = (Button) view.findViewById(R.id.btnProductImage);
        ivProductName = (ImageView) view.findViewById(R.id.ivProductImage);

        builder.setView(view);
        builder.setTitle("Add new product");

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getDialog().dismiss();
                    }
                }
        );

        return builder.create();
    }


    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean closeDialog = false;
                    TextView tvErrorMsg = (TextView) view.findViewById(R.id.errorMessage);
                    EditText etProductName = (EditText) view.findViewById(R.id.etProductName);
                    EditText etProductPrice = (EditText) view.findViewById(R.id.etProductPrice);
                    EditText etProductQuantity = (EditText) view.findViewById(R.id.etProductQuantity);
                    EditText etProductSupplier = (EditText) view.findViewById(R.id.etProductSupplier);

                    String productNameStr = etProductName.getText().toString();
                    String productPriceStr = etProductPrice.getText().toString();
                    String productQuantityStr = etProductQuantity.getText().toString();
                    String productSupplierStr = etProductSupplier.getText().toString();

                    if(!productNameStr.equals("")){

                        double productPrice = (!productPriceStr.equals(""))? Double.parseDouble(productPriceStr): 0.0;
                        int productQuantity = (!productQuantityStr.equals(""))? Integer.parseInt(productQuantityStr): 0;

                        Bitmap bitmap = ((BitmapDrawable)ivProductName.getDrawable()).getBitmap();

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap .compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] img = bos.toByteArray();

                        Product productToAdd = new Product(productNameStr, productPrice, productQuantity, productSupplierStr, img);

                        MainActivity.insertValue(productToAdd);

                        closeDialog = true;
                    }
                    else{
                        tvErrorMsg.setText("Insert product name");
                    }

                    if(closeDialog)
                        dismiss();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri selectedImage = data.getData();
            ivProductName.setImageURI(selectedImage);
        }

    }

}