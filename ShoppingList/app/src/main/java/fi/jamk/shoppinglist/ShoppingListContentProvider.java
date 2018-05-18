package fi.jamk.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ShoppingListContentProvider extends ContentProvider {
    private static final String AUTHORITY = "fi.jamk.shoppinglist.contentprovider";
    private static final String BASE_PATH = "shoppinglist";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/shoppinglist";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/shoppinglist";

    private ShoppingListDatabaseOpenHelper database;

    // Used for the UriMatcher
    private static final int SHOPPINGLIST = 1;
    private static final int SHOPPINGLIST_ID = 2;

    // is URI ending with /# -> only one shoppinglist, else all
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, SHOPPINGLIST);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SHOPPINGLIST_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ShoppingListDatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ShoppingListDatabase.TABLE_SHOPPINGLIST);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case SHOPPINGLIST:
                break;
            case SHOPPINGLIST_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere("_id =" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case SHOPPINGLIST:
                id = sqlDB.insert(ShoppingListDatabase.TABLE_SHOPPINGLIST, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        int count;

        switch (sURIMatcher.match(uri)) {
            case SHOPPINGLIST:
                count = db.delete(ShoppingListDatabase.TABLE_SHOPPINGLIST, selection, selectionArgs);
                break;
            case SHOPPINGLIST_ID:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(
                        ShoppingListDatabase.TABLE_SHOPPINGLIST,
                        ShoppingListDatabase.COLUMN_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        int count;
        switch (sURIMatcher.match(uri)) {
            case SHOPPINGLIST:
                count = db.update(ShoppingListDatabase.TABLE_SHOPPINGLIST, values, selection, selectionArgs);
                break;
            case SHOPPINGLIST_ID:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(ShoppingListDatabase.TABLE_SHOPPINGLIST, values,
                        ShoppingListDatabase.COLUMN_ID + "=" + rowId +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}