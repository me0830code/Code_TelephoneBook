package com.example.telephonebook;

public class ContactInfo {

    private String userName ;
    private String phoneNumber ;

    // 負責設定使用者名稱
    public void SetUserName(String name) {
        this.userName = name ;
    }

    // 讀取使用者名稱
    public String GetUserName() {
        return this.userName ;
    }

    // 負責設定使用者手機號碼
    public void SetPhoneNumber(String phone) {
        this.phoneNumber = phone ;
    }

    // 讀取使用者手機號碼
    public String GetPhoneNumber() {
        return this.phoneNumber ;
    }
}
