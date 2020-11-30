package com.example.teambeans;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ChatEnvironment.Message> messages;
    private RequestQueue queue;

    public MessageAdapter(Context context, ArrayList<ChatEnvironment.Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_tile, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MessageAdapter.MyViewHolder vh = new MessageAdapter.MyViewHolder(v); // pass the view to View Holder
        queue = Volley.newRequestQueue(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, final int position) {
        // Set the data in items
        holder.sender.setText((String) messages.get(position).getFrom());
        holder.messageText.setText((String) messages.get(position).getMessage());
        holder.timeSent.setText((String) messages.get(position).getTime());

        // implement setOnClickListener event on item view.
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go to chat environment for the item's conversation when item clicked
//                AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                Bundle args = new Bundle();
//                args.putString("Title", (String) inbox.get(position).getNamesDisplay());
//                Fragment nextFragment = new ChatEnvironment();
//                nextFragment.setArguments(args);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, nextFragment).addToBackStack(null).commit();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Declare the UI components of the inbox_element layout
        TextView sender;
        TextView timeSent;
        TextView messageText;

        public MyViewHolder(View itemView) {
            super(itemView);

            // Initialize the UI components of the inbox_element layout
            sender = (TextView) itemView.findViewById(R.id.sender);
            timeSent = (TextView) itemView.findViewById(R.id.messageText);
            messageText = (TextView) itemView.findViewById(R.id.timeSent);
        }
    }
}
