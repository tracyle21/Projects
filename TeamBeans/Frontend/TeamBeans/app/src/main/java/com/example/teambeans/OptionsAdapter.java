package com.example.teambeans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Adapter for displaying search results in Dashboard screen
 * @author Kathryn Rohlfing (kathrynr) with role updates by Renee Teoh
 * @see Dashboard
 */
public class OptionsAdapter extends ArrayAdapter<String> {
    CheckBox check;
    String name;
    Context context;
    int type;
    ArrayList<String> names;
    ArrayList<Boolean> filter;

    /**
     * Creates a SearchAdapter given a context and the list of results
     * @param context Context in which to add SearchAdapter
     * @param names List of option names to display initially
     */
    public OptionsAdapter(Context context, ArrayList<String> names, int type) {
        super(context, 0, names);
        this.names = names;
        this.context = context;
        this.type = type;
        filter = new ArrayList<Boolean>();
        for (int i=0; i < names.size(); i++) {
            filter.add(i, false);
        }
    }
    public ArrayList<Boolean> getThisFilter()
    {
        return this.filter;
    }
    public ArrayList<String> getThisNames()
    {
        return this.names;
    }

    /**
     * Sets up view and initializes variables for one line of search results
     * @param position Position of the current item in the list of results
     * @param convertView View inflated from search xml layout
     * @param parent Parent ViewGroup used to inflate convertView
     * @return View of current line
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        name = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if(type==1)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.options1, parent, false);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.options2, parent, false);
        }

        final TextView optionName = (TextView) convertView.findViewById(R.id.optionName);
        check = (CheckBox) convertView.findViewById(R.id.checkbox);
        optionName.setText(name);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                filter.set(position, checked);
                // Check which checkbox was clicked
            }
        });
        boolean c = check.isChecked();

        return convertView;
    }
}