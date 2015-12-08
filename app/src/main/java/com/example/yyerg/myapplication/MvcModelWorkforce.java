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
public class MvcModelWorkforce {
    static final String TABLE_NAME = "Workforce";

    private final SQLiteDatabase database;

    private MvcControllerEmployee controllerEmployee;

    public MvcModelWorkforce(final Context ctx) {
        this.database = DBHelper.database;
        this.controllerEmployee = new MvcControllerEmployee(ctx);
    }

    public void addWorkforce(ContentValues data) {
        this.database.insert(MvcModelWorkforce.TABLE_NAME, null, data);
    }

    public void deleteWorkforce(final String field_params) {
        this.database.delete(MvcModelWorkforce.TABLE_NAME, field_params, null);
    }

    public Cursor initAndLoadWorkforce(final String department, final String date) {
        Log.d(MainActivity.APP_TAG, "initAndLoadWorkforce()");
        List<MvcControllerEmployee.EmployeeDetail> employees = this.controllerEmployee.getEmployeeDetail(department);
        Cursor cursorWorkforce;
        Integer i = 0;
        while(i<employees.size()) {
            cursorWorkforce = this.database.query(
                    MvcModelWorkforce.TABLE_NAME,
                    new String[]{"workerid","name"},
                    "department='" + department + "' AND date='" + date + "' AND workerid='"+ employees.get(i).id.toString() +"'",
                    null, null, null, null);
            if(cursorWorkforce.getCount() == 0){
                ContentValues cv = new ContentValues();
                cv.put("date",date);
                cv.put("department",department);
                cv.put("workerid",employees.get(i).id);
                cv.put("name",employees.get(i).name);
                cv.put("remark",R.string.spinner_attend);
                this.addWorkforce(cv);
            }
            i++;
        }
        cursorWorkforce = this.database.query(
                MvcModelWorkforce.TABLE_NAME,
                new String[]{"workerid","name","remark"},
                "department='" + department + "' AND date='" + date + "'"
                , null, null, null, null);
        return cursorWorkforce;
    }

    public void editRemark(String date, Integer workerid, String remark){
        Log.d(MainActivity.APP_TAG, "edit:" + remark + " " + workerid);
        ContentValues cv = new ContentValues();
        cv.put("remark",remark);
        this.database.update(
                MvcModelWorkforce.TABLE_NAME,
                cv,
                "date='" + date + "' AND workerid='" + workerid + "'",
                null
        );
    }
}
