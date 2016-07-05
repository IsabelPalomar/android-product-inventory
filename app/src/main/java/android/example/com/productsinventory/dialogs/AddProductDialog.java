package android.example.com.productsinventory.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.activities.MainActivity;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.utils.Constants;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
    private static int RESULT_LOAD_IMAGE = 1;
    Resources res;
    View view;

    public AddProductDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        res = getResources();

        view = getActivity().getLayoutInflater().inflate(R.layout.new_product, null);
        Button btnSelectImage = (Button) view.findViewById(R.id.btnProductImage);
        ivProductName = (ImageView) view.findViewById(R.id.ivProductImage);

        builder.setView(view);
        builder.setTitle(res.getString(R.string.app_message_addProduct_title));

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            }
        });

        builder.setPositiveButton(res.getString(R.string.app_message_save),
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                    }
                });

        builder.setNegativeButton(res.getString(R.string.app_message_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getDialog().dismiss();
                    }
                }
        );

        return builder.create();
    }

    /**
     * Validate the product information and save it in Database
     */
    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton =  d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean validRecords = true;
                    String validationMsg = res.getString(R.string.app_message_addProduct_validation);
                    int invalidCounter = 0;
                    TextView tvErrorMsg = (TextView) view.findViewById(R.id.errorMessage);
                    EditText etProductName = (EditText) view.findViewById(R.id.etProductName);
                    EditText etProductPrice = (EditText) view.findViewById(R.id.etProductPrice);
                    EditText etProductQuantity = (EditText) view.findViewById(R.id.etProductQuantity);
                    EditText etProductSupplier = (EditText) view.findViewById(R.id.etProductSupplier);

                    String productNameStr = etProductName.getText().toString();
                    String productPriceStr = etProductPrice.getText().toString();
                    String productQuantityStr = etProductQuantity.getText().toString();
                    String productSupplierStr = etProductSupplier.getText().toString();


                    if(productNameStr.equals(Constants.BLANK_VALUE)){
                        validationMsg += res.getString(R.string.app_message_addProduct_name);
                        validRecords = false;

                        invalidCounter++;
                    }
                    if(productPriceStr.equals(Constants.BLANK_VALUE)) {

                        if (invalidCounter > 0)
                            validationMsg += Constants.COMMA;

                        validationMsg += res.getString(R.string.app_message_addProduct_price);
                        validRecords = false;
                        invalidCounter++;
                    }
                    if(productQuantityStr.equals(Constants.BLANK_VALUE)){

                        if(invalidCounter > 0)
                            validationMsg += Constants.COMMA;

                        validationMsg += res.getString(R.string.app_message_addProduct_quantity);
                        validRecords = false;
                        invalidCounter++;
                    }
                    if(productSupplierStr.equals(Constants.BLANK_VALUE)){

                        if(invalidCounter > 0)
                            validationMsg += Constants.COMMA;

                        validationMsg += res.getString(R.string.app_message_addProduct_supplier);
                        validRecords = false;
                    }

                    if(validRecords){

                        double productPrice = (!productPriceStr.equals(Constants.BLANK_VALUE))? Double.parseDouble(productPriceStr): 0.0;
                        int productQuantity = (!productQuantityStr.equals(Constants.BLANK_VALUE))? Integer.parseInt(productQuantityStr): 5;
                        String productSupplier = (!productSupplierStr.equals(Constants.BLANK_VALUE))? productSupplierStr: Constants.EMAIL_DEFAULT;

                        Bitmap bitmap = ((BitmapDrawable)ivProductName.getDrawable()).getBitmap();
                        Bitmap scaledBitmap = scaleBitmap(bitmap, 120, 120);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] img = bos.toByteArray();

                        Product productToAdd = new Product(productNameStr, productPrice, productQuantity, productSupplier, img);

                        MainActivity.insertValue(productToAdd);
                        dismiss();
                    }
                    else{
                        tvErrorMsg.setText(validationMsg);
                    }
                }
            });
        }
    }

    /**
     * Scale image
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if (bitmapToScale == null)
            return null;
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Matrix matrix = new Matrix();

        matrix.postScale(newWidth / width, newHeight / height);

        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    /**
     * Display the image selected by the user in the ImageView
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri selectedImage = data.getData();
            ivProductName.setImageURI(selectedImage);
        }

    }
}