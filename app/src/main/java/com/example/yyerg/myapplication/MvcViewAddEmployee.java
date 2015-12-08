package com.example.yyerg.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by yyerg on 2015/10/11.
 */
public class MvcViewAddEmployee extends Activity {


    private Button btAdd;
    private Button btCancel;
    private EditText etName;
    private EditText etDep;

    private MvcControllerEmployee controller;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_add_employee);
        this.controller = new MvcControllerEmployee(this);
        this.btAdd = (Button) this.findViewById(R.id.btAdd);
        this.btCancel = (Button) this.findViewById(R.id.btCancel);
        this.etName = (EditText) this.findViewById(R.id.etName);
        this.etDep = (EditText) this.findViewById(R.id.etName);
        this.etDep = (EditText) this.findViewById(R.id.etDep);

        this.btAdd.setOnClickListener(this.handleAddEmployeeEvent);
        this.btCancel.setOnClickListener(this.handleCancelEvent);
    }

    private final View.OnClickListener handleAddEmployeeEvent = new View.OnClickListener() {
        @Override public void onClick(final View view) {
            Log.d(MainActivity.APP_TAG, "AddEmployee.");
            MvcViewAddEmployee.this.controller.addEmployee(
                    MvcViewAddEmployee.this.etName.getText().toString(),
                    MvcViewAddEmployee.this.etDep.getText().toString());
            finish();
        }
    };

    private final View.OnClickListener handleCancelEvent = new View.OnClickListener() {
        @Override public void onClick(final View view) {
            Log.d(MainActivity.APP_TAG, "Cancel Button clicked.");
            finish();
        }
    };

    @Override protected void onStart() {
        super.onStart();
    }

    @Override protected void onStop() {
        super.onStop();
    }
}
