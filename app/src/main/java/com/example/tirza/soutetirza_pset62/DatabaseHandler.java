/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file handles the database actions.
 */

package com.example.tirza.soutetirza_pset62;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class DatabaseHandler {
    private ArrayList<Event> events;
    private String id;

    /** Creates DatabaseHandler constructor */
    DatabaseHandler(ArrayList<Event> events, String id) {
        this.events = events;
        this.id = id;
    }

    /** Saves an event in the database */
    void saveEvent(View view) {
        String eventName = getClickedEvent(view);
        Event event = findEvent(eventName);

        if (event != null) {
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child(id).push().setValue(event);
        } else {
            ResultActivity resultActivity = new ResultActivity();
            Context context = resultActivity.getApplicationContext();
            Toast.makeText(context, "Could not find event", Toast.LENGTH_SHORT).show();
        }
    }

    /** Gets the title of the event that was clicked */
    String getClickedEvent(View view) {
        RelativeLayout layout = (RelativeLayout) view.getParent();
        TextView titleTextView = (TextView) layout.findViewById(R.id.showTitle);
        return titleTextView.getText().toString();
    }

    /** Finds the Event object and all the corresponding information using the title of an event */
    Event findEvent(String eventToGet) {
        for (Event event: events) {
            String eventName = event.getTitle();

            if (eventName.equals(eventToGet)) {
                return event;
            }
        }
        return null;
    }

    /** Deletes an event from the database */
    void deleteEvent(View view) {
        String eventName = getClickedEvent(view);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query eventToDelete = database.child(id).orderByChild("title").equalTo(eventName);

        eventToDelete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    eventSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                SavedActivity savedActivity = new SavedActivity();
                Context context = savedActivity.getApplicationContext();
                Toast.makeText(context, "Failed to delete event", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
