package com.group1.swepproject.user.nochange.ModelClass;

import java.io.Serializable;

/**
 * Created by user on 4/1/2018.
 */

public class personalWallet implements Serializable {
    /*these variable names are the set of fields we want to display in our file*/
    private String name, amount, returnStatement;
    private boolean isDebt;

    public personalWallet(String name, String amount, boolean isThisADebt){
        this.name = name;
        this.amount = amount;
        /*Now you want to save either a debt or a credit... so if this is a debt,
        we return the statements specified in the string
         */
        if(isThisADebt){
            returnStatement = "To pay ";
            isDebt = true;
        }
        else{
            returnStatement = "To collect ";
            isDebt = false;
        }
    }
    /* we created setters and getter methods  so that we can use it to set user entries to certain values
    before we save them and get them when we want to display them on the screen
     */

    public String getNameOfWallet(){
        return name;
    }

    public String getAmount(){
        return amount;
    }

    public String getReturnStatement(){
        return returnStatement;
    }
    //return to or from depending on whether you want to pay or collect money from someone
    public String getPreposition(){
        if(returnStatement.equals("To pay ")){
            return " to ";
        }
        else{
            return " from ";
        }
    }

    public boolean getIsDebt(){
        return isDebt;
    }
}
