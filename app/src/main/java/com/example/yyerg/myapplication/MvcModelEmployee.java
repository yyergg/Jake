package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by yyerg on 2015/10/10.
 */
public class MvcModelEmployee {
    static final String TABLE_NAME = "Employees";

    private final SQLiteDatabase database;

    public MvcModelEmployee(final Context ctx) {
        this.database = DBHelper.database;
    }

    public void addEmployee(ContentValues data) {
        this.database.insert(MvcModelEmployee.TABLE_NAME, null, data);
    }

    public void deleteEmployee(final String field_params) {
        this.database.delete(MvcModelEmployee.TABLE_NAME, field_params, null);
    }

    public Cursor loadEmployees(final String department) {
        final Cursor c = this.database.query(
                MvcModelEmployee.TABLE_NAME,
                new String[]{"id","name"},
                "department='" + department + "'", null, null, null, "id");
        return c;
    }

    public Cursor loadDepartments() {
        final Cursor c = this.database.query( true,
                MvcModelEmployee.TABLE_NAME,
                new String[]{"department"}, null, null, null, null, "department", null);
        return c;
    }
}
