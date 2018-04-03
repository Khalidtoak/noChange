package com.group1.swepproject.user.nochange.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
 * Activities that contain this fragment must implement the
 * {@link PersonalCredits.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalCredits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalCredits extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PersonalCredits() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalCredits.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalCredits newInstance(String param1, String param2) {
        PersonalCredits fragment = new PersonalCredits();
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
        View root =  inflater.inflate(R.layout.fragment_personal_credits, container, false);
        //find the view by id for the list view
       listView = root.findViewById(R.id.lizzy);
        getActivity().overridePendingTransition(0,0);
        //find the floating action buuton
        (root.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when floating action button is clicked, we want to start the acivity where we add our personal stuff with an intent
                startActivity(new Intent(getContext(), AddPersonalstuffActivity.class));
            }
        });
        //get wallet ... method description below
        getWallet();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getWallet();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void getWallet(){
        File[] listOf = getContext().getFilesDir().listFiles();

        final ArrayList<String> allTheBloodyTransactionsGuy = new ArrayList<>();
        int debtCounter = 0;
        int credCounter = 0;
       // . Note that to save the personal wallets we
        //actually saved them in a file instead of using sqlite database...
        //we could have used sqlite database for this as well but e no mean lol :)
        //loop through the file...
        for(File i: listOf) {
            try{
                //create an instance of the personal wallet class which contains all  variables in our wallet
                //then we open the file and read it
                personalWallet object = (personalWallet) (new ObjectInputStream(getContext().openFileInput(i.getName()))).readObject();
                //if return statement is toPay... we want to increament the debtCounter
                if(object.getReturnStatement().equals("To pay ")){
                    debtCounter++;
                }
                //other if return statement is to collect
                //increament the no of credits and add the data since this is the fragment for credits
                else if(object.getReturnStatement().equals("To collect ")){
                    credCounter++;
                    //alltheblodytransactionsguy is an array list of strings.. as declared above
                    //so we add the credits and concatenate our string appropriately to display
                    allTheBloodyTransactionsGuy.add(object.getReturnStatement()
                            .concat("#").concat(object.getAmount()).concat(object.getPreposition())
                            .concat(object.getNameOfWallet()));
                }
            }
            catch(Exception e){
                //nothing
            }
        }
        //create an array adapter of strings and pass in the activity as well as a simple list item layout since
        // we dont have a customized layout for our list view and the array list
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, allTheBloodyTransactionsGuy);
        //set Adapter
        listView.setAdapter(arrayAdapter);
        ///when list view is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //create a confirm dialog whether user wants to delete the row  or nah
                (new AlertDialog.Builder(getContext())).setMessage("Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String[] tester = {};
                                //delete the wallet
                                String[] ohho = allTheBloodyTransactionsGuy.toArray(tester);
                                deleteWallet(ohho[position]);

                            }
                        })
                        .setNegativeButton("No", null)
                        //if no is selected do nothing
                        .show();
            }
        });
    }
    //method for deleting a particular wallet
    public void deleteWallet(String displayMessage) {
        String name = "";
        String amount = "";
        boolean isDebt = false;
        int startFromHereNextTime;
        //we use a char array to specify the string
        //turn the  string to a char array for example the word collect is an array of
        //all the characters c, o, l, l, e, c, t
        //now we know the display message starts with the phrase To collect
        //c is the 3rd character in the array
        char[] brakata = displayMessage.toCharArray();
        // if the arrays  3rd element is c, we know that it is to collect
        //means it cant be a debt so isDebt is a false
        if (brakata[3] == 'c') {
            isDebt = false;
            //however if the array's 3rd element is p, means  we are paying someone
            ///ie To p
        } else if (brakata[3] == 'p') {
            isDebt = true;
        }
        //now for the ArrayList of amounts
        ArrayList<String> amountArray = new ArrayList<>();
        //and for the name.. we make them an arrayList of strings
        ArrayList<String> nameArray = new ArrayList<>();
        //we loop through the brakata array list from bottom to top
        //here it is 12 cos we have a static statement of To collect from
        //which contains 12 characters...Note that we are adding the spaces
        for (int i = 12; i < brakata.length; i++) {
            //add the amt but we dont want to add it unless its on the 7th character.. ie to collect #50 from..
            amountArray.add(Character.toString(brakata[i]));
            if (brakata[i + 1] == ' ') {
                startFromHereNextTime = i + 7;
                ///now loop through the array of strings for the statement now  and add the name at the end
                //ie to collect 50 naira fromkhalid
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

        //Now we get  files directory and read the file went to delete by getting the name and amount
        //then delete this file
        File[] listOf = getContext().getFilesDir().listFiles();

        for (File i : listOf) {
            try {
                personalWallet theObjectWeWant = (personalWallet) (new ObjectInputStream(getContext()
                        .openFileInput(i.getName()))).readObject();

                if (theObjectWeWant.getNameOfWallet().equals(name)) {
                    if (theObjectWeWant.getAmount().equals(amount)) {
                        if (theObjectWeWant.getIsDebt() == isDebt) {
                            i.delete();
                            //create a toast message that tells us file has been deleted
                            Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception e) {
                //nothing
            }
        }
    }
}
