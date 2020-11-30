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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Adapts InboxElement data for display in the conversation list recycler view for the inbox screen.
 * @author Kaitlyn Hoopingarner (kjh04)
 * @see Inbox
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Inbox.InboxElement> inbox;
    private RequestQueue queue;

    /**
     * Constructs a new inbox adapter.
     * @param context Context of the activity.
     * @param inbox ArrayList of inbox element data.
     */
    public InboxAdapter(Context context, ArrayList<Inbox.InboxElement> inbox) {
        this.context = context;
        this.inbox = inbox;
    }

    /**
     * Called when a view holder is created.
     * Inflates the layout of the item that gets displayed in the list.
     * @param parent View that the element gets inflated into.
     * @param viewType Type of view.
     * @return View holder for the list element.
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_element, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        queue = Volley.newRequestQueue(context);
        return vh;
    }

    /**
     * Called when a view holder is bound.
     * Sets the data of the item in the list.
     * @param  holder View holder to set the data for.
     * @param position Position in the inbox data list.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        // Set the data in items
        holder.namesInConvo.setText((String) inbox.get(position).getNamesDisplay());
//        holder.convoMessagePreview.setText((String) inbox.get(position).getMessagePreviewDisplay());
        holder.convoMessagePreview.setText((String) "");
        holder.timeOfLatestMessage.setText((String) inbox.get(position).getTimeDisplay());

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to chat environment for the item's conversation when item clicked
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle args = new Bundle();
                args.putString("Title", (String) inbox.get(position).getNamesDisplay());
                args.putString("conversationId", (String) inbox.get(position).getId());
                Fragment nextFragment = new ChatEnvironment();
                nextFragment.setArguments(args);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, nextFragment).addToBackStack(null).commit();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Open a delete verification dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.dialog_message).setTitle("Delete Conversation?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO Delete the conversation and all associated messages
                        // Close dialog
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
//                dialog.show();

                return false;
            }
        });
    }

    /**
     * Gets the number of items in the inbox data list.
     * @return Number of items in inbox.
     */
    @Override
    public int getItemCount() {
        return inbox.size();
    }

    /**
     * Defines the UI elements of the view holder's list item layout.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Declare the UI components of the inbox_element layout
        TextView namesInConvo;
        TextView convoMessagePreview;
        TextView timeOfLatestMessage;

        public MyViewHolder(View itemView) {
            super(itemView);

            // Initialize the UI components of the inbox_element layout
            namesInConvo = (TextView) itemView.findViewById(R.id.names);
            convoMessagePreview = (TextView) itemView.findViewById(R.id.messagePreview);
            timeOfLatestMessage = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
