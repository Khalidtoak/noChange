package com.group1.swepproject.user.nochange;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class profileActivity extends AppCompatActivity {
    //declare views
    ImageView imageView;
    TextView textView, textView1, textView2, textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //find those views in the xml
        imageView = findViewById(R.id.imgProfile);
        textView = findViewById(R.id.name_of_customer);
        textView1 = findViewById(R.id.itemBought_);
        textView2 = findViewById(R.id.how_much_);
        textView3 = findViewById(R.id.date_debt_or_credit);

        //load the image of customer with the glide image loader library
        //here we are getting the values of the Strings we put in the class it started from
        Glide.with(this).load(getIntent().getStringExtra("imgUri")).crossFade().into(imageView);
        textView.setText(getIntent().getStringExtra("customer_name"));
        textView1.setText("Item purchased: " + getIntent().getStringExtra("bought_stuff"));
        textView2.setText("How much: " + getIntent().getStringExtra("how_much") + "naira");
        textView3.setText("Debt or change??: " + getIntent().getStringExtra("debt_or_change" ) + "" +
                "\n" + "\n" + getIntent().getStringExtra("time"));
    }
}
