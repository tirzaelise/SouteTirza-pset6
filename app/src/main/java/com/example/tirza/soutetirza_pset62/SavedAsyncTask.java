/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This file uses the user's user id to read the events that he's saved in the database.
 */

package com.example.tirza.soutetirza_pset62;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

class SavedAsyncTask extends AsyncTask<String, String, ArrayList<Event>> {
    private SavedActivity activity;

    SavedAsyncTask(SavedActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Event> doInBackground(String... params) {
        ArrayList<Event> events = new ArrayList<>();
        String id = params[0];
        DatabaseHandler databaseHandler = new DatabaseHandler(events, id);
        databaseHandler.readDatabase();
        return databaseHandler.getEvents();
    }

    /** Creates an ArrayList of Event objects */
    protected void onPostExecute(ArrayList<Event> events) {
        this.activity.setListView(events);

    }
}
