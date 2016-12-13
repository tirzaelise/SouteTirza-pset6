/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This activity shows the user's search results. He can also save an event if he is interested.
 */

package com.example.tirza.soutetirza_pset62;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    ArrayList<Event> events;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_result);

        SharedPreferences sharedPrefs = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        id = sharedPrefs.getString("userId", "noId");

        Bundle extras = getIntent().getExtras();
        events = (ArrayList<Event>) extras.getSerializable("events");
        setListView();
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
    private void setListView() {
        EventAdapter adapter = new EventAdapter(this, events, true);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    /** Gets the event that was clicked to save it to the database if the user is logged in */
    public void saveEvent(View view) {
        if (id.equals("noId")) {
            Toast.makeText(this, "Please sign in to save events", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
            databaseHandler.saveEvent(view);
        }
    }
}
