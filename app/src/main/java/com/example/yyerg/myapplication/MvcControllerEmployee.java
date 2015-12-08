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
public class MvcControllerEmployee {
    private MvcModelEmployee model;
    private List<String> employees;
    private List<String> departments;
    private List<EmployeeDetail> details;

    public MvcControllerEmployee(Context app_context) {
        employees = new ArrayList<String>();
        departments = new ArrayList<String>();
        details = new ArrayList<EmployeeDetail>();
        model = new MvcModelEmployee(app_context);
    }

    public void addEmployee(final String name,final String department) {
        final ContentValues data = new ContentValues();
        data.put("name", name);
        data.put("department",department);
        model.addEmployee(data);
    }

    public void deleteEmployee(final String name) {
        model.deleteEmployee("name='" + name + "'");
    }

    public void deleteEmployee(final Integer id) {
        Log.d(MainActivity.APP_TAG, id.toString());
        model.deleteEmployee("id='" + id + "'");
    }

    public void deleteAllEmployee() {
        model.deleteEmployee(null);
    }

    public List<String> getEmployees(final String department) {
        Cursor c = model.loadEmployees(department);
        employees.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                employees.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        }
        return employees;
    }

    public class EmployeeDetail{
        public Integer id;
        public String name;
        public EmployeeDetail(Integer id, String name){
            this.id = id;
            this.name = name;
        }
    }

    public List<EmployeeDetail> getEmployeeDetail(final String department) {
        Cursor c = model.loadEmployees(department);
        details.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                EmployeeDetail e = new EmployeeDetail(c.getInt(0),c.getString(1));
                details.add(e);
                c.moveToNext();
            }
            c.close();
        }
        return details;
    }

    public List<String> getDepartment() {
        Cursor c = model.loadDepartments();
        departments.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                departments.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        }
        return departments;
    }
}
