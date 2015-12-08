package com.example.yyerg.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class FragmentOrder extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private TableLayout tbOrder;

    private MvcControllerOrder controllerOrder;

    public static FragmentOrder newInstance(int sectionNumber) {
        FragmentOrder fragment = new FragmentOrder();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        this.controllerOrder = new MvcControllerOrder(this.getActivity());
        this.tbOrder = (TableLayout) rootView.findViewById(R.id.tbOrder);

        this.populateOrders();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.populateOrders();
    }

    private void populateOrders() {
        final List<MvcControllerOrder.Order> orders = this.controllerOrder.getOrder();
        tbOrder.removeAllViews();
        Integer i = 0;
        while(i<orders.size()){
            final Integer orderId = orders.get(i).id;
            TableRow tR = new TableRow(this.getActivity());

            TextView colId = new TextView(this.getActivity());
            colId.setPadding(5, 5, 5, 5);
            TextView colDescription = new TextView(this.getActivity());
            colDescription.setPadding(5, 5, 5, 5);
            TextView colDate = new TextView(this.getActivity());
            colDate.setPadding(5, 5, 5, 5);

            colId.setText(String.format("#%05d", orderId));
            colDescription.setText(orders.get(i).description);
            colDate.setText(orders.get(i).date);

            tR.addView(colId);
            tR.addView(colDescription);
            tR.addView(colDate);

            final String dateToPass = orders.get(i).date;
            final String descriptionToPass = orders.get(i).description;

            tR.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(getActivity(), MvcViewAddOrder.class);
                    Bundle b =new Bundle();
                    b.putInt("orderId", orderId);
                    b.putString("date", dateToPass);
                    b.putString("description",descriptionToPass);
                    intent.putExtras(b);
                    startActivity(intent);
                    return true;
                }
            });

            tbOrder.addView(tR);
            i++;
        }
    }
}
