package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yyerg on 2015/10/12.
 */
public class MvcControllerProduct {
    private MvcModelProduct model;
    private List<String> products;

    public MvcControllerProduct(Context app_context) {
        products = new ArrayList<String>();
        model = new MvcModelProduct(app_context);
    }

    public void addProduct(final String title, final String category) {
        final ContentValues data = new ContentValues();
        data.put("title", title);
        data.put("category", category);
        model.addProduct(data);
    }

    public void deleteProduct(final String title) {
        model.deleteProduct("title='" + title + "'");
    }

    public String getCategory(final String title) {
        String s = model.getCategory(title);
        return s;
    }

    public void deleteProduct(final long id) {
        model.deleteProduct("id='" + id + "'");
    }

    public void deleteAllProduct() {
        model.deleteProduct(null);
    }

    public List<String> getProducts() {
        Cursor c = model.loadAllProducts();
        products.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                products.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        }
        return products;
    }

}
