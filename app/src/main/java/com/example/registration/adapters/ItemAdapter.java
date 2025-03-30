package com.example.registration.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.registration.ItemFormActivity;
import com.example.registration.MainActivity;
import com.example.registration.R;
import com.example.registration.data.Item;
import com.example.registration.helpers.DatabaseHelper;
import com.example.registration.helpers.Utils;
import com.example.registration.services.ItemService;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> localDataSet;
    private Context context;
    private MainActivity mainActivity;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText, quantityText, priceText;
        private final Button editBtn, deleteBtn;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nameText = view.findViewById(R.id.nameText);
            quantityText = view.findViewById(R.id.quantityText);
            priceText = view.findViewById(R.id.priceText);

            editBtn = view.findViewById(R.id.editBtn);
            deleteBtn = view.findViewById(R.id.deleteBtn);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getQuantityText() {
            return quantityText;
        }

        public TextView getPriceText() {
            return priceText;
        }

        public Button getEditBtn() {
            return editBtn;
        }

        public Button getDeleteBtn() {
            return deleteBtn;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public ItemAdapter(List<Item> dataSet, Context context, MainActivity mainActivity) {
        localDataSet = dataSet;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Item item = localDataSet.get(position);
        viewHolder.getNameText().setText(item.getName());
        viewHolder.getQuantityText().setText(String.valueOf(item.getQuantity()));
        viewHolder.getPriceText().setText(item.getPrice() + " PHP");

        viewHolder.getEditBtn().setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemFormActivity.class);
            intent.putExtra(ItemFormActivity.FORM_TYPE, ItemFormActivity.EDIT_FORM);
            intent.putExtra(ItemFormActivity.VIEWED_ITEM_ID, item.getId());
            context.startActivity(intent);
        });
        viewHolder.getDeleteBtn().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton("Begone!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ItemService.delete(item.getId());
                    updateDataSet();
                    mainActivity.updateTotalPrice();
                    Utils.longToast(item.getName() + " has been deleted!", context);
                }
            });
            builder.setNegativeButton("No don't!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to delete " + item.getName() + " from the cart?");

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void updateDataSet() {
        List<Item> items = DatabaseHelper.getItemBank().getAll();
        updateDataSet(items);
    }

    public void updateDataSet(List<Item> updatedDataSet) {
        localDataSet.clear();
        for (Item item : updatedDataSet) {
            localDataSet.add(item);
        }
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
