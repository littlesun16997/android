package fi.jamk.shoppinglist;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ModifyShoppingListDialogFragment.DialogListener {

    // This is the Adapter being used to display the list's data.
    private SimpleCursorAdapter adapter;

    private long selectedItemId;

    // Context Menu for delete
    private final int UPDATE_ID = 0;
    private final int DELETE_ID = 1;

    // list view
    private ListView listView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find list view
        listView = (ListView) findViewById(R.id.listView);

        // register listView's context menu
        registerForContextMenu(listView);

        // show list
        showList();
    }

    private void calculateTotal() {
        // get database instance
        db = (new ShoppingListDatabaseOpenHelper(this)).getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM shoppinglist ORDER BY product", null);

        // calculate total prices
        float total = 0.2f;
        if (cursor.moveToFirst()) {
            do {
                float price = cursor.getFloat(4); // columnIndex
                total += price;
            } while(cursor.moveToNext());
            Toast.makeText(getBaseContext(), "Total price: " + String.format("%.2f", total), Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    // show product in listview
    private void showList() {
        // Fields from the database (projection)
        String[] from = new String[]{ShoppingListDatabase.COLUMN_PRODUCT, ShoppingListDatabase.COLUMN_COUNT, ShoppingListDatabase.COLUMN_UNIT, ShoppingListDatabase.COLUMN_PRICE};

        // Fields on the UI to which we map
        int[] to = new int[]{R.id.name, R.id.count, R.id.unit, R.id.price};

        // init loader, call data if needed
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item, null, from, to, 0);

        // show data in listView
        listView.setAdapter(adapter);
        calculateTotal();
    }

    /**
     * LOADER BELOW THIS ONE
     **/

    // Creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ShoppingListDatabase.COLUMN_ID,
                ShoppingListDatabase.COLUMN_PRODUCT,
                ShoppingListDatabase.COLUMN_COUNT,
                ShoppingListDatabase.COLUMN_UNIT,
                ShoppingListDatabase.COLUMN_PRICE
        };
        CursorLoader cursorLoader = new CursorLoader(
                this,
                ShoppingListContentProvider.CONTENT_URI,
                projection, null, null, "product ASC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // new data is available, use it
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    /* insert and update */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int count, double unit) {
        Cursor cursor = db.rawQuery("SELECT _id FROM shoppinglist", null);
        boolean flag = false;

        ContentValues values = new ContentValues(4);
        values.put("product", name);
        values.put("count", count);
        values.put("unit_price", unit);
        values.put("price", unit*count);

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(0) == selectedItemId) {
                    flag = true;

                    String[] args = {String.valueOf(selectedItemId)};
                    getContentResolver().update(ShoppingListContentProvider.CONTENT_URI, values, "_id=?", args);
                    break;
                }
            } while(cursor.moveToNext());
        }

        cursor.close();

        if (!flag) {
            getContentResolver().insert(ShoppingListContentProvider.CONTENT_URI, values);
        }
        calculateTotal();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO Auto-generated method stub
    }

    /* update and delete */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedItemId = info.id;

        menu.add(Menu.NONE, UPDATE_ID, Menu.NONE, "Update");
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case DELETE_ID:
                String[] args = {String.valueOf(info.id)};

                // url, where, args
                getContentResolver().delete(ShoppingListContentProvider.CONTENT_URI, "_id=?", args);

                calculateTotal();
                break;

            case UPDATE_ID:
                ModifyShoppingListDialogFragment eDialog = new ModifyShoppingListDialogFragment();
                eDialog.show(getFragmentManager(), "Update the product");
                break;

        }

        return (super.onOptionsItemSelected(item));
    }

    /**
     * MENU BELOW THIS ONE
     **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                ModifyShoppingListDialogFragment eDialog = new ModifyShoppingListDialogFragment();
                eDialog.show(getFragmentManager(), "Add a new product");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
