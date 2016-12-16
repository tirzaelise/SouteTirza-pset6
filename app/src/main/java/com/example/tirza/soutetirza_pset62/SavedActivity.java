/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This activity shows the user the events that he saved. This is done by reading the Firebase
 * database using the method getSavedEvents. This activity also allows the user to delete events
 * from his saved list using the method deleteEvent and to share an event among his platform of
 * choice using the method shareEvent.
 */

package com.example.tirza.soutetirza_pset62;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {
    private EventAdapter adapter;
    private String id;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_saved);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(null);

        SharedPreferences sharedPrefs = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        id = sharedPrefs.getString("userId", "noId");

        if (id.equals("noId") || !sharedPrefs.contains("userId")) {
            String signIn = "Please sign in to see your saved events";
            Toast.makeText(this, signIn, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ListView listView = (ListView) findViewById(R.id.listView);

            listView.setAdapter(adapter);
            getSavedEvents(listView);
        }
    }

    /** Gets the events that the user has saved in the database */
    private void getSavedEvents(final ListView listView) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events = new ArrayList<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    events.add(event);
                    SavedActivity.this.events = events;
                }

                EventAdapter adapter = new EventAdapter(SavedActivity.this, events, false);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                SavedActivity.this.adapter = adapter;
                replaceTextInView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Context context = SavedActivity.this.getApplicationContext();
                Toast.makeText(context, "Failed to read database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Changes the visibility of the 'Loading saved events' text view if there are saved events or
     * changes the text if there are no saved events
     */
    private void replaceTextInView() {
        TextView loadingText = (TextView) findViewById(R.id.loadingText);

        if (events.size() == 0) {
            loadingText.setVisibility(View.VISIBLE);
            String noEvents = getResources().getString(R.string.noEvents);
            loadingText.setText(noEvents);
        } else {
            loadingText.setVisibility(View.GONE);
        }
    }

    /** Deletes an event from the database */
    public void deleteEvent(View view) {
        DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
        databaseHandler.deleteEvent(view);
        String eventName = databaseHandler.getClickedEvent(view);
        Event clickedEvent = databaseHandler.findEvent(eventName);

        events.remove(clickedEvent);
        adapter.notifyDataSetChanged();

        if (events.size() == 0) replaceTextInView();
    }

    /** Shares an event to the user's platform of choice */
    public void shareEvent(View view) {
        DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
        String eventName = databaseHandler.getClickedEvent(view);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);

        sharingIntent.setType("text/plain");
        String shareBody = "I would like to share " + eventName + "!";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        Intent chooser = Intent.createChooser(sharingIntent, "Share via");
        startActivity(chooser);
    }

    /** Adds items to the action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /** Defines what happens when an item in the action bar is clicked */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.searchMenu) {
            Intent searchActivity = new Intent(this, SearchActivity.class);
            startActivity(searchActivity);
            finish();
            return true;
        } else if (id == R.id.starMenu) {
            Intent savedActivity = new Intent(this, SavedActivity.class);
            startActivity(savedActivity);
            finish();
            return true;
        } else if (id == R.id.userMenu) {
            Intent accountActivity = new Intent(this, AccountActivity.class);
            startActivity(accountActivity);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}