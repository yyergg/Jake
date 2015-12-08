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
import android.widget.TextView;

import java.util.List;

public class FragmentProduct extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView lvProduct;

    private MvcControllerProduct controller;

    AlertDialog.Builder builder;
    AlertDialog dialogDelete;

    public static FragmentProduct newInstance(int sectionNumber) {
        FragmentProduct fragment = new FragmentProduct();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentProduct() {
    }

    private void populateProducts() {
        final List<String> products = this.controller.getProducts();
        Log.d(MainActivity.APP_TAG, String.format("%d found products ", products.size()));
        this.lvProduct.setAdapter(new ArrayAdapter<String>(
                        this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        products.toArray(new String[]{}))
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        lvProduct = (ListView) rootView.findViewById(R.id.lvProduct);
        this.controller = new MvcControllerProduct(this.getActivity());
        this.lvProduct.setOnItemLongClickListener(handleDeleteEvent);
        this.populateProducts();
        return rootView;
    }

    private final AdapterView.OnItemLongClickListener handleDeleteEvent = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(
                final AdapterView<?> parent,
                final View view,
                final int position,
                final long id) {
            final TextView v = (TextView) view;
            builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_confirm_delete).setTitle(R.string.dialog_title_alert);
            builder.setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FragmentProduct.this.controller.deleteProduct(v.getText().toString());
                    FragmentProduct.this.populateProducts();
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

    @Override
    public void onResume() {
        super.onResume();
        this.populateProducts();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
