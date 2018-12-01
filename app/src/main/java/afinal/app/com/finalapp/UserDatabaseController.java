package afinal.app.com.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDatabaseController {
    UserDBHelper helper;
    SQLiteDatabase database;

    public UserDatabaseController(Context context){
        helper = new UserDBHelper(context);
        database = helper.getWritableDatabase();
    }

    // Users table Methods

    public long insertUser(UserEntry user){

        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_USER, user.getUser());
        values.put(UserEntry.COLUMN_PSWD, user.getPassword());

        long inserted = database.insert(UserEntry.TABLE_NAME, null, values);
        return inserted;
    }

    public Cursor selectUsers(String selection, String[] selectionArgs){

        String columns[] = {
                UserEntry.COLUMN_ID,
                UserEntry.COLUMN_USER,
                UserEntry.COLUMN_PSWD
        };


        Cursor cursor = database.query(UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null
                , null);

        return cursor;
    }

    // Product table Methods

    /*public long insertProduct(ProductEntry prod){

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME, prod.getName());
        values.put(ProductEntry.COLUMN_MODEL, prod.getModel());
        values.put(ProductEntry.COLUMN_DESC, prod.getDescription());
        values.put(ProductEntry.COLUMN_PRICE, prod.getPrice());

        long inserted = database.insert(ProductEntry.TABLE_NAME, null, values);
        return inserted;
    }*/

    /*public Cursor selectProducts(String selection, String[] selectionArgs){

        String columns[] = {
                ProductEntry.COLUMN_ID,
                ProductEntry.COLUMN_NAME,
                ProductEntry.COLUMN_MODEL,
                ProductEntry.COLUMN_DESC,
                ProductEntry.COLUMN_PRICE
        };


        Cursor cursor = database.query(ProductEntry.TABLE_NAME, columns, selection, selectionArgs, null, null
                , null);

        return cursor;
    }*/

    /*public long updateProduct(ProductEntry prod){

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME, prod.getName());
        values.put(ProductEntry.COLUMN_MODEL, prod.getModel());
        values.put(ProductEntry.COLUMN_DESC, prod.getDescription());
        values.put(ProductEntry.COLUMN_PRICE, prod.getPrice());

        String selection = ProductEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { ""+prod.getId() };

        long count = database.update(
                ProductEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }*/

    /*public long deleteProduct(ProductEntry prod){

        String where = ProductEntry.COLUMN_ID + " = ?";
        String whereArgs[] = {
                ""+prod.getId()
        };

        long deleted = database.delete(ProductEntry.TABLE_NAME, where, whereArgs);

        return deleted;
    }*/

    public  void close(){
        database.close();
    }
}
