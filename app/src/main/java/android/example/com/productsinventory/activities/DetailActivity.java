package android.example.com.productsinventory.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.data.source.ProductsContract;
import android.example.com.productsinventory.data.source.ProductsDbHelper;
import android.example.com.productsinventory.dialogs.DeleteProductDialog;
import android.example.com.productsinventory.dialogs.UpdateProductQuantityDialog;
import android.example.com.productsinventory.utils.Constants;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.example.com.productsinventory.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DetailActivity extends AppCompatActivity {

    Product product;
    static Resources res;
    static int productId;
    static ProductsDbHelper mDbHelper;
    static DetailActivity detailActivity;
    static TextView productQuantity;
    static RelativeLayout rlDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button modifyQuantity = (Button) findViewById(R.id.btnModifyQuantity);
        Button contactSupplier = (Button) findViewById(R.id.btnContactSupplier);
        rlDetail = (RelativeLayout) findViewById(R.id.rlDetail);

        Intent intent = getIntent();
        productId = intent.getIntExtra(Constants.PRODUCT_ID_EXTRA, 0);

        mDbHelper = new ProductsDbHelper(this);
        detailActivity = this;
        res = getResources();

        //Fetch the product information from DB using the productId
        populateDetailView(productId);

        //OnClick behaviour for buttons
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteProduct();
                }
            });
        }

        if (modifyQuantity != null) {
            modifyQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    openUpdateQuantityDialog();
                }
            });
        }

        if (contactSupplier != null) {
            contactSupplier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmailToSupplier();
                }
            });
        }

    }

    /**
     * Populates the Detail layout with the product information
     */
    private void populateDetailView(int productId) {

        ImageView ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        TextView productInfo = (TextView) findViewById(R.id.tvProductInfo);
        TextView productSupplier = (TextView) findViewById(R.id.tvSupplier);
        productQuantity = (TextView) findViewById(R.id.tvQuantity);

        //Get the product information from DB
        product = getProductById(productId);

        if(product != null){
            //Get image information stored and convert it to a bitmap to display in the ImageView
            byte[] productImg = product.getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(productImg, 0, productImg.length);

            //Set product values in Views
            if (ivProductImage != null)
                ivProductImage.setImageBitmap(bmp);
            if (productInfo != null)
                productInfo.setText(String.format(res.getString(R.string.app_message_productInfo), product.getName(), product.getPrice()));
            if (productQuantity != null)
                productQuantity.setText(String.format(res.getString(R.string.app_message_productQty), product.getQuantity()));
            if (productSupplier != null)
                productSupplier.setText(String.format(res.getString(R.string.app_message_productSupplier), product.getSupplier()));
        }
    }

    /**
     * Open the dialog to update the product quantity
     */
    private void openUpdateQuantityDialog() {
        UpdateProductQuantityDialog editQuantityDialog = new UpdateProductQuantityDialog();
        editQuantityDialog.show(getSupportFragmentManager(), "");
    }

    /**
     * Send an email to supplier with the product order information
     */
    private void sendEmailToSupplier() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{product.getSupplier()});
        intent.putExtra(Intent.EXTRA_SUBJECT, String.format(res.getString(R.string.app_message_productEmail), product.getSupplier()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * Open the dialog to delete the product
     */
    private void deleteProduct() {
        DeleteProductDialog editNameDialog = new DeleteProductDialog();
        editNameDialog.show(getSupportFragmentManager(), "");
    }

    /**
     * Fetch a product from DB using the productId
     * and assign the values to a Product Object
     */
    private Product getProductById(int productId) {

        Product product = null;
        Cursor c = mDbHelper.getProductById(productId);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int entryId = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID));
                String productName = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT));
                double price = c.getDouble(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRICE));
                int quantity = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY));
                String supplier = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER));
                byte[] imageByte = c.getBlob(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_IMAGE));

                product = new Product(entryId, productName, price, quantity, supplier, imageByte);

            }
        }
        if (c != null) {
            c.close();
        }

        mDbHelper.close();
        return product;
    }

    /**
     * Updates the Product Quantity in DB
     */
    public static void updateProductQuantity(int newQuantity) {
        int rowsUpdated;
        String toastMsg = "";

        try {
            rowsUpdated = mDbHelper.updateQuantityById(productId, newQuantity);
            if (rowsUpdated > 0) {
                productQuantity.setText(String.format(res.getString(R.string.app_message_productQty), newQuantity));
                toastMsg = res.getString(R.string.app_message_productQty_updated);
            }
            else{
                toastMsg = res.getString(R.string.app_message_productQty_error);
            }

            Snackbar.make(rlDetail, toastMsg, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Delete product from DB
     */
    public static void deleteCurrentProduct() {
        mDbHelper.deleteElementById(productId);
        detailActivity.finish();
    }

}
