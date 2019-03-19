package com.bramgussekloo.projectb;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.helpers.inputValidation;
import com.bramgussekloo.projectb.sql.DatabaseHelper;
import com.bramgussekloo.projectb.models.DatabaseModel;


public class Register extends AppCompatActivity {
    private Button registerButton;
    private TextInputEditText password;
    private TextInputEditText passwordConfirmation;
    private TextInputEditText email;
    private TextInputEditText name;
    private DatabaseHelper databaseHelper;
    private inputValidation inputValidation;

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputLayout nameLayout;
    private DatabaseModel user;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register_Button();
    }
    private void initObjects() {
        databaseHelper = new DatabaseHelper(getBaseContext());
        inputValidation = new inputValidation(getBaseContext());
        user = new DatabaseModel();
    }

    public void Register_Button(){
        registerButton = findViewById(R.id.RegisterRegisterButton);
        password = findViewById(R.id.Password);
        passwordConfirmation = findViewById(R.id.registerConfirmPassword);
        email = findViewById(R.id.registerEmail);
        name = findViewById(R.id.registerName);

        nameLayout = findViewById(R.id.registerNameLayout);
        emailLayout = findViewById(R.id.registerEmailLayout);
        passwordLayout = findViewById(R.id.registerPasswordLayout);
        confirmPasswordLayout = findViewById(R.id.registerConfirmPasswordLayout);

        registerButton.setOnClickListener( // the back-end of the register button
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!inputValidation.isInputEditTextFilled(name, nameLayout, getString(R.string.error_name))){
                            return;
                        }
                        if (!inputValidation.isInputEditTextFilled(email, emailLayout, getString(R.string.error_email))){
                            return;
                        }
                        if (!inputValidation.isInputEditTextEmail(email, emailLayout, getString(R.string.error_email))){
                            return;
                        }
                        if (!inputValidation.isInputEditTextMatches(password, passwordConfirmation, confirmPasswordLayout, getString(R.string.error_password))){
                            return;
                        }
                        if (!inputValidation.isInputEditTextFilled(password, passwordLayout, getString(R.string.error_password))){
                            return;
                        }
                        if (!databaseHelper.checkUser(email.getText().toString().trim())){
                            user.setName(name.getText().toString().trim());
                            user.setPassword(password.getText().toString().trim());
                            user.setEmail(email.getText().toString().trim());

                            databaseHelper.AddUser(user);

                            Toast.makeText(Register.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                            emptyInputEditText();

                        }
                        else {
                            Toast.makeText(Register.this, getString(R.string.email_exist), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

    }
    private void emptyInputEditText() {
        name.setText(null);
        email.setText(null);
        password.setText(null);
        passwordConfirmation.setText(null);
    }

}
