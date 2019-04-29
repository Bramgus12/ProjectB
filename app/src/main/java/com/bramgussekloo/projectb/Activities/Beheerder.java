package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.LoginActivity;
import com.bramgussekloo.projectb.Activities.Login.ResetEmail;
import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bramgussekloo.projectb.fragments.HistoryBeheerderFragment;
import com.bramgussekloo.projectb.fragments.HomeBeheerderFragment;
import com.bramgussekloo.projectb.fragments.ReservationsBeheerderFragment;

public class Beheerder extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    private BottomNavigationView mainBeheerderBottomNav;

    private HomeBeheerderFragment homeBeheerderFragment;
    private ReservationsBeheerderFragment reservationsBeheerderFragment;
    private HistoryBeheerderFragment historyBeheerderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beheerder);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Beheerder"); // sets title for toolbar

        mainBeheerderBottomNav = findViewById(R.id.mainBeheerderBottomNav);

        homeBeheerderFragment = new HomeBeheerderFragment();
        reservationsBeheerderFragment = new ReservationsBeheerderFragment();
        historyBeheerderFragment = new HistoryBeheerderFragment();

        replaceFragment(homeBeheerderFragment);


        mainBeheerderBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){

                    case R.id.bottom_nav_home:

                        replaceFragment(homeBeheerderFragment);

                        menuItem.setChecked(true);


                        return true;

                    case R.id.bottom_nav_reservations:

                        replaceFragment(reservationsBeheerderFragment);

                        menuItem.setChecked(true);


                        return true;

                    case R.id.bottom_nav_history:

                        replaceFragment(historyBeheerderFragment);

                        menuItem.setChecked(true);


                        return true;

                }

                return false;
            }
        });


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
            case R.id.action_resetPassword_buttn:
                resetpassword();

                return true;
            case R.id.action_ChangeEmail_bttn:
                Intent changeEmail = new Intent(getBaseContext(), ResetEmail.class);
                startActivity(changeEmail);
                return true;
            default:

                return false;


        }

    }

    private void logOut() {
        mAuth.signOut();
        sendTologin();
    }
    private void resetpassword(){
        if (user.getEmail() != null)
            mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Beheerder.this, "Mail was sent succesful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Beheerder.this, "Mail not sent succesful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void sendTologin() {

        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
        finish(); // ensures user can't go back
    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_beheerder_container, fragment);
        fragmentTransaction.commit();


    }
}

