package com.example.yyerg.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by yyerg on 2015/10/11.
 */
public class MvcViewAddOrder extends Activity {

    private static ArrayMap<String,Integer> mapProductionRate = new ArrayMap<String,Integer>();
    static{
        mapProductionRate.put("A",2);
        mapProductionRate.put("B",4);
        mapProductionRate.put("C",4);
        mapProductionRate.put("D",1);
    }

    private Intent intent;

    private Button btDate;
    private EditText etDescription;
    private Spinner spProductType;
    private EditText etProductAmount;
    private Button btAddProduct;
    private TableLayout tbOrder;
    private Button btCancel;
    private Button btSubmit;
    private TextView tvETA;

    private Integer year;
    private Integer month;
    private Integer day_of_month;

    private Integer orderId;

    DatePickerDialog dialogDatePicker;

    AlertDialog.Builder builder;
    AlertDialog dialogDelete;

    private MvcControllerOrder controllerOrder;
    private MvcControllerProduct controllerProduct;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.intent= getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_add_order);
        this.controllerOrder = new MvcControllerOrder(this);
        this.controllerProduct = new MvcControllerProduct(this);
        this.btDate = (Button) findViewById(R.id.btDate);
        this.btDate.setOnClickListener(handleDateClickEvent);
        this.etDescription = (EditText) findViewById(R.id.etDescription);
        this.spProductType = (Spinner) findViewById(R.id.spProduct);
        this.etProductAmount = (EditText) findViewById(R.id.etAmount);
        this.btAddProduct = (Button) findViewById(R.id.btAdd);
        this.btAddProduct.setOnClickListener(handleAddProductEvent);
        this.tbOrder = (TableLayout) findViewById(R.id.tbOrder);
        this.btCancel = (Button) findViewById(R.id.btCancel);
        this.btCancel.setOnClickListener(handleCancelEvent);
        this.btSubmit = (Button) findViewById(R.id.btSubmit);
        this.btSubmit.setOnClickListener(handleSubmitEvent);
        this.tvETA = (TextView) findViewById(R.id.tvEstimated);
        this.orderId = this.intent.getIntExtra("orderId", -1);
        if (this.orderId.equals(-1)){
            this.orderId = controllerOrder.getNewId();
        }
        populateDate();
        populateProduct();
        loadTable();
    }

    private final View.OnClickListener handleAddProductEvent = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            TableRow tR = new TableRow(MvcViewAddOrder.this);
            TextView colName = new TextView(MvcViewAddOrder.this);
            TextView colAmount = new TextView(MvcViewAddOrder.this);
            TextView colCategory = new TextView(MvcViewAddOrder.this);

            colName.setPadding(5, 5, 5, 5);
            colAmount.setPadding(5, 5, 5, 5);
            colCategory.setPadding(5, 5, 5, 5);

            colName.setText(spProductType.getSelectedItem().toString());
            colAmount.setText(etProductAmount.getText());
            colCategory.setText(controllerProduct.getCategory(spProductType.getSelectedItem().toString()));

            tR.addView(colName);
            tR.addView(colAmount);
            tR.addView(colCategory);
            tR.setOnLongClickListener(handleDeleteEvent);
            tbOrder.addView(tR);
            updateETA();
        }
    };

    private void loadTable(){
        Log.d(MainActivity.APP_TAG, "loadTable: "+this.orderId.toString());
        final List<MvcControllerOrder.ProductInOrder> products = this.controllerOrder.getProductInOrder(this.orderId);
        Integer i = 0;
        while(i<products.size()){
            TableRow tR = new TableRow(this);

            TextView colName = new TextView(this);
            colName.setPadding(5, 5, 5, 5);
            colName.setText(products.get(i).name);

            TextView colAmount = new TextView(this);
            colAmount.setPadding(5, 5, 5, 5);
            colAmount.setText(products.get(i).amount.toString());

            TextView colCategory = new TextView(this);
            colCategory.setPadding(5, 5, 5, 5);
            colCategory.setText(products.get(i).category.toString());

            tR.addView(colName);
            tR.addView(colAmount);
            tR.addView(colCategory);

            tR.setOnLongClickListener(handleDeleteEvent);
            this.etDescription.setText(this.intent.getStringExtra("description"));
            this.btDate.setText(this.intent.getStringExtra("date"));
            this.tbOrder.addView(tR);
            i++;
        }
        updateETA();
    }

    private void updateETA(){
        ArrayMap<String,Integer> mapProductAmount = new ArrayMap<String,Integer>();
        mapProductAmount.put("A",0);
        mapProductAmount.put("B",0);
        mapProductAmount.put("C",0);
        mapProductAmount.put("D",0);
        for(int i = 0; i < tbOrder.getChildCount(); i++) {
            TableRow tr = (TableRow)tbOrder.getChildAt(i);
            Integer amount = Integer.parseInt( ((TextView)tr.getChildAt(1)).getText().toString());
            String category = ((TextView)tr.getChildAt(2)).getText().toString();
            mapProductAmount.put(category, mapProductAmount.get(category) + amount);
        }
        Integer bottleneck = 0;
        for(int i = 0; i < mapProductAmount.size(); i++) {
            if(mapProductAmount.valueAt(i)>0){
                if(bottleneck < mapProductAmount.valueAt(i)/mapProductionRate.valueAt(i)){
                    bottleneck = mapProductAmount.valueAt(i)/mapProductionRate.valueAt(i);
                }
            }
        }
        this.tvETA.setText(bottleneck.toString());
    }


    private void populateProduct() {
        this.spProductType.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                controllerProduct.getProducts().toArray(new String[]{})));
    }

    private void populateDate() {
        if (this.btDate.getText().toString().equals("")) {
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

    private final View.OnClickListener handleDateClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogDatePicker = new DatePickerDialog(
                    MvcViewAddOrder.this,
                    myDateListener,
                    MvcViewAddOrder.this.year,
                    MvcViewAddOrder.this.getAndroidMonth(),
                    MvcViewAddOrder.this.day_of_month);
            dialogDatePicker.show();
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker dp, int year, int month, int day) {
            MvcViewAddOrder.this.year = year;
            MvcViewAddOrder.this.month = month + 1;
            MvcViewAddOrder.this.day_of_month = day;
            MvcViewAddOrder.this.populateDate();
        }
    };

    private final View.OnClickListener handleCancelEvent = new View.OnClickListener() {
        @Override public void onClick(final View view) {
            Log.d(MainActivity.APP_TAG, "Cancel Button clicked.");
            finish();
        }
    };

    private final View.OnClickListener handleSubmitEvent = new View.OnClickListener() {
        @Override public void onClick(final View view) {
            Log.d(MainActivity.APP_TAG, "Submit Button clicked.");
            controllerOrder.deleteOrder(orderId);
            for(int i = 0; i < tbOrder.getChildCount(); i++) {
                TableRow tr = (TableRow)tbOrder.getChildAt(i);
                String description = etDescription.getText().toString();
                String name = ((TextView)tr.getChildAt(0)).getText().toString();
                String amount = ((TextView)tr.getChildAt(1)).getText().toString();
                String date = btDate.getText().toString();
                String category = controllerProduct.getCategory(name);
                controllerOrder.AddSingleProduct(orderId,date,description,name,amount,category);
            }
            finish();
        }
    };

    private final View.OnLongClickListener handleDeleteEvent = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            builder = new AlertDialog.Builder(MvcViewAddOrder.this);
            builder.setMessage(R.string.dialog_confirm_delete).setTitle(R.string.dialog_title_alert);
            builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ViewGroup container = ((ViewGroup)v.getParent());
                    container.removeView(v);
                    container.invalidate();
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
    };

    @Override protected void onStart() {
        super.onStart();
    }

    @Override protected void onStop() {
        super.onStop();
    }
}
