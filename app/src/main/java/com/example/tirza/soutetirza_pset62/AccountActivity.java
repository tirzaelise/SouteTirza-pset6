/* Native App Studio: Assignment 6
 * Tirza Soute
 *
 * This activity allows the user to sign up, sign in and sign out.
 */

package com.example.tirza.soutetirza_pset62;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_account);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(null);


        mAuth = FirebaseAuth.getInstance();
        setLoggedInListener();
    }

    /** Starts registering listeners to the authentication state */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /** Stops registering listeners to the authentication state */
    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /** Adds a listener to check if the user is logged in */
    public void setLoggedInListener() {
        SharedPreferences sharedPrefs = getSharedPreferences("userInfo",
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefs.edit();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user = firebaseAuth.getCurrentUser();
                    changeVisibility(true);

                    editor.putString("userId", user.getUid()).apply();
                } else {
                    changeVisibility(false);
                    editor.remove("userId").apply();
                }
            }
        };
    }

    /**
     * Hides the text views to give an email address and password and sign in and up buttons if the
     * user is logged in, shows the sign out button
     * Hides the sign out button if the user is not logged in and shows the sign in and up buttons
     * and the text views to give an email address and password
     */
    private void changeVisibility(boolean loggedIn) {
        EditText giveEmail = (EditText) findViewById(R.id.giveEmail);
        EditText givePassword = (EditText) findViewById(R.id.givePassword);
        Button signIn = (Button) findViewById(R.id.signInButton);
        Button signUp = (Button) findViewById(R.id.signUpButton);
        Button signOut = (Button) findViewById(R.id.signOutButton);

        int loggedInButtons;
        int loggedOutButton;
        if (loggedIn) {
            loggedInButtons = View.INVISIBLE;
            loggedOutButton = View.VISIBLE;
        } else {
            loggedInButtons = View.VISIBLE;
            loggedOutButton = View.INVISIBLE;
        }

        giveEmail.setVisibility(loggedInButtons);
        givePassword.setVisibility(loggedInButtons);
        signIn.setVisibility(loggedInButtons);
        signUp.setVisibility(loggedInButtons);
        signOut.setVisibility(loggedOutButton);
    }

    /** Creates an account in the Firebase database */
    public void createAccount(View view) {
        final EditText giveEmail = (EditText) findViewById(R.id.giveEmail);
        email = giveEmail.getText().toString();
        final EditText givePassword = (EditText) findViewById(R.id.givePassword);
        password = givePassword.getText().toString();

        if (isValidEmail(email) && password.length() >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(AccountActivity.this, "Registration failed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AccountActivity.this, "Registration successful",
                                        Toast.LENGTH_SHORT).show();
                                giveEmail.setText("");
                                givePassword.setText("");
                            }
                        }
                    });
        } else if (email.length() < 6 && password.length() < 6){
            Toast.makeText(this, "Please enter a valid email address and password",
                    Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter a password of at least 6 characters",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Checks if an email address is valid */
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /** Signs in the user */
    public void signIn(View view) {
        final EditText giveEmail = (EditText) findViewById(R.id.giveEmail);
        email = giveEmail.getText().toString();
        final EditText givePassword = (EditText) findViewById(R.id.givePassword);
        password = givePassword.getText().toString();

        if (email.equals("")) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        } else if (email.equals("") && password.equals("")) {
            Toast.makeText(this, "Please enter an email address and a password",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(AccountActivity.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AccountActivity.this, "Signed in successfully",
                                        Toast.LENGTH_SHORT).show();
                                hideKeyboard(giveEmail);
                                hideKeyboard(givePassword);
                            }
                        }
                    });
        }
    }

    /** Signs out the user if the user is logged in */
    public void signOut(View view) {
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /** Hides the keyboard */
    public void hideKeyboard(View view) {
        Context context = getApplicationContext();
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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