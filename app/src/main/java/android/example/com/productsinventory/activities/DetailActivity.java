package android.example.com.productsinventory.activities;

import android.content.Intent;
import android.database.Cursor;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.data.source.ProductsContract;
import android.example.com.productsinventory.data.source.ProductsDbHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.example.com.productsinventory.R;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    ProductsDbHelper mDbHelper;
    int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mDbHelper = new ProductsDbHelper(this);

        productId  = intent.getIntExtra("productId", 0);

        populateDetailView(productId);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void populateDetailView(int productId) {

        Product product = getElementById(productId);

        ImageView ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        TextView productInfo = (TextView) findViewById(R.id.tvProductInfo);
        TextView productQuantity = (TextView) findViewById(R.id.tvQuantity);
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





}
