package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import android.util.Log;

/**
 * Created by yyerg on 2015/10/10.
 */
public class MvcModelProduct {
    static final String TABLE_NAME = "Products";

    private final SQLiteDatabase database;

    public MvcModelProduct(final Context ctx) {
        this.database = DBHelper.database;
    }

    public void addProduct(ContentValues data) {
        this.database.insert(MvcModelProduct.TABLE_NAME, null, data);
    }

    public void deleteProduct(final String field_params) {
        this.database.delete(MvcModelProduct.TABLE_NAME, field_params, null);
    }

    public Cursor loadAllProducts() {
        Log.d(MainActivity.APP_TAG, "loadAllProducts()");
        final Cursor c = this.database.query(
                MvcModelProduct.TABLE_NAME,
                new String[]{"title"},
                null, null, null, null,"title");
        return c;
    }
}
