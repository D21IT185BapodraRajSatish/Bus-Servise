package com.example.proto_type_1;

import android.content.Context;

public class RegisterConductor extends Person {

    String id,password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterConductor() {
    }

    @Override
    public void SENDMAIL(Context ct) {

    }

    @Override
    public void SENDMAIL(Context ct,String TEMP1) {

        String message = "Dear " + getfName() + " " + getlName() + "" + ",\n" +
                "You are successfully registered for the Designation as conductor in city bus services and your id is " + getId() + " \n" +
                "\n" +
                "Click the below Link to download our CONDUCTOR APPLICATION\n \uD83D\uDC47\uD83D\uDC47\uD83D\uDC47 \n "+TEMP1+
                "\n" +
                "Thank You,\n" +
                "Bus Services";

        String subject = "Register Conductor";

        String toast_message="REGISTER CONDUCTOR SUCCESSFUL";

        JavaMailAPI javaMailAPI = new JavaMailAPI(ct,getEmail(), subject, message);
        javaMailAPI.SetToastMessage(toast_message);
        javaMailAPI.execute();
    }

    @Override
    public void SENDMAIL(Context ct, String Message, String Subject, String Toast_message) {

    }
}
