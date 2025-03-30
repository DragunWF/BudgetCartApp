package com.example.registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registration.adapters.ItemAdapter;
import com.example.registration.data.Item;
import com.example.registration.helpers.DatabaseHelper;
import com.example.registration.helpers.Utils;
import com.example.registration.services.ItemService;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private View mainView;

    private TextView totalPriceText;
    private Button addBtn, clearBtn;

    private RecyclerView itemRecycler;
    private ItemAdapter itemAdapter;
    private RecyclerView.LayoutManager itemLayoutManager;

    private SearchView searchBar;

    @Override
    protected void onResume() {
        super.onResume();
        itemAdapter.updateDataSet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            DatabaseHelper.initialize(this);
            DatabaseHelper.addDummyData();

            bindElements();
            setButtons();
            setRecycler();
            setSearch();
            updateTotalPrice();

            Snackbar.make(mainView, "Welcome to the Budget Cart App!", Snackbar.LENGTH_SHORT).show();
        } catch(Exception err){
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void bindElements() {
        mainView = findViewById(R.id.main);
        totalPriceText = findViewById(R.id.totalPriceText);
        addBtn = findViewById(R.id.addBtn);
        clearBtn = findViewById(R.id.clearBtn);
        itemRecycler = findViewById(R.id.itemRecycler);
        searchBar = findViewById(R.id.searchBar);
    }

    private void setButtons() {
        addBtn.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, ItemFormActivity.class);
            intent.putExtra(ItemFormActivity.FORM_TYPE, ItemFormActivity.ADD_FORM);
            startActivity(intent);
        });
        clearBtn.setOnClickListener(v ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setPositiveButton("HELL YES DELETE EM ALL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemService.clear();
                    itemAdapter.updateDataSet();
                    updateTotalPrice();
                }
            });
            builder.setNegativeButton("NOOOO LET ME SAVE IT!!!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you really really really sure that you want to delete your cart?");

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setRecycler() {
        itemRecycler.setHasFixedSize(false);

        itemAdapter = new ItemAdapter(DatabaseHelper.getItemBank().getAll(), this, this);
        itemRecycler.setAdapter(itemAdapter);

        itemLayoutManager = new LinearLayoutManager(this);
        itemRecycler.setLayoutManager(itemLayoutManager);
    }

    private void setSearch() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                update(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                update(s);
                return false;
            }

            public void update(String query) {
                List<Item> itemList = DatabaseHelper.getItemBank().getAll();
                List<Item> results = new ArrayList<>();

                query = query.toLowerCase();
                for (Item item : itemList) {
                    String itemName = item.getName().toLowerCase();
                    if (itemName.contains(query)) {
                        results.add(item);
                    }
                }

                itemAdapter.updateDataSet(results);
            }
        });
    }

    public void updateTotalPrice() {
        totalPriceText.setText(String.format("Total Price: %s PHP", ItemService.getTotalPrice()));
    }
}