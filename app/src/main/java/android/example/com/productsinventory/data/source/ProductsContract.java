package android.example.com.productsinventory.data.source;

import android.provider.BaseColumns;

public class ProductsContract {

    public ProductsContract(){}

    public static abstract class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_ENTRY_ID = "entryId";
        public static final String COLUMN_NAME_PRODUCT = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_SUPPLIER = "supplier";
        public static final String COLUMN_NAME_IMAGE = "image";

        public static String COLUMN_NAME_NULLABLE = null;

    }
}