package com.group1.swepproject.user.nochange;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 3023;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            setupButtons();
        }
        else {
            nextPage();
        }
    }

    private void nextPage(){
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        finish();
    }
    private void setupButtons(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setLogo(R.mipmap.ic_launcher)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK){
            nextPage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
