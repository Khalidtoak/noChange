package com.group1.swepproject.user.nochange.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.group1.swepproject.user.nochange.ModelClass.personalWallet;
import com.group1.swepproject.user.nochange.R;

import java.io.File;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonalDebt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalDebt extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PersonalDebt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalDebt.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalDebt newInstance(String param1, String param2) {
        PersonalDebt fragment = new PersonalDebt();
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
        // Inflate the layout for this fragment
        //We do the same thing we did in the personal credits class except that we use To collect as the statement
        //since is just for the debts
        View rootNotStem = inflater.inflate(R.layout.fragment_personal_debt, container, false);
        listView = rootNotStem.findViewById(R.id.list_For_debt);
        getWallet();
        getActivity().overridePendingTransition(0, 0);

        (rootNotStem.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPersonalstuffActivity.class));
            }
        });

        return rootNotStem;
    }
    public void getWallet(){

        File[] listOf = getContext().getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();
        int debtCounter = 0;
        int credCounter = 0;


        for (File i : listOf) {
            try {
                personalWallet object = (personalWallet) (new ObjectInputStream(getContext().openFileInput(i.getName()))).readObject();
                if (object.getReturnStatement().equals("To pay ")) {
                    debtCounter++;
                    allTheBloodyTransactionsGuy.add(object.getReturnStatement().concat("#").concat(object.getAmount()).concat(object.getPreposition()).concat(object.getNameOfWallet()));
                } else if (object.getReturnStatement().equals("To collect ")) {
                    credCounter++;
                }
            } catch (Exception e) {
                //nothing
            }
        }



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    (new AlertDialog.Builder(getContext())).setMessage("Delete?")
                            .setCancelable(false)
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

    public void deleteWallet(String displayMessage) {
        String name = "";
        String amount = "";
        boolean isDebt = false;
        int startFromHereNextTime;

        char[] brakata = displayMessage.toCharArray();

        if (brakata[3] == 'c') {
            isDebt = false;
        } else if (brakata[3] == 'p') {
            isDebt = true;
        }


        ArrayList<String> amountArray = new ArrayList<>();
        ArrayList<String> nameArray = new ArrayList<>();

        for (int i = 8; i < brakata.length; i++) {
            amountArray.add(Character.toString(brakata[i]));
            if (brakata[i + 1] == ' ') {
                startFromHereNextTime = i + 5;
                for (int j = startFromHereNextTime; j < brakata.length; j++) {
                    nameArray.add(Character.toString(brakata[j]));
                }
                break;
            }
        }
        for (String a : amountArray) {
            amount += (a);
        }
        for (String a : nameArray) {
            name += (a);
        }


        File[] listOf = getContext().getFilesDir().listFiles();

        for (File i : listOf) {
            try {
                personalWallet theObjectWeWant = (personalWallet)
                        (new ObjectInputStream(getContext().openFileInput(i.getName()))).readObject();

                if (theObjectWeWant.getNameOfWallet().equals(name)) {
                    if (theObjectWeWant.getAmount().equals(amount)) {
                        if (theObjectWeWant.getIsDebt() == isDebt) {
                            i.delete();
                            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception e) {
                //nothing
            }
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getWallet();
    }
}
