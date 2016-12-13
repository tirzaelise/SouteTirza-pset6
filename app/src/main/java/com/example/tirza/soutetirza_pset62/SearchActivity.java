/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This activity lets the user sign in and search for events given a location and optional keywords.
 */

package com.example.tirza.soutetirza_pset62;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        resetLastSearch();

        mAuth = FirebaseAuth.getInstance();
        setLoggedInListener();
    }

    /** Adds a listener to see if the user is logged in */
    public void setLoggedInListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    user = firebaseAuth.getCurrentUser();
                    Log.wtf("signed in", "onAuthStateChanged:signed_in:" + user.getUid());

                    SharedPreferences sharedPrefs = getSharedPreferences("userInfo",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("userId", user.getUid());
                    editor.apply();
                } else {
                    // User is signed out
                    Log.wtf("signed out", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    /** Sets the location and keywords in the EditTexts to the user's last search */
    public void resetLastSearch() {
        SharedPreferences sharedPrefs = this.getSharedPreferences("lastSearch",
                                                                  Context.MODE_PRIVATE);
        if (sharedPrefs.contains("lastLocation")) {
            String lastLocation = sharedPrefs.getString("lastLocation", "location");
            EditText giveLocation = (EditText) findViewById(R.id.giveLocation);
            giveLocation.setText(lastLocation);
            if (sharedPrefs.contains("keywords")) {
                String lastKeywords = sharedPrefs.getString("lastKeywords", "keywords");
                giveLocation.setText(lastKeywords);
            }
        }
    }

    /** Starts the EventAsyncTask to get search results if a location is given */
    public void search(View view) {
        EditText giveLocation = (EditText) findViewById(R.id.giveLocation);
        String location = giveLocation.getText().toString();
        EditText giveKeywords = (EditText) findViewById(R.id.giveKeywords);
        String keywords = giveKeywords.getText().toString();

        if (!(location.equals(""))) {
            location = location.replaceAll("\\s+","%20");
            keywords = keywords.replaceAll("\\s+", "%20");
            saveLastSearch(location, keywords);
            EventAsyncTask asyncTask = new EventAsyncTask(this);
            asyncTask.execute(location, keywords, "");
        } else {
            Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
        }
    }

    /** Saves the last search in Shared Preferences */
    private void saveLastSearch(String location, String keywords) {
        SharedPreferences sharedPrefs = this.getSharedPreferences("lastSearch",
                                                                  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("lastLocation", location);

        if (!keywords.equals("")) {
            editor.putString("lastKeywords", keywords);
        }
        editor.apply();
    }

    /** Retrieves the data from the AsyncTask and sends it to a new activity to show the results */
    public void setData(ArrayList<Event> events) {
        Intent showResult = new Intent(this, ResultActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("events", events);
        showResult.putExtras(extras);
        startActivity(showResult);
    }
}