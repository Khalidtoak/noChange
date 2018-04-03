package com.group1.swepproject.user.nochange;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.group1.swepproject.user.nochange.ModelClass.personalWallet;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class AddPersonalstuffActivity extends AppCompatActivity {
    //similar to the AddChange or debt activity just that there id no image here

    RadioButton isDebt, isCredit;
    EditText name, amount;
    FloatingActionButton save;
    ImageButton cancel;
    boolean areWeOwing;
    LinearLayout newn;


    @Override
    public void onBackPressed() {
        AddPersonalstuffActivity.super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_personalstuff);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        isDebt =  findViewById(R.id.isDebt);
        isCredit =  findViewById(R.id.isCredit);
        name =  findViewById(R.id.name);
        amount =  findViewById(R.id.amount);
        save = findViewById(R.id.save);
        cancel =  findViewById(R.id.cancel);
        newn = findViewById(R.id.dreamer);


        isDebt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isDebt.isChecked()){
                    if(isCredit.isChecked()){
                        isCredit.setChecked(false);
                    }
                }
            }
        });

        isCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isCredit.isChecked()){
                    if(isDebt.isChecked()){
                        isDebt.setChecked(false);
                    }
                }
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDebt.isChecked() || isCredit.isChecked()){
                    if(!name.getText().toString().trim().equals("")){
                        if(!amount.getText().toString().trim().equals("")){
                            if(isDebt.isChecked()){
                                areWeOwing = true;
                            }
                            else if(isCredit.isChecked()){
                                areWeOwing = false;
                            }
                            try{
                                FileOutputStream fos = openFileOutput(Long.toString(System.currentTimeMillis()), Context.MODE_APPEND);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(new personalWallet(name.getText().toString().trim(),
                                        amount.getText().toString().trim(),
                                        areWeOwing));
                                oos.close();
                                fos.close();
                                Toast.makeText(getApplicationContext(), "Successful!!!", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            catch (Exception e){
                                Toast.makeText(getApplicationContext(), "Failed, try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Amount field empty!!!", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Name field empty!!!", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Choose between credit and debit!!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
