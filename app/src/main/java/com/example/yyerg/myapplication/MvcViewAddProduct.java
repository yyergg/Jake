package com.example.yyerg.myapplication;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

/**
 * Created by yyerg on 2015/10/11.
 */
public class MvcViewAddProduct extends Activity {

    private Button btAddProduct;
    private Button btCancel;
    private EditText etAddProduct;
    private Spinner spCategory;

    private MvcControllerProduct controller;

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_add_product);
        this.controller = new MvcControllerProduct(this);
        this.btAddProduct = (Button) this.findViewById(R.id.btAddProduct);
        this.btCancel = (Button) this.findViewById(R.id.btCancelProduct);
        this.etAddProduct = (EditText) this.findViewById(R.id.etAddProduct);
        this.spCategory = (Spinner) this.findViewById(R.id.spCategory);
        final String[] category = {
                "A",
                "B",
                "C",
                "D"};
        this.spCategory.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                category));
        this.btAddProduct.setOnClickListener(this.handleAddProductEvent);
        this.btCancel.setOnClickListener(this.handleCancelEvent);
    }

    private final View.OnClickListener handleAddProductEvent = new View.OnClickListener() {
        @Override public void onClick(final View view) {
            Log.d(MainActivity.APP_TAG, "AddProduct Button clicked.");
            MvcViewAddProduct.this.controller.addProduct(
                    MvcViewAddProduct.this.etAddProduct.getText().toString(),
                    MvcViewAddProduct.this.spCategory.getSelectedItem().toString());
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
