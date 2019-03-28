package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bramgussekloo.projectb.R;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        createToolbar();

    }

    public void createToolbar() {

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Admin");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout_bttn:

                logOut();

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
        finish();
    }
}
