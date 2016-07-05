package android.example.com.productsinventory.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.data.source.ProductsContract;
import android.example.com.productsinventory.data.source.ProductsDbHelper;
import android.example.com.productsinventory.dialogs.DeleteProductDialog;
import android.example.com.productsinventory.dialogs.UpdateProductQuantityDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.example.com.productsinventory.R;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    static ProductsDbHelper mDbHelper;
    static int productId;
    static Context context;
    static DetailActivity detailActivity;
    Product product;
    static TextView productQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button modifyQuantity = (Button) findViewById(R.id.btnModifyQuantity);
        Button contactSuplier = (Button) findViewById(R.id.btnContactSupplier);

        Intent intent = getIntent();
        mDbHelper = new ProductsDbHelper(this);
        context = getApplicationContext();
        detailActivity = this;

        productId  = intent.getIntExtra("productId", 0);

        populateDetailView(productId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProduct();
            }
        });

        modifyQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openUpdateQuantityDialog();
            }
        });

        contactSuplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmailToSuplier();
            }
        });

    }

    private void openUpdateQuantityDialog() {
        FragmentManager fm = getSupportFragmentManager();
        UpdateProductQuantityDialog editNameDialog = new UpdateProductQuantityDialog();
        editNameDialog.show(fm, "");

    }

    private void sendEmailToSuplier() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, product.getSupplier());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Product Order - " + product.getName());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void deleteProduct() {
        FragmentManager fm = getSupportFragmentManager();
        DeleteProductDialog editNameDialog = new DeleteProductDialog();
        editNameDialog.show(fm, "");
    }

    private void populateDetailView(int productId) {

        product = getElementById(productId);

        ImageView ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        TextView productInfo = (TextView) findViewById(R.id.tvProductInfo);
        productQuantity = (TextView) findViewById(R.id.tvQuantity);
        TextView productSupplier = (TextView) findViewById(R.id.tvSupplier);

        byte[] productImg = product.getImage();


        Bitmap bmp = BitmapFactory.decodeByteArray(productImg, 0, productImg.length);
        ivProductImage.setImageBitmap(bmp);

       productInfo.setText( product.getName() + " - $" + product.getPrice());
       productQuantity.setText("Quantity available: " + String.valueOf(product.getQuantity()));
        productSupplier.setText("Email Supplier: " + String.valueOf(product.getSupplier()));


    }

    private Product getElementById(int productId) {

        Product product = null;

        Cursor c = mDbHelper.getProductById(productId);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int entryId = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID));
                String productName = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT));
                double price = c.getDouble(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRICE));
                int quantity =  c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY));
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


    public static void deleteCurrentProduct() {

        mDbHelper.deleteElementById(productId);
        detailActivity.finish();

    }

    public static void updateProductQuantity(int newQuantity) {
        int rowsUpdated = 0;

        try {
            rowsUpdated = mDbHelper.updateQuantityById(productId, newQuantity);
            if(rowsUpdated > 0){
                productQuantity.setText("Quantity available: " + String.valueOf(newQuantity));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
