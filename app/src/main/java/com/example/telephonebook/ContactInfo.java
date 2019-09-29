package com.example.telephonebook ;

public class ContactInfo {

    private int uID ;
    private String userName ;
    private String phoneNumber ;

    // 負責設定使用者名稱、電話號碼
    public void init(int id, String name, String phone) {

        this.uID = id ;
        this.userName = name ;
        this.phoneNumber = phone ;
    }

    // 讀取使用者ID
    public int GetUserID() {
        return this.uID ;
    }

    // 讀取使用者名稱
    public String GetUserName() {
        return this.userName ;
    }

    // 讀取使用者手機號碼
    public String GetPhoneNumber() {
        return this.phoneNumber ;
    }
}
