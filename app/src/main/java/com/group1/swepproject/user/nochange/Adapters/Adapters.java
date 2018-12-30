package com.group1.swepproject.user.nochange.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.CreditorsAndDebtorsDataBase;
import com.group1.swepproject.user.nochange.R;
import com.group1.swepproject.user.nochange.profileActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 3/28/2018.
 */
/**This is an adapter class... we use this to plan our recycler view layout
 * a recycler view is a more advanced list view and more flexible listView
 * It creates a fixed number of views and reuses those views instead of creating new ones
 * every time we scroll in this adapter... we pass in the cursor returned from the query method
 * and the context
 * A context in android specifies where you are in your app.. it could be an activity, a fragment etc**/
public class Adapters extends RecyclerView.Adapter<Adapters.AdapterViewHolder>{
    private Cursor mCursor;
    private Context context;
    private static int viewHolderCount;
    //Contructs for the adapters.. Constructors a basically for initialization
    public Adapters(Cursor mCursor, Context context){
        this.mCursor = mCursor;
        this.context = context;
        viewHolderCount  = 0;
    }
//When we extend an adapter... we have to override these 3 methods below
    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //we override the onCreateViewHolder which of Course we use to specify what happens when the recycler views are created
        //great thing is.... the code for this part is usually almost the same
        //we always have to inflate the views from the parent context...we tell it which recycler view we are inflating by
        //passing the layout file from the xml and the 3rd parameter is set to false cos we don't want to attach to root immediately
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent,false);
        AdapterViewHolder viewHolder = new AdapterViewHolder(view);
        // COMPLETED (15) Increment viewHolderCount and log its value
        viewHolderCount++;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterViewHolder holder, int position) {
        //here the views are binded to the view holder
        //we move our cursor to the position on the recycler view adapter
        mCursor.moveToPosition(position);
        //get the values in our table that we have queried
        //by calling get column Index
        //and passing the column name
        final String customerName = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_CUSTOMER_NAME));
        String boughtStuff = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_ITEM_BOUGHT));
        String howMuch = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_HOW_MUCH));
        String Time = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_TIME_STAMP));
        String DebtOrChange = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_DEBT_OR_CHANGE));
        final String phoneNo = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_PHONE_NUMBER));
        //display this data on our recyclerview
            holder.CustomerName.setText(customerName);
            holder.ItemBought.setText("bought Item: " + boughtStuff);
            holder.amount.setText(howMuch + "Naira");
            holder.time.setText(Time);
            holder.itemView.setTag(mCursor.getLong(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase._ID)));
            //for the Image ..we made use of glide Image loading library and using the Circle transform class created
        // in the utils package... we display the circuilar version of the image
            Glide.with(context).load(mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_IMAGE)))
                    .crossFade().into(holder.imageProfile);
            //an image button is a clickable image
            //we create a pop up menu and set a listener for it so that we click it...
        //we will bw able to perform a certain action
            holder.callOrTextessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.callOrTextessage);
                    //we always inflate menus in android or else the menu won't show
                    //A menu resource defines an application menu
                    // (Options Menu, Context Menu, or submenu) that can be inflated with MenuInflater
                    popupMenu.inflate(R.menu.menu);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        popupMenu.setGravity(Gravity.CENTER);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            //get the id of the item that was clicked in the menu
                            switch (menuItem.getItemId()){
                                //if call is clicked
                                case R.id.call:
                                    //open the dialer using an implicit intent
                                    /**
                                     * An intent is an abstract description of an operation to be performed.
                                     * It can be used with startActivity to launch an Activity,
                                     * broadcastIntent to send it to any interested BroadcastReceiver components, and startService(Intent)
                                     * or bindService(Intent, ServiceConnection, int) to communicate with a Background Service.
                                     * Explicit intent launches an intent in your app while implicit intents launches another app
                                     * from your app
                                     * here we are launching the dialer app from our app **/
                                    openDialer(phoneNo);
                                    break;
                                case R.id.send_text_message:
                                    //here we will launch the sms app from our app
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNo));
                                    context.startActivity(intent);
                                    break;
                            }
                            return false;
                        }
                    });
                    //show the popup menu
                    popupMenu.show();

                }
            });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    // note that our viewHolder implements the OnClicklistener interface.
    //Recycler views do not have a default onClickListener like buttons so that is why we have
    //to implement this interface to make each view clickable
    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //the view holder holds our views
        //all the views we specified  in our layout will be found here by their id
        CircleImageView imageProfile;
        TextView CustomerName;
        TextView ItemBought;
        TextView time;
        TextView amount;
        ImageButton callOrTextessage;


        public AdapterViewHolder(View itemView) {
            super(itemView);
            //we use an itemView to find the views by their id
            imageProfile = itemView.findViewById(R.id.profile_of_customer);
            CustomerName = itemView.findViewById(R.id.text_for_Customer_Name2);
            ItemBought = itemView.findViewById(R.id.Text_for_item_bought);
            time= itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.how_much);
            callOrTextessage = itemView.findViewById(R.id.call_or_send_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //when we click the recyclerview
            //we move the cursor to our adapter position
            mCursor.moveToPosition(getAdapterPosition());
            String customerName = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_CUSTOMER_NAME));
            String boughtStuff = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_ITEM_BOUGHT));
            String howMuch = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_HOW_MUCH));
            String Time = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_TIME_STAMP));
           String imgUri = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_IMAGE));
            String DebtOrChange = mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_DEBT_OR_CHANGE));
            String phoneNo= mCursor.getString(mCursor.getColumnIndex(CreditorsAndDebtorsDataBase.COLUMN_PHONE_NUMBER));
            ///we launch an explicitIntent ..see def above
            Intent intentToStartProfileActivity = new Intent(context, profileActivity.class);
            //we put the values we want to display in the next page
            intentToStartProfileActivity.putExtra("customer_name", customerName);
            intentToStartProfileActivity.putExtra("bought_stuff", boughtStuff);
            intentToStartProfileActivity.putExtra("how_much", howMuch);
            intentToStartProfileActivity.putExtra("time", Time);
            intentToStartProfileActivity.putExtra("debt_or_change", DebtOrChange);
            intentToStartProfileActivity.putExtra("imgUri", imgUri);
            intentToStartProfileActivity.putExtra("phoneNo", phoneNo);
            //start another activity
            context.startActivity(intentToStartProfileActivity);
        }
    }
    //when we delete an item... we use this method to swap the cursor by closing it so that a gap will
    //not be left in the recycler view
    public void swapCursor(Cursor newCursor)
    {
        if (mCursor == null){
            mCursor.close();
        }
        mCursor =newCursor;
        if (newCursor == null){
            this.notifyDataSetChanged();
        }
        notifyDataSetChanged();
    }
    // Custom method to open dialer app
    private void openDialer(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        // Send phone number to intent as data
        intent.setData(Uri.parse("tel:" + phoneNumber));
        // Start the dialer app activity with number
        context.startActivity(intent);
    }
}
