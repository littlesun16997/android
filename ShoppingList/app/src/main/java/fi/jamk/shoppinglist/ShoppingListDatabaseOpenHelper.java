package fi.jamk.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingListDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shoppinglisttable.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public ShoppingListDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // database creation
    @Override
    public void onCreate(SQLiteDatabase database) {
        ShoppingListDatabase.onCreate(database);
    }

    // database upgrade
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ShoppingListDatabase.onUpgrade(database, oldVersion, newVersion);
    }
}