package com.example.yyerg.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class FragmentEmployee extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner spDepartment;
    private TableLayout tbEmployee;

    private MvcControllerEmployee controller;

    AlertDialog.Builder builder;
    AlertDialog dialogDelete;

    public static FragmentEmployee newInstance(int sectionNumber) {
        FragmentEmployee fragment = new FragmentEmployee();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEmployee() {
    }

    private void populateEmployees() {
        if (spDepartment.getSelectedItem() != null) {
            final List<MvcControllerEmployee.EmployeeDetail> employees
                    = this.controller.getEmployeeDetail(spDepartment.getSelectedItem().toString());
            tbEmployee.removeAllViews();
            Integer i = 0;
            while(i<employees.size()){
                TableRow tR = new TableRow(this.getActivity());
                TextView colId = new TextView(this.getActivity());
                colId.setPadding(5,5,5,5);
                TextView colName = new TextView(this.getActivity());
                colName.setPadding(5,5,5,5);
                colId.setText(String.format("#%05d", employees.get(i).id));
                colName.setText(employees.get(i).name);
                tR.addView(colId);
                tR.addView(colName);
                tR.setTag(employees.get(i).id);
                tR.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final Integer tag = (Integer)v.getTag();
                        Log.d(MainActivity.APP_TAG,"Tag:"+tag.toString());
                        builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(R.string.dialog_confirm_delete).setTitle(R.string.dialog_title_alert);
                        builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FragmentEmployee.this.controller.deleteEmployee(tag);
                                FragmentEmployee.this.populateDepartment();
                                FragmentEmployee.this.populateEmployees();
                            }
                        });
                        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Do nothing
                            }
                        });
                        dialogDelete = builder.create();
                        dialogDelete.show();
                        return true;
                    }
                });
                tbEmployee.addView(tR);
                i++;
            }
        }
    }

    private void populateDepartment() {
        this.spDepartment.setAdapter(new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_spinner_item,
                controller.getDepartment().toArray(new String[]{})));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_employee, container, false);
        this.controller = new MvcControllerEmployee(this.getActivity());
        this.tbEmployee = (TableLayout) rootView.findViewById(R.id.tbEmployee);
        this.spDepartment = (Spinner) rootView.findViewById(R.id.spDepartment);
        this.spDepartment.setOnItemSelectedListener(handleChangeDepartmentEvent);
        this.populateDepartment();
        this.populateEmployees();
        return rootView;
    }

    private final Spinner.OnItemSelectedListener handleChangeDepartmentEvent = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            FragmentEmployee.this.populateEmployees();
        }

        @Override
        public void onNothingSelected(AdapterView arg0) {
            // TODO Auto-generated method stub
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        this.populateDepartment();
        this.populateEmployees();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
