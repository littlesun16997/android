package fi.jamk.shoppinglist;

import android.database.sqlite.SQLiteDatabase;

public class ShoppingListDatabase {

    // Database table
    public static final String TABLE_SHOPPINGLIST = "shoppinglist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_UNIT = "unit_price";
    public static final String COLUMN_PRICE = "price";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_SHOPPINGLIST
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PRODUCT + " text not null, "
            + COLUMN_COUNT + " int, "
            + COLUMN_UNIT + " double, "
            + COLUMN_PRICE + " double"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPINGLIST);
        onCreate(database);
    }
}