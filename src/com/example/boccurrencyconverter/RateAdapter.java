package com.example.boccurrencyconverter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class RateAdapter extends ArrayAdapter {
    // we use the constructor allowing to provide a List of objects for the data
    // to be binded.
	
	Resources res;
	int selected;
	
    public RateAdapter(Context context, int textViewResourceId,
            List objects, Resources res, int selected) {
        super(context, textViewResourceId, objects);
        this.res = res;
        this.selected = selected;
    }
    
    public void setSelected (int selected) {
    	this.selected = selected;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        // retrieve the Person object binded at this position
        final Currency c = (Currency) getItem(position);
 
        // A ViewHolder keeps references to children views to avoid unneccessary
        // calls
        // to findViewById() on each row.
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_row, null);
 
            // Creates a ViewHolder and store references to the two children
            // views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.label = (TextView) convertView
                    .findViewById(R.id.label);
            holder.flag = (ImageView) convertView.findViewById(R.id.flag);
            holder.flag.setFocusable(false);
            holder.flag.setFocusableInTouchMode(false);
            holder.cbk = (CheckBox) convertView
                    .findViewById(R.id.cbk);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
 
        // Bind the data efficiently with the holder.
        holder.label.setText(c.getLabel());
        
        String mDrawableName = c.getShortcode().toLowerCase() + "_flag";
        int resID = res.getIdentifier(mDrawableName , "drawable", MainActivity.PACKAGE_NAME);
        holder.flag.setImageResource(resID);
        
        if (position == selected) {
        	holder.cbk.setChecked(true);
        } else {
        	holder.cbk.setChecked(false);
        }
        
        return convertView;
    }
 
    /**
     *
     * Inner holder class for a single row view in the ListView
     *
     */
    static class ViewHolder {
        public CheckBox cbk;
		public TextView label;
        ImageView flag;
    }
 
}