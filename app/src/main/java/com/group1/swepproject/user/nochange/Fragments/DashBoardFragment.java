package com.group1.swepproject.user.nochange.Fragments;

import android.app.IntentService;
import android.app.SearchManager;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group1.swepproject.user.nochange.Adapters.Adapters;
import com.group1.swepproject.user.nochange.AddChangeOrDebt;
import com.group1.swepproject.user.nochange.DashBoard;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.Utils.ImageDbUtils;
import com.group1.swepproject.user.nochange.R;

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
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    Adapters adapters;
    SearchView searchView;
    SQLiteDatabase sqLiteDatabase;
    CreditorsAndDebtorsDataBase creditorsAndDebtorsDataBase;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        creditorsAndDebtorsDataBase = new CreditorsAndDebtorsDataBase(getContext());
        searchView = rootV.findViewById(R.id.sv);
        sqLiteDatabase = creditorsAndDebtorsDataBase.getWritableDatabase();
        adapters = new Adapters(getAllSaved(), getContext());
        recyclerView.setAdapter(adapters);
        floatingActionButton = rootV.findViewById(R.id.fab_for_recyclcer_view_dash);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddChangeOrDebt.class);
                startActivity(intent);
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
                final long id1 = (long)viewHolder.itemView.getTag();

                //delete the News with id that was swiped off
                new AlertDialog.Builder(getContext())
                        .setMessage("Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //delete the Customer with id that was swiped off
                                removeCustomer(id1);
                                Snackbar.make(getView(), "deleted!!", Snackbar.LENGTH_LONG).show();
                                //now swap the cursor for proper arrangement
                                adapters.swapCursor(getAllSaved());
                                Log.d(TAG, "onSwiped: did something happen here??");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapters.swapCursor(getAllSaved());
                            }
                        })
                        .show();


            }
        }).attachToRecyclerView(recyclerView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = searchView.getQuery().toString();
                adapters = new Adapters( creditorsAndDebtorsDataBase.retrieve(query), getContext());
                recyclerView.setAdapter(adapters);
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
    //get all saved queries the whole database since here we are not classifying the data according debtors or creditors
    private Cursor getAllSaved(){
        return sqLiteDatabase.query(CreditorsAndDebtorsDataBase.TABLE_NAME,
                null, null, null, null, null,
                CreditorsAndDebtorsDataBase._ID);
    }
    private  boolean removeCustomer(long id)
    {
        return sqLiteDatabase.delete(CreditorsAndDebtorsDataBase.TABLE_NAME,
                CreditorsAndDebtorsDataBase._ID + "=" + id, null ) > 0;
    }


}
