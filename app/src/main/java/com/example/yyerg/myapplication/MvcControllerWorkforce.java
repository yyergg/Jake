package com.example.yyerg.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yyerg on 2015/10/12.
 */
public class MvcControllerWorkforce {
    private MvcModelWorkforce model;
    private List<Workforce> workforces;

    public MvcControllerWorkforce(Context app_context) {
        workforces = new ArrayList<Workforce>();
        model = new MvcModelWorkforce(app_context);
    }

    public class Workforce{
        public Integer id;
        public String name;
        public String remark;
        public Workforce(Integer id, String name, String remark){
            this.id = id;
            this.name = name;
            this.remark = remark;
        }
    }

    public List<Workforce> getWorkforce(final String department, final String date) {
        Cursor c = model.initAndLoadWorkforce(department, date);
        workforces.clear();
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                Workforce e = new Workforce(c.getInt(0),c.getString(1),c.getString(2));
                workforces.add(e);
                c.moveToNext();
            }
            c.close();
        }
        return workforces;
    }

    public void editRemark(String date, Integer workerid, String remark){
        model.editRemark(date,workerid,remark);
    }
}
