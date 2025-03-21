package com.example.registration.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.registration.data.Item;
import com.example.registration.services.ItemService;

public class DatabaseHelper {
    private static final String FILE_KEY = "db";

    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private static ModelBank<Item> itemBank;

    public static void initialize(Context context) {
        sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        itemBank = new ModelBank<>(sharedPref, editor, "items", Item.class);
    }

    public static ModelBank<Item> getItemBank() {
        return itemBank;
    }

    public static void addDummyData(){
        if(itemBank.getAll().isEmpty()){
            ItemService.add(new Item("Ballpen", 3, 45.50));
            ItemService.add(new Item("Earbud", 1, 949.50));
            ItemService.add(new Item("Phone Stand", 1, 99));
            ItemService.add(new Item("Socks", 5, 80));
            ItemService.add(new Item("Magnet", 3, 60));
            ItemService.add(new Item("Noodles", 12, 45.99));
            ItemService.add(new Item("Keyboard", 1, 2599));
            ItemService.add(new Item("Monitor", 1, 8900));
            ItemService.add(new Item("Power Bank", 1, 450));
            ItemService.add(new Item("ID Holder", 1, 99));
            ItemService.add(new Item("T-Shirt", 2, 249.50));
        }
    }
}
