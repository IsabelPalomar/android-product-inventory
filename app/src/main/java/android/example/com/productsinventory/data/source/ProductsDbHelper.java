package android.example.com.productsinventory.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Products.db";

    private static final String INTEGER_PK = " INTEGER PRIMARY KEY";

    private static final String TEXT_TYPE = " TEXT";

    private static final String REAL_TYPE = " REAL";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + ProductsContract.ProductEntry.TABLE_NAME + " (" +
                    ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID + INTEGER_PK + COMMA_SEP +
                    ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT + TEXT_TYPE + COMMA_SEP +
                    ProductsContract.ProductEntry.COLUMN_NAME_PRICE + TEXT_TYPE + COMMA_SEP +
                    ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                    ProductsContract.ProductEntry.COLUMN_NAME_SUPPLIER + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductsContract.ProductEntry.TABLE_NAME;


    public ProductsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Get all products stored in database
     */
    public Cursor getAllProducts() {

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID,
                ProductsContract.ProductEntry.COLUMN_NAME_PRODUCT,
                ProductsContract.ProductEntry.COLUMN_NAME_PRICE,
                ProductsContract.ProductEntry.COLUMN_NAME_QUANTITY,

        };

        String sortOrder =
                ProductsContract.ProductEntry.COLUMN_NAME_ENTRY_ID + " ASC";

        Cursor c = db.query(
                ProductsContract.ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return c;
    }


    /**
     * Insert the new row, returning the primary key value of the new row
     */
    public long insertProduct(ContentValues values) {

        SQLiteDatabase db = this.getWritableDatabase();

        //db.execSQL(SQL_DELETE_ENTRIES);
        //db.execSQL(SQL_CREATE_PRODUCTS_TABLE);


        long newRowId;

        System.out.println(values);

        newRowId = db.insert(
                ProductsContract.ProductEntry.TABLE_NAME,
                ProductsContract.ProductEntry.COLUMN_NAME_NULLABLE,
                values);

        System.out.println(newRowId);


        return newRowId;
    }


}