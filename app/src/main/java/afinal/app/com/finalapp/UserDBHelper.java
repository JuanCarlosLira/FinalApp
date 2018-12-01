package afinal.app.com.finalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "storeDatabase";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_USERS = "CREATE TABLE " + UserEntry.TABLE_NAME  +" ("+
            UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "+
            UserEntry.COLUMN_USER + " TEXT, "+
            UserEntry.COLUMN_PSWD + " TEXT) ";

    /*private static final String SQL_CREATE_PRODS = "CREATE TABLE " + ProductEntry.TABLE_NAME  +" ("+
            ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "+
            ProductEntry.COLUMN_NAME + " TEXT, "+
            ProductEntry.COLUMN_MODEL + " TEXT, "+
            ProductEntry.COLUMN_DESC + " TEXT, "+
            ProductEntry.COLUMN_PRICE + " INTEGER) ";*/

    private static final String DESTROY_USERS = "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;
    //private static final String DESTROY_PRODS = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_USERS);
        //db.execSQL(SQL_CREATE_PRODS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DESTROY_USERS);
        //db.execSQL(DESTROY_PRODS);
        onCreate(db);
    }
}
