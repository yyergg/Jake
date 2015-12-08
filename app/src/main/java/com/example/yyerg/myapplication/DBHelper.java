package com.example.yyerg.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yyerg on 2015/10/10.
 */
public class DBHelper {
    private static final String DB_NAME = "jake_db";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE_EMPLOYEE = "CREATE TABLE "
            + MvcModelEmployee.TABLE_NAME
            + " (id integer primary key autoincrement, name text not null, department text not null);";
    private static final String DB_CREATE_PRODUCT = "CREATE TABLE "
            + MvcModelProduct.TABLE_NAME
            + " (id integer primary key autoincrement, title text not null, category text not null);";
    private static final String DB_CREATE_WORKFORCE = "CREATE TABLE "
            + MvcModelWorkforce.TABLE_NAME
            + " (id integer primary key autoincrement, date text not null, department text not null, workerid integer, name text not null, remark text not null);";
    private static final String DB_CREATE_ORDER = "CREATE TABLE "
            + MvcModelOrder.TABLE_NAME
            + " (id integer primary key autoincrement, date text not null, description text not null, orderid integer, product text not null, amount integer, closed integer);";

    static SQLiteDatabase database;
    SQLiteOpenHelper helper;

    public DBHelper(final Context ctx) {
        this.helper = new SQLiteOpenHelper(ctx, DBHelper.DB_NAME, null, DBHelper.DB_VERSION) {
            @Override
            public void onCreate(final SQLiteDatabase db) {
                db.execSQL(DBHelper.DB_CREATE_EMPLOYEE);
                db.execSQL(DBHelper.DB_CREATE_PRODUCT);
                db.execSQL(DBHelper.DB_CREATE_WORKFORCE);
                db.execSQL(DBHelper.DB_CREATE_ORDER);
            }

            @Override
            public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + MvcModelEmployee.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + MvcModelProduct.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + MvcModelWorkforce.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + MvcModelOrder.TABLE_NAME);
                this.onCreate(db);
            }
        };
        Log.d(MainActivity.APP_TAG, "DB created.");
        this.database = this.helper.getWritableDatabase();
    }
}
