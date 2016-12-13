/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This activity shows the user the events that he saved.
 */

package com.example.tirza.soutetirza_pset62;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {
    private EventAdapter adapter;
    private String id;
    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_saved);

        SharedPreferences sharedPrefs = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        id = sharedPrefs.getString("userId", "noId");

        if (id.equals("noId") || !sharedPrefs.contains("userId")) {
            String signIn = "Please sign in to see your saved events";
            Toast.makeText(this, signIn, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            SavedAsyncTask asyncTask = new SavedAsyncTask(this);
            asyncTask.execute(id);
        }
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
            return true;
        } else if (id == R.id.starMenu) {
            Intent savedActivity = new Intent(this, SavedActivity.class);
            startActivity(savedActivity);
            return true;
        } else if (id == R.id.userMenu) {
            Intent accountActivity = new Intent(this, AccountActivity.class);
            startActivity(accountActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Sets the obtained data in the ListView */
    public void setListView(ArrayList<Event> events) {
        adapter = new EventAdapter(this, events, false);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        this.events = events;
    }

    /** Deletes an event from the database */
    public void deleteEvent(View view) {
        DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
        databaseHandler.deleteEvent(view);
        String eventName = databaseHandler.getClickedEvent(view);
        Event clickedEvent = databaseHandler.findEvent(eventName);
        events.remove(clickedEvent);
        adapter.notifyDataSetChanged();
    }

    /** Shares an event to the user's platform of choice */
    public void shareEvent(View view) {
        DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
        String eventName = databaseHandler.getClickedEvent(view);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "I would like to share " + eventName + "!";
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}