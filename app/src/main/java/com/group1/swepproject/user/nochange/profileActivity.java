package com.group1.swepproject.user.nochange;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.group1.swepproject.user.nochange.data.Constant;

import de.hdodenhof.circleimageview.CircleImageView;

public class profileActivity extends AppCompatActivity {
    //declare views
    CircleImageView imageView;
    TextView textView, textView1, textView2, textView3, textView4;
    private String phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //find those views in the xml
        imageView = findViewById(R.id.profile_pic);
        textView = findViewById(R.id.nameOfCustomer);
        textView1 = findViewById(R.id.itemBought);
        textView2 = findViewById(R.id.how_much);
        textView3 = findViewById(R.id.debtOrChange);
        textView4 = findViewById(R.id.date);

        //load the image of customer with the glide image loader library
        //here we are getting the values of the Strings we put in the class it started from
        Glide.with(this).load(getIntent().getStringExtra("imgUri")).crossFade().into(imageView);
        textView.setText(getIntent().getStringExtra("customer_name"));
        textView1.setText(getIntent().getStringExtra("bought_stuff"));
        textView2.setText(getIntent().getStringExtra("how_much").concat(" naira"));
        textView3.setText(getIntent().getStringExtra("debt_or_change" ));
        textView4.setText(getIntent().getStringExtra("time"));
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void openTextMessageIntent(View view) {
        //here we will launch the sms app from our app
        phoneNo = getIntent()
                .getStringExtra("phoneNo");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
        startActivity(intent);
    }

    public void callCustomer(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Send phone number to intent as data
        intent.setData(Uri.parse("tel:" + phoneNo));
        // Start the dialer app activity with number
        startActivity(intent);
    }

    public void deleteCustomer(View view) {
        FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
        .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION).document(phoneNo)
                .delete().addOnSuccessListener(aVoid -> {
                    Snackbar.make(view,"Deleted", Snackbar.LENGTH_SHORT ).show();
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(profileActivity.this, "Couldn't delete data", Toast.LENGTH_SHORT).show());
    }
}
