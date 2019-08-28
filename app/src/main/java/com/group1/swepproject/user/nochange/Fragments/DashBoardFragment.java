package com.group1.swepproject.user.nochange.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.group1.swepproject.user.nochange.Adapters.Adapters;
import com.group1.swepproject.user.nochange.AddChangeOrDebt;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.R;
import com.group1.swepproject.user.nochange.data.Constant;
import com.group1.swepproject.user.nochange.models.AppViewModel;
import com.group1.swepproject.user.nochange.models.Payment;

import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashBoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 * We did the same thing in all the fragments the omly thing that changed is the cursor queries that we
 * passed into the adapter
 */
public class DashBoardFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private Adapters adapter;
    private SearchView searchView;
    private SQLiteDatabase sqLiteDatabase;
    private CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;
    ProgressBar progressBar;
    ImageButton imageButton;
    AppViewModel appViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View rootV= inflater.inflate(R.layout.fragment_dash_board, container, false);
        recyclerView = rootV.findViewById(R.id.recyclcer_view_dash);
        appViewModel = new AppViewModel(Objects.requireNonNull(getActivity()).getApplication());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        searchView = rootV.findViewById(R.id.sv);
        progressBar = rootV.findViewById(R.id.progressBar);
        imageButton = rootV.findViewById(R.id.redo_button);
        floatingActionButton = rootV.findViewById(R.id.fab_for_recyclcer_view_dash);
        final Query query =  FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION).whereEqualTo("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
        displayData(query);
        imageButton.setOnClickListener(view->{
            imageButton.setVisibility(View.INVISIBLE);
            displayData(query);
        });
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddChangeOrDebt.class);
            startActivity(intent);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query1 = searchView.getQuery().toString();
                final Query query =  FirebaseFirestore.getInstance().collection(Constant.USER_COLLECTION).document(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid()).collection(Constant.PAYMENT_COLLECTION).whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .whereGreaterThanOrEqualTo("customerName" , query1);
               adapter.setQuery(query);
               //displayData(query);
                return true;
            }
        });
        return  rootV;

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
    private  boolean removeCustomer(long id)
    {
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
