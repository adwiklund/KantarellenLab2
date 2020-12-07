package com.example.kantarellen.Recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kantarellen.R;

import java.util.ArrayList;

public class RecipeItemAdapter extends
        RecyclerView.Adapter<RecipeItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView amountTextView;
        //public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.item_name);
            amountTextView = (TextView) itemView.findViewById(R.id.item_amount);
            //messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }

    private ArrayList<String> mItems;
    private ArrayList<String> mAmounts;

    public RecipeItemAdapter(ArrayList<String> items, ArrayList<String> amounts) {
        mItems = items;
        mAmounts = amounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        String item = mItems.get(position);
        String amount = mAmounts.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        TextView amountView = holder.amountTextView;
        //textView.setText(item.getItemName());
        textView.setText(item);
        amountView.setText(amount);
        /*
        Button button = holder.messageButton;
        button.setText(contact.isOnline() ? "Message" : "Offline");
        button.setEnabled(contact.isOnline());

         */
    }

    @Override
    public int getItemCount() {
            return mItems.size();
    }


}
