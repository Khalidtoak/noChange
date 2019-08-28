package com.group1.swepproject.user.nochange.Adapters;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 8/27/2019.
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot> {
    private static final String TAG = "Debts and Change";
    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e!=null){
            Log.e(TAG, "Error loading random memes" + e);
        }
        else {
            if (queryDocumentSnapshots != null) {
                for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                    // Snapshot of the changed document
                    DocumentSnapshot snapshot = change.getDocument();
                    switch (change.getType()) {
                        case ADDED:
                            //  handle document added
                            onDocumentAdded(change);
                            break;
                        case MODIFIED:
                            //  handle document modified
                            onDocumentModified(change);
                            break;
                        case REMOVED:
                            //  handle document removed
                            onDocumentRemoved(change);
                            break;
                    }
                }
                onDataChanged();
            }
        }
    }
    private Query mQuery;
    private ListenerRegistration mRegistration;
    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();
    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    public void startListening() {
        // (developer): Implement
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }
        mSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery(Query query) {
        // Stop listening
        stopListening();
        // Clear existinkodig data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    protected List<DocumentSnapshot> getSnapshots() {
        return mSnapshots;
    }

    protected void onError(FirebaseFirestoreException e) {};

    protected void onDataChanged() {}
    private void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }
}
