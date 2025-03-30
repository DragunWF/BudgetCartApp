package com.example.registration.services;

import com.example.registration.data.Item;
import com.example.registration.helpers.DatabaseHelper;
import com.example.registration.helpers.ModelBank;

public class ItemService {
    public static void add(Item item) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.add(item);
    }

    public static void edit(Item updatedItem) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.update(updatedItem);
    }

    public static void delete(int id) {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.delete(id);
    }

    public static void clear() {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        bank.clear();
    }

    public static double getTotalPrice() {
        ModelBank<Item> bank = DatabaseHelper.getItemBank();
        double totalPrice = 0;
        for (Item item : bank.getAll()) {
            totalPrice += item.getPrice();
        }
        return (double) Math.round(totalPrice * 100) / 100;
    }
}
