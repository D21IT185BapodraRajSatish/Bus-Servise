package com.example.proto_type_1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import javax.mail.Flags;

abstract public class Person {

    public String fName, lName, email, phoneNo, gender, address, date;

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void DOB(Context ct, TextInputLayout date) {
        Calendar c;
        DatePickerDialog dpg;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        //Store selected Date in Text Box
        dpg = new DatePickerDialog(ct, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int MYear, int MMonth, int MDay) {
                date.getEditText().setText(MDay + "/" + (MMonth + 1) + "/" + MYear);
            }
        }, day, month, year);
        //show Date picker
        dpg.show();
    }

    public void DOB(Context ct, TextView date) {
        Calendar c;
        DatePickerDialog dpg;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        //Store selected Date in Text Box
        dpg = new DatePickerDialog(ct, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int MYear, int MMonth, int MDay) {
                date.setText(MDay + "/" + (MMonth + 1) + "/" + MYear);
            }
        }, day, month, year);
        //show Date picker
        dpg.show();
    }

    abstract public void SENDMAIL(Context ct);

    abstract public void SENDMAIL(Context ct,String TEMP1);

    abstract public void SENDMAIL(Context ct,String Message,String Subject,String Toast_message);

}
