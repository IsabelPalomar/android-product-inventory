package android.example.com.productsinventory.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.example.com.productsinventory.dialogs.AddProductDialog;
import android.example.com.productsinventory.R;
import android.example.com.productsinventory.adapters.ProductsRecyclerAdapter;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.data.source.ProductsContract;
import android.example.com.productsinventory.data.source.ProductsDbHelper;
import android.example.com.productsinventory.utils.Constants;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ProductsDbHelper mDbHelper;
    static ProductsRecyclerAdapter productsAdapter;
    RecyclerView recyclerView;
    static TextView tvAddValues;
    private static List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvAddValues = (TextView) findViewById(R.id.tvAddValues);
        setSupportActionBar(toolbar);

        mDbHelper = new ProductsDbHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.rvProducts);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(llm);
        }

        products = getAllElements();

        //Creates a new a instance of ProductsRecyclerAdapter, passing the current context and the list of products
        productsAdapter = new ProductsRecyclerAdapter(this, products);
        if (recyclerView != null) {
            recyclerView.setAdapter(productsAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddDialog();

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProductsList();
    }

    /**
     * Show the dialog to add products
     */
    private void showAddDialog() {
        AddProductDialog editNameDialog = new AddProductDialog();
        editNameDialog.show(getSupportFragmentManager(), "");
    }

    /**
     * Insert new value in DataBase
     */
    public static long insertValue(Product product) {
        long newHabitId1 = 0;
        try {

            ContentValues values = new ContentValues();
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT, product.getName());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRICE, product.getPrice());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY, product.getQuantity());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER, product.getSupplier());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_IMAGE, product.getImage());

            newHabitId1 = mDbHelper.insertProduct(values);

            if (newHabitId1 > 0) {
                refreshProductsList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newHabitId1;
    }

    /**
     * Get all the product from db
     * @return a list of Products
     */
    private static List<Product> getAllElements() {

        List<Product> productsList = new ArrayList<>();
        Cursor c = mDbHelper.getAllProducts();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int entryId = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID));
                String productName = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT));
                double price = c.getDouble(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRICE));
                int quantity = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY));
                String supplier = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER));
                byte[] imageByte = c.getBlob(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_IMAGE));

                Product product = new Product(entryId, productName, price, quantity, supplier, imageByte);
                productsList.add(product);
            }
        }
        if (c != null) {
            c.close();
        }

        mDbHelper.close();
        return productsList;
    }

    /**
     * Update product quantity in DB
     */
    public void updateProductQuantity(int productId, int newQuantity) {
        int rowsUpdated;

        try {
            rowsUpdated = mDbHelper.updateQuantityById(productId, newQuantity);
            if (rowsUpdated > 0) {
                refreshProductsList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the Product List
     */
    public static void refreshProductsList() {

        products = getAllElements();
        productsAdapter.refreshList(products);

        if(products.size() > 0){
            tvAddValues.setText(Constants.BLANK_VALUE);
        }else{
            tvAddValues.setText(R.string.app_message_add);
        }

    }
}