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
 * Use the {@link DebtorsRecord#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DebtorsRecord extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    Adapters adapters;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;
    FloatingActionButton floatingActionButton;
    CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DebtorsRecord() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DebtorsRecord.
     */
    // TODO: Rename and change types and number of parameters
    public static DebtorsRecord newInstance(String param1, String param2) {
        DebtorsRecord fragment = new DebtorsRecord();
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
        View rootView = inflater.inflate(R.layout.fragment_debtors_record, container, false);
        recyclerView = rootView.findViewById(R.id.recyclcer_view_debtors);
        searchView = rootView.findViewById(R.id.sv2);
        floatingActionButton = rootView.findViewById(R.id.fab_for_recyclcer_view_dash);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        creditorsAndDebtorsDataBase = new CreditorsAndDebtorsDataBase(getContext());
        Query query = FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        collection(Constant.PAYMENT_COLLECTION).whereEqualTo("userId",
                        FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("type", "Debt");
        displayData(query);
        recyclerView.setAdapter(adapters);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query1 = searchView.getQuery().toString();
                adapters.setQuery(FirebaseFirestore.getInstance().collection(Constant
                        .USER_COLLECTION).document(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION)
                        .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .whereEqualTo("type", "Debt").whereGreaterThanOrEqualTo("customerName", query1));
                recyclerView.setAdapter(adapters);
                return true;
            }
        });
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), AddChangeOrDebt.class)));
        //ItemTouch helper to handle swipe to delete the saved News function
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final long id1 = (long) viewHolder.itemView.getTag();
                //delete the News with id that was swiped off
                new AlertDialog.Builder(getContext())
                        .setMessage("Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            //delete the News with id that was swiped off

                            removeCustomer(id1);
                            Snackbar.make(getView(), "deleted!!", Snackbar.LENGTH_LONG).show();
                            //now swap the cursor for proper arrangement
                            // adapter.swapCursor(creditorsAndDebtorsDataBase.retrieveByViewpager("Debtors"));
                        })
                        .setNegativeButton("No", (dialogInterface, i) -> {
                            // adapter.swapCursor(creditorsAndDebtorsDataBase.retrieveByViewpager("Debtors"));
                        })
                        .show();

                Log.d(TAG, "onSwiped: did something happen here??");
                Log.d(TAG, "onSwiped: did something happen here??");

            }
        }).attachToRecyclerView(recyclerView);
        return rootView;
    }

    private boolean removeCustomer(long id) {
        sqLiteDatabase = creditorsAndDebtorsDataBase.getReadableDatabase();
        return sqLiteDatabase.delete(CreditorsAndDebtorsDataBase.TABLE_NAME,
                CreditorsAndDebtorsDataBase._ID + "=" + id, null) > 0;
    }

    private void displayData(Query query) {
        adapters = new Adapters(query, requireActivity()) {
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                if (getItemCount() == 0) {
                    Toast.makeText(requireContext(), "You have no debtor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Toast.makeText(requireContext(), "Couldn't load data", Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.setAdapter(adapters);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapters.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapters.stopListening();
    }
}
