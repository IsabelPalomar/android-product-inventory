package android.example.com.productsinventory;

import android.content.ContentValues;
import android.database.Cursor;
import android.example.com.productsinventory.adapters.ProductsRecyclerAdapter;
import android.example.com.productsinventory.data.Product;
import android.example.com.productsinventory.data.source.ProductsContract;
import android.example.com.productsinventory.data.source.ProductsDbHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static ProductsDbHelper mDbHelper;
    static ProductsRecyclerAdapter productsAdapter;
    private static List<Product> products;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbHelper = new ProductsDbHelper(this);

        //insertValue(mDbHelper);

        products = getAllElements();

        recyclerView = (RecyclerView) findViewById(R.id.rvProducts);
        LinearLayoutManager llm = new LinearLayoutManager(this);

        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(llm);
        }

        //Creates a new a instance of PlacesRecyclerAdapter, passing the current context and the list of products
        productsAdapter = new ProductsRecyclerAdapter(this, products);
        if (recyclerView != null) {
            recyclerView.setAdapter(productsAdapter);
        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();

            }
        });


    }

    private void showAddDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddProductDialog editNameDialog = new AddProductDialog();
        editNameDialog.show(fm, "fragment_edit_name");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Insert new value
     */
    public static long insertValue(Product product){
        long newHabitId1 = 0;

        try{

            ContentValues values = new ContentValues();
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT, product.getName());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRICE, product.getPrice());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY, product.getQuantity());
            values.put(ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER, product.getSupplier());

            newHabitId1 = mDbHelper.insertProduct(values);

            if(newHabitId1 > 0){
                refreshProductsList();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return newHabitId1;
    }

    /**
     *  Create a new map of values (testing)
     */
    private ContentValues generateRandomValue() {

        ContentValues values = new ContentValues();

        values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT, "Blue t-shirt");
        values.put(ProductsContract.ProductEntry.COLUMN_NAME_PRICE, 15.5);
        values.put(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY, 10);
        values.put(ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER, "Urban Outfitters");

        return values;
    }


    private static List<Product> getAllElements() {

        List<Product> productsList  = new ArrayList<>();

        Cursor c = mDbHelper.getAllProducts();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                int entryId = c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID));
                String productName = c.getString(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT));
                double price = c.getDouble(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_PRICE));
                int quantity =  c.getInt(c.getColumnIndexOrThrow(ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY));

                Product product = new Product(entryId, productName, price, quantity);
                productsList.add(product);
            }
        }
        if (c != null) {
            c.close();
        }

        mDbHelper.close();

        return productsList;
    }

    public void updateProductQuantity(int productId, int newQuantity) {
        int rowsUpdated = 0;

        try {
            rowsUpdated = mDbHelper.updateQuantityById(productId, newQuantity);
            if(rowsUpdated > 0){
                refreshProductsList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void refreshProductsList(){
        products = getAllElements();
        productsAdapter.refreshList(products);
    }


}
