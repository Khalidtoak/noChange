package com.group1.swepproject.user.nochange.Fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.group1.swepproject.user.nochange.AddPersonalstuffActivity;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.Utils.BroadcastRecieverForNotification;
import com.group1.swepproject.user.nochange.ModelClass.personalWallet;
import com.group1.swepproject.user.nochange.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalDashBoard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalDashBoard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PersonalDashBoard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalDashBoard.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalDashBoard newInstance(String param1, String param2) {
        PersonalDashBoard fragment = new PersonalDashBoard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //in this fragment we do the same thing we did in the personal credits except that we

        //are adding all data here either debts or credits
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_personal_dash_board, container, false);
        listView = rootView.findViewById(R.id.lizzy);
        //the following lines of code just helps us to remind the user of his debts and credits
        getActivity().overridePendingTransition(0, 0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(getActivity(), BroadcastRecieverForNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 14);
        alarmStartTime.set(Calendar.MINUTE, 46);
        alarmStartTime.set(Calendar.SECOND, 0);
        if(now.after(alarmStartTime)){
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        (rootView.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPersonalstuffActivity.class));
            }
        });


        return rootView;
    }
    public void getWallet(){
        File[] listOf = getContext().getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();
        int debtCounter = 0;
        int credCounter = 0;

        for(File i: listOf){
            try{
                personalWallet walletObject = (personalWallet) (new ObjectInputStream(getContext().openFileInput(i.getName()))).readObject();
                allTheBloodyTransactionsGuy.add(walletObject.getReturnStatement().concat("#").concat(walletObject.getAmount()).
                        concat(walletObject.getPreposition()).concat(walletObject.getNameOfWallet()));
            }
            catch(Exception e){
                //Nothing
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext()).setMessage("Delete?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] tester = {};
                                String[] ohho = allTheBloodyTransactionsGuy.toArray(tester);
                                deleteWallet(ohho[position]);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
    public void deleteWallet(String displayMessage){
        String name = "";
        String amount = "";
        boolean isDebt = false;
        int startFromHereNextTime;

        char[] brakata = displayMessage.toCharArray();

        if(brakata[3] == 'c'){
            isDebt = false;
        }
        else if(brakata[3] == 'p'){
            isDebt = true;
        }


        if(isDebt){
            ArrayList<String> amountArray = new ArrayList<>();
            ArrayList<String> nameArray = new ArrayList<>();

            for(int i = 8; i < brakata.length; i++){
                amountArray.add(Character.toString(brakata[i]));
                if(brakata[i + 1] == ' '){
                    startFromHereNextTime = i + 5;
                    for(int j = startFromHereNextTime; j < brakata.length; j++){
                        nameArray.add(Character.toString(brakata[j]));
                    }
                    break;
                }
            }
            for(String a: amountArray){
                amount += (a);
            }
            for(String a: nameArray){
                name += (a);
            }

        }

        else{
            ArrayList<String> amountArray = new ArrayList<>();
            ArrayList<String> nameArray = new ArrayList<>();
            for(int i = 12; i < brakata.length; i++){
                amountArray.add(Character.toString(brakata[i]));
                if(brakata[i + 1] == ' '){
                    startFromHereNextTime = i + 7;
                    for(int j = startFromHereNextTime; j < brakata.length; j++){
                        nameArray.add(Character.toString(brakata[j]));
                    }
                    break;
                }
            }
            for(String a: amountArray){
                amount += (a);
            }
            for(String a: nameArray){
                name += (a);
            }
        }

        File[] listOf = getContext().getFilesDir().listFiles();

        for(File i: listOf) {
            try {
                FileInputStream fis = getContext().openFileInput(i.getName());
                ObjectInputStream ois = new ObjectInputStream(fis);
                personalWallet theObjectWeWant = (personalWallet) ois.readObject();

                if(theObjectWeWant.getNameOfWallet().equals(name)){
                    if(theObjectWeWant.getAmount().equals(amount)){
                        if(theObjectWeWant.getIsDebt() == isDebt){
                            i.delete();
                            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), AddPersonalstuffActivity.class));
                        }
                    }
                }
            }
            catch (Exception e){
            }
        }

    }

    public int giveCred(Context context){
        int credCounter = 0;

        File[] listOf = context.getApplicationContext().getFilesDir().listFiles();
        for(File i: listOf){
            try{
                personalWallet walletObject = (personalWallet) (new ObjectInputStream(context.openFileInput(i.getName()))).readObject();

                if(walletObject.getReturnStatement().equals("To collect ")){
                    credCounter++;
                }

            }
            catch(Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return credCounter;
    }


    public int giveDebt(Context context){
        int debtCounter = 0;
        File[] listOf = context.getApplicationContext().getFilesDir().listFiles();

        for(File i: listOf){
            try{
                personalWallet walletObject = (personalWallet) (new ObjectInputStream(context.openFileInput(i.getName()))).readObject();

                if(walletObject.getReturnStatement().equals("To pay ")){
                    debtCounter++;
                }
            }
            catch(Exception e){
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return debtCounter;
    }

    @Override
    public void onResume() {
        super.onResume();
        getWallet();
    }
}
