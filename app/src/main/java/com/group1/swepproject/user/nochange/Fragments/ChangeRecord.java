package com.group1.swepproject.user.nochange.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.group1.swepproject.user.nochange.Adapters.Adapters;
import com.group1.swepproject.user.nochange.AddChangeOrDebt;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.R;
import com.group1.swepproject.user.nochange.data.Constant;

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
    Adapters adapter;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton floatingActionButton;
    CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;
    ProgressBar progressBar;
    ImageButton imageButton;
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
        progressBar = rootView.findViewById(R.id.progressBar);
        searchView = rootView.findViewById(R.id.sv1);
        floatingActionButton = rootView.findViewById(R.id.fab_for_recyclcer_view_dash);
        imageButton = rootView.findViewById(R.id.redo_button);
        //setLayout manager to a vertical linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //setFixed size for recycler view
        recyclerView.setHasFixedSize(true);
        displayData(FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION).whereEqualTo("userId",
                FirebaseAuth.getInstance().getCurrentUser().getUid()).whereEqualTo("type", "Change"));
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddChangeOrDebt.class)));
        //When the text in the search view changes .. we listen to it and re-do our  querying
        //by initializing the adapter and passing in the new cursor
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = searchView.getQuery().toString();
                adapter.setQuery(FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(Constant.PAYMENT_COLLECTION).whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("type", "Change").whereGreaterThanOrEqualTo("customerName", query));
                recyclerView.setAdapter(adapter);
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
                        .setPositiveButton("Yes", (dialog, id) -> {
                            Snackbar.make(getView(), "deleted!!", Snackbar.LENGTH_LONG).show();
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            //if no is clicked... just swap  cursor for proper arrangement
                           // adapter.swapCursor(creditorsAndDebtorsDataBase.retrieveByViewpager("Change"));
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
    private void displayData(Query query){
        adapter = new Adapters(query, requireActivity()){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                progressBar.setVisibility(View.INVISIBLE);
                if (getItemCount() == 0){
                    Toast.makeText(requireContext(), "You are not owing change", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error", e + e.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity()
                        , "Unable to load data" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

