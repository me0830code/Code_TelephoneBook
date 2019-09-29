package com.example.telephonebook ;

// ContactInfo Model
public class ContactInfo {

    private int uID ;
    private String userName ;
    private String phoneNumber ;

    // Setting userName & userPhone
    public void init(int id, String name, String phone) {

        this.uID = id ;
        this.userName = name ;
        this.phoneNumber = phone ;
    }

    // Read userID
    public int GetUserID() {
        return this.uID ;
    }

    // Read userName
    public String GetUserName() {
        return this.userName ;
    }

    // Read userPhone
    public String GetPhoneNumber() {
        return this.phoneNumber ;
    }
}
