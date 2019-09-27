package com.example.telephonebook ;

public class ContactInfo {

    private String userName ;
    private String phoneNumber ;

    // 負責設定使用者名稱、電話號碼
    public void init(String name, String phone) {

        this.userName = name ;
        this.phoneNumber = phone ;
    }
}
