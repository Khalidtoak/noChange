package com.group1.swepproject.user.nochange.DataBaseForTheDebtorsAndCreditors.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.group1.swepproject.user.nochange.Fragments.PersonalDashBoard;
import com.group1.swepproject.user.nochange.Fragments.PersonalDebt;
import com.group1.swepproject.user.nochange.R;

public class BroadcastRecieverForNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        int debt = new PersonalDashBoard().giveDebt(context);
        int credit = new PersonalDashBoard().giveCred(context);
        String debtsShouldS = "debts";
        String creditsShouldS = "credits";

        if(debt == 1){
            debtsShouldS = "debt";
        }
        if(credit == 1){
            creditsShouldS = "credit";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_happy_face).setContentIntent(PendingIntent.getActivity(context, 0,
                new Intent(context, PersonalDebt.class),
                0)).setAutoCancel(true).setContentTitle
                (String.format("You have %d %s and %d %s", debt, debtsShouldS, credit, creditsShouldS))
                .setContentText("Touch to see details");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1400);
    }
}
