package com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 3/27/2018.
 */
//here we implement base columns here cos it ia an interface containing count and id...it will automatically
    //generate an id for us which we will use as the primary key
public class CreditorsAndDebtorsDataBase extends SQLiteOpenHelper implements BaseColumns {
    /*SQLite is a opensource SQL database that stores data to a
    text file on a device. Android comes in with built in SQLite database implementation.

SQLite supports all the relational database features.
In order to access this database, you don't need to establish any kind of
 connections for it like JDBC,ODBC e.t.c*/
    //create constants that define how the table will look like
    public static final String DB_NAME = "debtorsandcreditors.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "debtorsandcreditors";
    public static final String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String GET_IMAGE_QUERY = "SELECT * FROM " + TABLE_NAME;
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_IMAGE = "cutomer_image";
    public static final String COLUMN_ITEM_BOUGHT = "photo_url";
    public static final String COLUMN_HOW_MUCH = "how_much";
    public static final String COLUMN_DEBT_OR_CHANGE = "debt_or_change";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_TIME_STAMP = "time_stamp";
    SQLiteDatabase db;
    //Now we create a string, this string is what we will excecute and when we excecute it..
    //an sqlite table will be created for us with all the values we added
    final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "" +
            "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CUSTOMER_NAME + " TEXT not null, " +
            COLUMN_ITEM_BOUGHT + " TEXT not null, " +
            COLUMN_DEBT_OR_CHANGE + " TEXT not null, " +
            COLUMN_IMAGE + " TEXT, " +
            COLUMN_TIME_STAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            COLUMN_PHONE_NUMBER + " TEXT not null, " +
            COLUMN_HOW_MUCH + " TEXT not null)";

    public CreditorsAndDebtorsDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //here we excecute the table.... we surround this in  a try catch cos sqliteDatabase.excecSql throws an sqlException
        try {
            sqLiteDatabase.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
            Log.e(TAG, "SqliteException caught");
        }


    }
    //onUpgrade is useful in case we want to change a column in the table
    //it will drop the previous tables and create  a new table for us
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_QUERY);
        this.onCreate(sqLiteDatabase);
    }
    //this method will be called when the user clicks save..
    // it takes in all the parameters that we expect the user to enter
    public void SaveLecturesAdded(String CustomerName, String boughtStuff,
                                  String howMuch, String debtOrChange, String ImageUri,
                                  String phoneNumber) {
        /**android.content.ContentValues.
         * This class is used to store a set of values that the ContentResolver can process.
         * and the content resolver: This class provides applications access to the content model.
         * put in simple terms... we use the content values to put all the data the user will enter
         * into the database table before we finally insert them using the insert method**/
        ContentValues contentValues = new ContentValues();
        //getWritabledatabase is here because we wnt to write to the data base...
        //i.e we want to add a row to the database table
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_CUSTOMER_NAME, CustomerName);
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_ITEM_BOUGHT, boughtStuff);
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_HOW_MUCH, howMuch);
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_DEBT_OR_CHANGE, debtOrChange);
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_IMAGE, ImageUri);
        contentValues.put(CreditorsAndDebtorsDataBase.COLUMN_PHONE_NUMBER, phoneNumber);
        try {
            sqLiteDatabase.insert(CreditorsAndDebtorsDataBase.TABLE_NAME, null, contentValues);
        } catch (SQLException e) {
            Log.d(TAG, "SqliteException Thrown");

        }

    }

    //RETRIEVE OR FILTERING
    //now...we have 3 methods for querying the database
    public Cursor retrieve(String searchTerm) {
        //here we specify the columns that we are interested in querying although, this
        //is not necessary cos we are interested in all the columns
        String[] columns = {COLUMN_CUSTOMER_NAME, COLUMN_ITEM_BOUGHT, COLUMN_HOW_MUCH, COLUMN_TIME_STAMP, COLUMN_DEBT_OR_CHANGE};
        Cursor c = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CUSTOMER_NAME + " LIKE '%" + searchTerm + "%'";
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        c = liteDatabase.rawQuery(sql, null);
        return c;
    }

    //RETRIEVE OR FILTERING
    public Cursor retrieveByViewpager(String page) {
        String[] columns = {COLUMN_CUSTOMER_NAME, COLUMN_ITEM_BOUGHT, COLUMN_HOW_MUCH, COLUMN_TIME_STAMP, COLUMN_DEBT_OR_CHANGE};
        /**Cursors are what contain the result set of a
         *  query made against a database in Android.
         *  The Cursor class has an API that allows an app to read (in a type-safe manner)
         * the columns that were returned from the query as well as iterate over the rows of the result set.**/
        Cursor c = null;
        //Now here is the important shit.... we want to query the data base in such a way that
        //all debtors will be separted from all the people we are owing...
        //so we select from the table where the column debt or change is the same as the page name
        //we will see how this is used in the creditors and debtors  record fragments
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DEBT_OR_CHANGE + " = '" + page + "' ";
      //  String sql2 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CUSTOMER_NAME + " LIKE '%" + page + "%'";
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        c = liteDatabase.rawQuery(sql, null);
        return c;

    }
    public Cursor  retrieveByViewPagerAndSearchedText(String searchedItem, String page){
        //now when we search we want to make sure the name we are querying is the same as
        //what we are searching and is under the debt or change page that we will pass
        Cursor c = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CUSTOMER_NAME + " LIKE '%" +
                searchedItem + "%'" + " AND " + COLUMN_DEBT_OR_CHANGE +  " = '" + page + "' ";
        //get readable database is used here cos we  do not want to write to the database
        //rather we want read it and look for available data in the table
        SQLiteDatabase liteDatabase = this.getReadableDatabase();
        //now we eventually query the data base and return the cursor which we will use in our recyler
        //view
        c = liteDatabase.rawQuery(sql, null);
        return c;

    }
}
//Data base class was fun :)