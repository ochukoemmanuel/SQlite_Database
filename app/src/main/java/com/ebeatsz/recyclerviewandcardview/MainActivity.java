package com.ebeatsz.recyclerviewandcardview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // references of buttons and other controls on the layout
    Button btn_viewAll, btn_add;
    TextView et_name, et_age;
    Switch sw_active_customer;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_viewAll = findViewById(R.id.btn_viewAll);
        btn_add = findViewById(R.id.btn_add);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_active_customer = findViewById(R.id.sw_active_customer);
        lv_customerList = findViewById(R.id.lv_customerList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowCustomersOnListView(dataBaseHelper);

        // Set onclickListeners
        btn_add.setOnClickListener((v) -> {

                CustomerModel customerModel;

                try {
                    customerModel = new CustomerModel(-1, et_name.getText().toString().trim(), Integer.parseInt(et_age.getText().toString()), sw_active_customer.isChecked());
                    Toast.makeText(MainActivity.this, customerModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error Creating customer", Toast.LENGTH_SHORT).show();
                    customerModel = new CustomerModel(-1, "error", 0, false);
                }

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);

                boolean success = dataBaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "Success " + success, Toast.LENGTH_SHORT).show();
            ShowCustomersOnListView(dataBaseHelper);
        });

        btn_viewAll.setOnClickListener((v) -> {

            ShowCustomersOnListView(dataBaseHelper);

//                Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();

        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedCustomer);
                ShowCustomersOnListView(dataBaseHelper);

                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowCustomersOnListView(DataBaseHelper dataBaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}