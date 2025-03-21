package com.example.registration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.registration.data.Item;
import com.example.registration.helpers.DatabaseHelper;
import com.example.registration.helpers.Utils;
import com.example.registration.services.ItemService;

public class ItemFormActivity extends AppCompatActivity {
    public static final String FORM_TYPE = "formType";
    public static final String ADD_FORM = "addForm";
    public static final String EDIT_FORM = "editForm";

    public static final String VIEWED_ITEM_ID = "viewedItemId";

    private EditText nameText, quantityText, priceText;
    private Button saveBtn, resetBtn;

    private int viewedItemId = 0;
    private boolean isEditForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_form);

        try {
            DatabaseHelper.initialize(this);
            isEditForm = getIntent().getStringExtra(FORM_TYPE).equals(EDIT_FORM);

            bindElements();
            setButtons();

            if (isEditForm) {
                viewedItemId = getIntent().getIntExtra(VIEWED_ITEM_ID, 0);
                autoFillTextFields();
            }
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void bindElements() {
        nameText = findViewById(R.id.nameText);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);

        saveBtn = findViewById(R.id.saveBtn);
        resetBtn = findViewById(R.id.resetBtn);
    }

    private void setButtons() {
        saveBtn.setOnClickListener(v -> {
            String name = Utils.getText(nameText);
            String quantityStr = Utils.getText(quantityText);
            String priceStr = Utils.getText(priceText);

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Utils.longToast("All fields are required!", this);
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            AlertDialog.Builder builder = new AlertDialog.Builder(ItemFormActivity.this);
            builder.setPositiveButton("Yes, let's save it!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (isEditForm) {
                        Item item = DatabaseHelper.getItemBank().get(viewedItemId);
                        item.setName(name);
                        item.setQuantity(quantity);
                        item.setPrice(price);

                        ItemService.edit(item);
                        Utils.longToast(name + " has been successfully edited!", ItemFormActivity.this);
                    } else {
                        ItemService.add(new Item(name, quantity, price));
                        Utils.longToast(name + " has been added to the cart", ItemFormActivity.this);
                    }

                    finish();
                }
            });
            builder.setNegativeButton("Nope!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to save this " + name + "?");
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        resetBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes Please!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    nameText.setText("");
                    quantityText.setText("");
                    priceText.setText("");
                    Utils.toast("Text fields have been reset!", ItemFormActivity.this);
                }
            });
            builder.setNegativeButton("Hell Nah", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to reset the text fields?");

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void autoFillTextFields() {
        Item item = DatabaseHelper.getItemBank().get(viewedItemId);
        nameText.setText(item.getName());
        quantityText.setText(String.valueOf(item.getQuantity()));
        priceText.setText(String.valueOf(item.getPrice()));
    }
}