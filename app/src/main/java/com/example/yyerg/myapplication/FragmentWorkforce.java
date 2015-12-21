package com.example.yyerg.myapplication;

import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class FragmentWorkforce extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner spDepartment;
    private Button btDate;
    private TableLayout tbWorkforce;
    private Integer year;
    private Integer month;
    private Integer day_of_month;

    private MvcControllerWorkforce controllerWorkforce;
    private MvcControllerEmployee controllerEmployee;

    DatePickerDialog.Builder builder;
    DatePickerDialog dialogDatePicker;

    public static FragmentWorkforce newInstance(int sectionNumber) {
        FragmentWorkforce fragment = new FragmentWorkforce();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentWorkforce() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workforce, container, false);
        this.controllerWorkforce = new MvcControllerWorkforce(this.getActivity());
        this.controllerEmployee = new MvcControllerEmployee(this.getActivity());
        this.tbWorkforce = (TableLayout) rootView.findViewById(R.id.tbWorkforce);
        this.btDate = (Button) rootView.findViewById(R.id.btDate);
        this.btDate.setOnClickListener(handleDateClickEvent);
        this.spDepartment = (Spinner) rootView.findViewById(R.id.spDepartment);
        this.spDepartment.setOnItemSelectedListener(handleChangeDepartmentEvent);
        this.populateDepartment();
        this.populateDate();
        this.populateWorkforces();
        return rootView;
    }

    private void populateWorkforces() {
        if (spDepartment.getSelectedItem() != null) {
            final String date = this.year.toString() + "-" + this.month.toString() + "-" + this.day_of_month.toString();
            final List<MvcControllerWorkforce.Workforce> workforces
                    = this.controllerWorkforce.getWorkforce(spDepartment.getSelectedItem().toString(),date);
            tbWorkforce.removeAllViews();
            Integer i = 0;
            while(i<workforces.size()){
                TableRow tR = new TableRow(this.getActivity());
                TextView colId = new TextView(this.getActivity());
                colId.setPadding(5, 5, 5, 5);
                TextView colName = new TextView(this.getActivity());
                colName.setPadding(5, 5, 5, 5);
                colId.setText(String.format("#%05d", workforces.get(i).id));
                colName.setText(workforces.get(i).name);
                Spinner spRemark = new Spinner(this.getActivity());
                Log.d(MainActivity.APP_TAG, "load: " + workforces.get(i).remark);
                final String[] remarktype = {
                        getString(R.string.spinner_attend),
                        getString(R.string.spinner_sick),
                        getString(R.string.spinner_vacation)};
                spRemark.setAdapter(new ArrayAdapter<String>(
                        this.getActivity(),
                        android.R.layout.simple_spinner_item,
                        remarktype));
                Log.d(MainActivity.APP_TAG, "after adapter: " + workforces.get(i).remark);
                spRemark.setSelection(getIDbyName(workforces.get(i).remark));
                tableOnItemSelectedListener handleChangeRemarkEvent = new tableOnItemSelectedListener(workforces.get(i).id);
                spRemark.setOnItemSelectedListener(handleChangeRemarkEvent);
                tR.addView(colId);
                tR.addView(colName);
                tR.addView(spRemark);

                tbWorkforce.addView(tR);
                i++;
            }
        }
    }

    private void populateDepartment() {
        this.spDepartment.setAdapter(new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_spinner_item,
                controllerEmployee.getDepartment().toArray(new String[]{})));
    }

    private void populateDate(){
        if(this.btDate.getText().toString().equals("")) {
            Calendar now = Calendar.getInstance();
            this.year = now.get(Calendar.YEAR);
            this.month = now.get(Calendar.MONTH) + 1;
            this.day_of_month = now.get(Calendar.DAY_OF_MONTH);
        }
        this.btDate.setText("    " + this.year.toString() + "-" + this.month.toString() + "-" + this.day_of_month.toString());
    }

    public Integer getAndroidMonth(){
        return this.month - 1;
    }

    private final View.OnClickListener handleDateClickEvent = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            dialogDatePicker = new DatePickerDialog(
                    getActivity(),
                    myDateListener,
                    FragmentWorkforce.this.year,
                    FragmentWorkforce.this.getAndroidMonth(),
                    FragmentWorkforce.this.day_of_month);
            dialogDatePicker.show();
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker dp, int year, int month, int day) {
            FragmentWorkforce.this.year = year;
            FragmentWorkforce.this.month = month;
            FragmentWorkforce.this.day_of_month = day;
            FragmentWorkforce.this.populateDate();
            FragmentWorkforce.this.populateWorkforces();
        }
    };

        private final Spinner.OnItemSelectedListener handleChangeDepartmentEvent = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            FragmentWorkforce.this.populateWorkforces();
        }

        @Override
        public void onNothingSelected(AdapterView arg0) {
            // TODO Auto-generated method stub
        }
    };


    public class tableOnItemSelectedListener implements AdapterView.OnItemSelectedListener
    {
        Integer workerid;
        public tableOnItemSelectedListener(Integer id) {
            this.workerid = id;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final String date = FragmentWorkforce.this.year.toString() + "-" + FragmentWorkforce.this.month.toString() + "-" + FragmentWorkforce.this.day_of_month.toString();
            Log.d(MainActivity.APP_TAG, "view:" + ((TextView)view).getText().toString() + "  " + this.workerid.toString());
            FragmentWorkforce.this.controllerWorkforce.editRemark(date, this.workerid, ((TextView)view).getText().toString());
        }

        @Override
        public void onNothingSelected(AdapterView arg0) {
            // TODO Auto-generated method stub
        }
    }


    Integer getIDbyName(String name){

        if(name.equals(getString(R.string.spinner_attend))){
            return 0;
        } else if(name.equals(getString(R.string.spinner_sick))){
            return 1;
        } else if(name.equals(getString(R.string.spinner_vacation))){
            return 2;
        } else{
            return 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.populateDepartment();
        this.populateDate();
        this.populateWorkforces();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
