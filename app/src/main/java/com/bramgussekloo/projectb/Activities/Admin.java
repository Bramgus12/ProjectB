package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.EditProduct.ChooseProduct;
import com.bramgussekloo.projectb.Activities.Login.LoginActivity;
import com.bramgussekloo.projectb.Activities.Login.ResetEmail;
import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.bramgussekloo.projectb.fragments.HistoryAdminFragment;
import com.bramgussekloo.projectb.fragments.HomeAdminFragment;
import com.bramgussekloo.projectb.fragments.ReservationsAdminFragment;

public class Admin extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FloatingActionButton addProductBttn;
    private FirebaseUser user = mAuth.getCurrentUser();

    private BottomNavigationView mainAdminBottomNav;

    private HomeAdminFragment homeAdminFragment;
    private ReservationsAdminFragment reservationsAdminFragment;
    private HistoryAdminFragment historyAdminFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Admin"); // sets title for toolbar
        mainAdminBottomNav = findViewById(R.id.mainAdminBottomNav);
        homeAdminFragment = new HomeAdminFragment();
        reservationsAdminFragment = new ReservationsAdminFragment();
        historyAdminFragment = new HistoryAdminFragment();
        replaceFragment(homeAdminFragment);
        mainAdminBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.bottom_nav_home:
                        replaceFragment(homeAdminFragment);
                        menuItem.setChecked(true);
                        return true;
                    case R.id.bottom_nav_reservations:
                        replaceFragment(reservationsAdminFragment);
                        menuItem.setChecked(true);
                        return true;
                    case R.id.bottom_nav_history:
                        replaceFragment(historyAdminFragment);
                        menuItem.setChecked(true);
                        return true;
                }
                return false;
            }
        });
        addProductBttn = findViewById(R.id.addProductBttn);
        addProductBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newProductIntent = new Intent(getApplicationContext(), NewProductActivity.class);
                startActivity(newProductIntent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_admin, menu); // inflates menu from XML to objects
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) { // switch case for detecting which menu item is clicked
            case R.id.ADaction_logout_bttn:  // triggers when logout button is clicked
                logOut();  // logs out user
                return true;
            case R.id.ADaction_resetPassword_buttn:
                resetpassword();
                return true;
            case R.id.ADaction_ChangeEmail_bttn:
                Intent changeEmail = new Intent(getBaseContext(), ResetEmail.class);
                startActivity(changeEmail);
                return true;
            case R.id.Edit_product:
                Intent intent= new Intent(getBaseContext(), ChooseProduct.class);
                startActivity(intent);
                return true;
            case R.id.users_role:
                Intent changeUserRole = new Intent(getBaseContext(),chooseUser.class);
                startActivity(changeUserRole);
                return true;
            case R.id.send_notification:
                Intent sendNotification = new Intent(getApplicationContext(), com.bramgussekloo.projectb.Activities.sendNotification.class);
                startActivity(sendNotification);
                return true;
            case R.id.send_return_notification:
                Intent sendReturnNotification = new Intent(getApplicationContext(), returnNotification.class);
                startActivity(sendReturnNotification);
                return true;
            case R.id.ADaction_statistics:
                Intent statistics = new Intent(getApplicationContext(), com.bramgussekloo.projectb.Activities.statistics.class);
                startActivity(statistics);
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
                    Toast.makeText(Admin.this, "Mail was sent succesful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Admin.this, "Mail not sent succesful", Toast.LENGTH_SHORT).show();
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
        fragmentTransaction.replace(R.id.main_admin_container, fragment);
        fragmentTransaction.commit();
    }
}
