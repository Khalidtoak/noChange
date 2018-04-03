package com.group1.swepproject.user.nochange.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group1.swepproject.user.nochange.Adapters.Adapters;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.Utils.ImageDbUtils;
import com.group1.swepproject.user.nochange.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeRecord#newInstance} factory method to
 * create an instance of this fragment.
 * A fragment is usually used as part of an activity's
 * user interface and contributes its own layout to the activity.
 * To provide a layout for a fragment, you must implement the onCreateView()
 * callback method, which the Android system calls when it's time for the fragment to draw its layout.
 * You can think of fragments as mini Activities
 */
public class ChangeRecord extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //declare all variables needed
    RecyclerView recyclerView;
    Adapters adapters;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton floatingActionButton;
    CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChangeRecord() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeRecord newInstance(String param1, String param2) {
        ChangeRecord fragment = new ChangeRecord();
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
        View rootView =  inflater.inflate(R.layout.fragment_change_record, container, false);
        //find the recycler view in its xml by its id
        recyclerView = rootView.findViewById(R.id.recyclcer_view_change);
        //find the searView ById
        searchView = rootView.findViewById(R.id.sv1);
        //setLayout manager to a vertical linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //setFixed size for recycler view
        recyclerView.setHasFixedSize(true);
        //initialize database
        creditorsAndDebtorsDataBase = new CreditorsAndDebtorsDataBase(getContext());
        //initialize  the adapter and pass in the context of the activity and retrieve by calling the method we
        //created in the database and passing it in as the cursor
        adapters = new Adapters(creditorsAndDebtorsDataBase.retrieveByViewpager("Change"),
                getContext());
        ///set the adapter of the recycler view
        recyclerView.setAdapter(adapters);
        //When the text in the search view changes .. we listen to it and re-do our  querying
        //by initializing the adapter and passing in the new cursor
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //get the query and convert it to a string
                String query = searchView.getQuery().toString();
                adapters = new Adapters(
                        creditorsAndDebtorsDataBase.retrieveByViewPagerAndSearchedText(query, "Change"),
                        getContext());
                //re set the adapter
                recyclerView.setAdapter(adapters);
                return true;
            }
        });
        //ItemTouch helper to handle swipe to delete the saved News function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                //get the id of the row yo want to delete
                final long id1 = (long)viewHolder.itemView.getTag();
                ///create a dialog message that confirms if you want to delete it or not
                new AlertDialog.Builder(getContext())
                        .setMessage("Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //if yes is selected
                                //delete that customer and pass in the id
                                //delete the News with id that was swiped off

                                removeCustomer(id1);
                                //create a snack bar that tells the user iif it has been deleted or not
                                Snackbar.make(getView(), "deleted!!", Snackbar.LENGTH_LONG).show();
                                //now swap the cursor for proper arrangement
                                adapters.swapCursor(creditorsAndDebtorsDataBase.retrieveByViewpager("Change"));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //if no is clicked... just swap  cursor for proper arrangement
                                adapters.swapCursor(creditorsAndDebtorsDataBase.retrieveByViewpager("Change"));
                            }
                        })
                        //show the dialog
                        .show();
                Log.d(TAG, "onSwiped: did something happen here??");

            }
            //attach itemTouchHelper to recycler view
        }).attachToRecyclerView(recyclerView);
        return rootView;
    }
    private  boolean removeCustomer(long id)
    {
        //this method is for deleting a row in a database by passing the id
        sqLiteDatabase= creditorsAndDebtorsDataBase.getReadableDatabase();
        return sqLiteDatabase.delete(CreditorsAndDebtorsDataBase.TABLE_NAME,
                CreditorsAndDebtorsDataBase._ID + "=" + id, null ) > 0;
    }

}
