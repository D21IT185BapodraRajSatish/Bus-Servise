package com.example.proto_type_1;

import android.content.Context;

public class RegisterUser extends Person {
    String cardID, balance;

    public RegisterUser() {
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    @Override
    public void SENDMAIL(Context ct) {

        String message = "Dear Passenger,\n" +
                "\t\t\t\tYour Smart Card for City Bus Service is issued. The card contains the initial balance of  ₹ 50.0+ ₹" + (Double.parseDouble(balance) - 50.0) + " i.e ₹ " + balance + ".This card is only useful for cashless transaction in city Bus. Card should have minimum balance of ₹ 25.\n" +
                "\n" +
                "\t\t\t\tTo Recharge card you have to go to respected card recharge office issued by government of particular district.\n" +
                "\n" +
                "Thank You,\n" +
                "Bus Services.";

        String subject = "ISSUE CARD";

        String toast_message="ISSUE CARD SUCCESSFUL";

        JavaMailAPI javaMailAPI = new JavaMailAPI(ct, getEmail(),subject,message);
        javaMailAPI.SetToastMessage(toast_message);
        javaMailAPI.execute();
    }

    @Override
    public void SENDMAIL(Context ct, String TEMP1) {

    }

    @Override
    public void SENDMAIL(Context ct, String Message, String Subject, String Toast_message) {

    }
}
