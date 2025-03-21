package com.example.registration.helpers;

import android.content.SharedPreferences;

import com.example.registration.data.Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ModelBank<T extends Model> {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private String modelKey;
    private Class<T> modelClass;

    private static Gson gson = new Gson();

    public ModelBank(SharedPreferences sharedPref, SharedPreferences.Editor editor, String modelKey, Class<T> modelClass) {
        this.sharedPref = sharedPref;
        this.editor = editor;
        this.modelKey = modelKey;
        this.modelClass = modelClass;
    }

    public List<T> getAll() {
        String json = sharedPref.getString(modelKey, "[]");
        Type type = TypeToken.getParameterized(List.class, modelClass).getType();
        return gson.fromJson(json, type);
    }

    public T get(int id) {
        for (T model : getAll()) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public void add(T model) {
        List<T> models = getAll();
        model.setId(getLatestId());
        models.add(model);
        save(models);
    }

    public void update(T updatedModel) {
        List<T> models = getAll();
        for (int i = 0; i < models.size(); i++) {
            T model = models.get(i);
            if (model.getId() == updatedModel.getId()) {
                models.set(i, updatedModel);
                save(models);
                return;
            }
        }
    }

    public void delete(int id) {
        List<T> models = getAll();
        List<T> updatedModels = new ArrayList<>();
        for (T model : models) {
            if (model.getId() != id) {
                updatedModels.add(model);
            }
        }
        save(updatedModels);
    }

    public void save(List<T> updatedModels) {
        String json = gson.toJson(updatedModels);
        editor.putString(modelKey, json);
        editor.apply();
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }

    private int getLatestId() {
        int maxId = 0;
        for (T model : getAll()) {
            if (model.getId() > maxId) {
                maxId = model.getId();
            }
        }
        return maxId + 1;
    }
}
