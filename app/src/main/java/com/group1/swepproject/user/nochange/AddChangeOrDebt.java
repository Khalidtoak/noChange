package com.group1.swepproject.user.nochange;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.data.Constant;
import com.group1.swepproject.user.nochange.models.Payment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddChangeOrDebt extends AppCompatActivity {
    //thiis is the class where we input our values
    //declare variables
    EditText customerName, ItemBought, HowMuch, AddPhoneNumber;
    CircleImageView addImage;
    FloatingActionButton save;
    private String ChangeOrDebit;
    ProgressBar progressBar;
    //setter and getter for image path which be stored in the data base when we take picture of customer

    public String getPathForImage() {
        return pathForImage;
    }

    public void setPathForImage(String pathForImage) {
        this.pathForImage = pathForImage;
    }

    String pathForImage;
    CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;
    //Rad buttons for selecting whether debtor or creditor
    RadioButton ChangeradButton, DebtRadButton;
    private int REQUEST_CAPTURE_IMAGE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_change_or_debt);
        //find all the views
        customerName = findViewById(R.id.edit_text_name);
        ItemBought = findViewById(R.id.itemBought);
        HowMuch = findViewById(R.id.edit_text_Amount);
        save = findViewById(R.id.fab_for_save);
        addImage = findViewById(R.id.add_profileImage);
        AddPhoneNumber = findViewById(R.id.phone_no);
        progressBar = findViewById(R.id.progress_circular);

        ChangeradButton = findViewById(R.id.radButton1);
        creditorsAndDebtorsDataBase = new CreditorsAndDebtorsDataBase(this);
        DebtRadButton = findViewById(R.id.radButton2);
        //set default checked rad button to the one for change
        ChangeradButton.setChecked(true);
        ChangeOrDebit = "Change";
        //when you click on the floating action button for taking a picture
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //use an implicit intent to launch camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivity
                startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
            }
        });
        //when we click save
        save.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            save.setEnabled(false);
            //we get the text from each of the edit Text
            String customersname = customerName.getText().toString();
            String boughtStuff = ItemBought.getText().toString();
            String howMuch = HowMuch.getText().toString();
            // get the change or debt rad button
            String debtOrChange = ChangeOrDebit;
            //get the text of the phone number editText
            String phoneNumber = AddPhoneNumber.getText().toString();
            //Now we want a valid phone number so we have to do some checking here,
            //if the phone number's first 3 character dont start with 090, 080, or 070,or 081 or the pone number
            //editText length is not 11
            if (phoneNumber.length() != 0) {
                if (!(phoneNumber.substring(0, 2).equals("080")
                        || phoneNumber.substring(0, 2).equals("090") || phoneNumber.substring(0, 2).equals("070")
                        || phoneNumber.length() == 11 || phoneNumber.substring(0, 2).equals("081"))) {
                    //you tell the user his phone number is invalid and don't go futher by calling return
                    progressBar.setVisibility(View.INVISIBLE);
                    save.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Invalid phone Number", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            //if any of the editTexts are empty... we also tell the user and do not go further
            if (customersname.length() == 0 || boughtStuff.length() == 0 || howMuch.length() == 0 || debtOrChange.length() == 0 ||
                    phoneNumber.length() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
                save.setEnabled(true);
                Toast.makeText(getApplicationContext(), "please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }
            //now we save the customer's details
//                    public Payment(String customerName, String item, String amount, String type, String imageUrl, String ohoneNumber) {
            Payment payment = new Payment(customersname, boughtStuff, howMuch, debtOrChange, getPathForImage(), phoneNumber, FirebaseAuth.getInstance().getCurrentUser().getUid());
            saveNewRecordOnline(payment);

//                creditorsAndDebtorsDataBase.SaveLecturesAdded(customersname, boughtStuff, howMuch, debtOrChange,
//                        getPathForImage(), phoneNumber);
//                Log.d("ContentVals: ", "onClick: " + customersname + boughtStuff + howMuch + debtOrChange);
//                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();
//                //start the DashBoard Activity
//                Intent intent = new Intent(AddChangeOrDebt.this, DashBoard.class);
//                startActivity(intent);


        });

    }

    //create an img file with this method
    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //the file name
        String fileName = "IMG_" + timeStamp + "_.jpg";
        //the file directory
        File storageDir = new File(this.getFilesDir(), fileName);
        return storageDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //when picture is taken and the result is okay
        if (requestCode == REQUEST_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //save the image in internal storage
            String imagePath = saveToInternalSorage(thumbnail, createImageFile());
            //set the image path to the saved image
            setPathForImage(imagePath);
            Glide.with(AddChangeOrDebt.this).load(imagePath).asBitmap().centerCrop().into(addImage);
            Log.d("Thumbnail", "onActivityResult: " + imagePath);
        }
    }

    //when radButton is selected
    public void onPrioritySelected(View view) {
        if (ChangeradButton.isChecked()) {
            ChangeOrDebit = "Change";
        } else if (DebtRadButton.isChecked()) {
            ChangeOrDebit = "Debtors";
        }

    }

    public String saveToInternalSorage(Bitmap bitmapImage, File file) {

        try {
            //create a new file
            FileOutputStream fos = new FileOutputStream(file);
            ///compress the image
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            //close file
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showError(Exception e) {
        showToast("Could not save, try again with internet." + e.getMessage());
    }

    private void saveNewRecordOnline(Payment payment) {
        if (pathForImage != null) {
            Uri imageUri = Uri.fromFile(new File(pathForImage));
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child(Constant.IMAGE_COLLECTION).child(
                    String.format(Locale.getDefault(), "payment_%s.jpg", String.valueOf(System.currentTimeMillis())));
            imageRef.putFile(imageUri)
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            showError(Objects.requireNonNull(task.getException()));
                        }

                        // Continue with the task to get the download URL
                        return imageRef.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    assert downloadUri != null;
                    setPathForImage(downloadUri.toString());

                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                    firestore.collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION).add(payment)
                            .addOnSuccessListener(aVoid -> {
                                progressBar.setVisibility(View.INVISIBLE);
                                save.setEnabled(true);
                                showToast("Successfully saved!");
                                //start the DashBoard Activity
                                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(error ->
                            {
                                showError(error);
                                progressBar.setVisibility(View.INVISIBLE);
                                save.setEnabled(true);
                            });

                } else {
                    return;
                }

            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            save.setEnabled(true);
            (Toast.makeText(this, "Please upload an image to recognize user before continuing", Toast.LENGTH_LONG)).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Normally, calling setDisplayHomeAsUpEnabled(true) (we do so in onCreate here) as well as
         * declaring the parent activity in the AndroidManifest is all that is required to get the
         * up button working properly. However, in this case, we want to navigate to the previous
         * screen the user came from when the up button was clicked, rather than a single
         * designated Activity in the Manifest.
         *
         * We use the up button's ID (android.R.id.home) to listen for when the up button is
         * clicked and then call onBackPressed to navigate to the previous Activity when this
         * happens.
         */
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
