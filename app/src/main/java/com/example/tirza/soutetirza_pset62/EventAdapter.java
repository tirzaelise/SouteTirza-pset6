/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file implements a custom adapter (extending the BaseAdapter) to give the obtained
 * information about the events.
 */

package com.example.tirza.soutetirza_pset62;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Event> data;
    private Boolean resultList;
    private LayoutInflater inflater;
    private TextView title;
    private TextView date;
    private TextView venue;
    private TextView location;
    private TextView description;
    private Event event;

    EventAdapter(Context context, ArrayList<Event> data, Boolean resultList) {
        this.context = context;
        this.data = data;
        this.resultList = resultList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /** Sets the information about the event in the ListView */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        int layout;

        if (resultList) layout = R.layout.row_result;
        else layout = R.layout.row_saved;

        if (convertView == null) {
            rowView = inflater.inflate(layout, parent, false);
            getTextViewsAndEvent(rowView, position);

            title.setText(event.getTitle());
            date.setText(event.getDate());
            venue.setText(event.getVenue());
            location.setText(event.getLocation());
            String descriptionString = context.getResources().getString(R.string.noDescription);
            if (event.getDescription().equals("")) description.setText(descriptionString);
            else description.setText(event.getDescription());

        } else {
            getTextViewsAndEvent(rowView, position);

            title.setText(this.data.get(position).getTitle());
            date.setText(this.data.get(position).getDate());
            venue.setText(this.data.get(position).getVenue());
            location.setText(this.data.get(position).getLocation());
            String descriptionString = context.getResources().getString(R.string.noDescription);
            if (event.getDescription().equals("")) description.setText(descriptionString);
            else description.setText(event.getDescription());
        }
        return rowView;
    }

    /** Initialises values for the event and the text views that the data has to be set in*/
    private void getTextViewsAndEvent(View view, int position) {
        event = (Event) getItem(position);

        title = (TextView) view.findViewById(R.id.showTitle);
        date = (TextView) view.findViewById(R.id.showDate);
        venue = (TextView) view.findViewById(R.id.showVenue);
        location = (TextView) view.findViewById(R.id.showLocation);
        description = (TextView) view.findViewById(R.id.showDescription);
    }
}
