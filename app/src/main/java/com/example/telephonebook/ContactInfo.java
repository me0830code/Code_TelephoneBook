package com.example.telephonebook ;

public class ContactInfo {

    private String userName ;
    private String phoneNumber ;

    // 這裡 final 是為了讓 init() 不能被其他人 @override
    // 負責設定使用者名稱、電話號碼
    public void init(String name, String phone) {

        this.userName = name ;
        this.phoneNumber = phone ;
    }

//    // 讀取使用者名稱
//    public String GetUserName() {
//        return this.userName ;
//    }
//
//    // 讀取使用者手機號碼
//    public String GetPhoneNumber() {
//        return this.phoneNumber ;
//    }
//
//    public String ShowingInList() {
//        return this.userName + " " + this.phoneNumber ;
//    }
}
