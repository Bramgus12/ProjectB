package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bramgussekloo.projectb.R;
import com.google.firebase.auth.FirebaseAuth;

public class Beheerder extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beheerder);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Beheerder"); // sets title for toolbar


}






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu); // inflates menu from XML to objects

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) { // switch case for detecting which menu item is clicked

            case R.id.action_logout_bttn: // triggers when logout button is clicked

                logOut(); // logs out user


                return true;

            default:

                return false;


        }

    }

    private void logOut() {
        mAuth.signOut();
        sendTologin();
    }

    private void sendTologin() {

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish(); // ensures user can't go back
    }
}

