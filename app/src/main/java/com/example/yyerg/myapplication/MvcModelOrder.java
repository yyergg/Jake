package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

/**
 * Created by yyerg on 2015/10/10.
 */
public class MvcModelOrder {
    static final String TABLE_NAME = "Orders";

    private final SQLiteDatabase database;

    private MvcControllerProduct controllerProduct;

    public MvcModelOrder(final Context ctx) {
        this.database = DBHelper.database;
        this.controllerProduct = new MvcControllerProduct(ctx);
    }

    public void addOrder(ContentValues data) {
        this.database.insert(MvcModelOrder.TABLE_NAME, null, data);
    }

    public void deleteOrder(final String field_params) {
        this.database.delete(MvcModelOrder.TABLE_NAME, field_params, null);
    }

    public Cursor getMaxId(){
        Cursor c = this.database.query(
                MvcModelOrder.TABLE_NAME,
                new String[]{"MAX(orderid)"},
                null, null, null, null, null);
        return c;
    }

    public Cursor getOrderById(Integer id){
        Cursor c;
        c = this.database.query(
                true,
                MvcModelOrder.TABLE_NAME,
                new String[]{"orderid","description","date","product","amount","category"},
                "orderid='" + id.toString() + "'",
                null, null, null, "date", null);
        return c;
    }

    public Cursor getOpenOrder(){
        Cursor cursorOrder;
        cursorOrder = this.database.query(
                true,
                MvcModelOrder.TABLE_NAME,
                new String[]{"orderid","description","date"},
                "closed=0",
                null, null, null, "date", null);
        return cursorOrder;
    }

    public void AddSingleProduct(ContentValues data){
        this.database.insert(MvcModelOrder.TABLE_NAME, null, data);
    }
}
