package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yyerg on 2015/10/12.
 */
public class MvcControllerOrder {
    private MvcModelOrder model;
    private List<Order> orders;
    private List<ProductInOrder> products;

    public MvcControllerOrder(Context app_context) {
        orders = new ArrayList<Order>();
        products = new ArrayList<ProductInOrder>();
        model = new MvcModelOrder(app_context);
    }

    public class Order{
        public Integer id;
        public String description;
        public String date;
        public Order(Integer id, String description, String date){
            this.id = id;
            this.description = description;
            this.date = date;
        }
    }

    public class ProductInOrder{
        public String name;
        public Integer amount;
        public String category;
        public ProductInOrder(String name, Integer amount, String category){
            this.name = name;
            this.amount = amount;
            this.category = category;
        }
    }

    public void deleteOrder(Integer orderId){
        model.deleteOrder("orderid='" + orderId.toString() + "'");
    }

    public Integer getNewId(){
        Cursor c = model.getMaxId();
        if (c == null){
            return 1;
        }
        c.moveToFirst();
        return c.getInt(0) + 1;
    }

    public List<ProductInOrder> getProductInOrder(Integer orderId){
        Cursor c = model.getOrderById(orderId);
        products.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                ProductInOrder e = new ProductInOrder(c.getString(3),c.getInt(4),c.getString(5));
                products.add(e);
                c.moveToNext();
            }
            c.close();
        }
        return products;
    }

    public List<Order> getOrder() {
        Cursor c = model.getOpenOrder();
        orders.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Order e = new Order(c.getInt(0),c.getString(1),c.getString(2));
                orders.add(e);
                c.moveToNext();
            }
            c.close();
        }
        return orders;
    }

    public void AddSingleProduct(Integer orderId,String date,String description,String name,String amount, String category){
        final ContentValues data = new ContentValues();
        data.put("date", date);
        data.put("description", description);
        data.put("orderid", orderId);
        data.put("product", name);
        data.put("amount", Integer.valueOf(amount));
        data.put("closed", 0);
        data.put("category", category);
        model.AddSingleProduct(data);
    }
}
