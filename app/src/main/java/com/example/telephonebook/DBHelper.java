package com.example.telephonebook ;

import android.content.ContentValues ;
import android.content.Context ;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase ;
import android.database.sqlite.SQLiteOpenHelper ;

import android.widget.Toast ;

import java.util.ArrayList ;

public class DBHelper extends SQLiteOpenHelper {

    private Context nowContext ;

    // For 創建 Table 時的欄位名稱
    private final String uID = "ID" ;
    private final String userName = "User" ;
    private final String phoneNum = "Phone" ;

    // Database 名稱、版本
    private static final String databaseName = "LocalDB" ;
    private static final int databaseVersion = 1 ;

    // Table 名稱
    private final String tableName = "TelephoneBook" ;

    // 創建 Table 的 SQL 語法
    // CREATE TABLE IF NOT EXISTS ContactInfo ( ID INTEGER PRIMARY KEY AUTOINCREMENT, User VARCHAR(255), Phone VARCHAR(255) ) ;
    private final String createTableSQL = "CREATE TABLE IF NOT EXISTS " + this.tableName +
            " ( " + this.uID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + this.userName + " VARCHAR(255), "
            + this.phoneNum + " VARCHAR(255)" +
            " ) ; " ;

    // 刪除 Table 的 SQL 語法
    // DROP TABLE IF EXISTS ContactInfo ;
    private final String deleteTableSQL = "DROP TABLE IF EXISTS " + this.tableName + " ; " ;

    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion) ;

        this.nowContext = context ;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL(this.createTableSQL) ;
        } catch ( Exception e ) {

            Toast.makeText(this.nowContext, "Create Table Fail -> " + e.toString(), Toast.LENGTH_LONG).show() ;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            sqLiteDatabase.execSQL(this.deleteTableSQL) ;
        } catch ( Exception e ) {

            Toast.makeText(this.nowContext, "Delete Table Fail -> " + e.toString(), Toast.LENGTH_LONG).show() ;
        }
    }

    public void InsertToLocalDB(String name, String phone) {

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(this.userName, name) ;
        contentValues.put(this.phoneNum, phone) ;

        long nowID = myLocalDB.insert(this.tableName, null, contentValues) ;

        // final 用法就如同 constant，無法再更改變數的值
        final String successLog = "新增成功！" + "\n\n" + "編號 : " + nowID + "\n" +
                                  "姓名 : " + name + "\n" + "電話 : " + phone ;

        Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;
    }

    public void UpdateToLocalDB(int id, String newName, String newPhone) {

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(this.userName, newName) ;
        contentValues.put(this.phoneNum, newPhone) ;

        String[] argu = { String.valueOf(id) } ;

        // 回傳的值是「所影響Row」的數量
        int affectRow = myLocalDB.update(this.tableName, contentValues, this.uID + " = ? ", argu) ;

        if (affectRow == 0) {

            Toast.makeText(this.nowContext, "更新失敗，請重新再試！", Toast.LENGTH_LONG).show() ;
        } else {

            // final 用法就如同 constant，無法再更改變數的值
            final String successLog = "更新成功！" + "\n\n" + "編號 : " + id + "\n" +
                                      "姓名 : " + newName + "\n" + "電話 : " + newPhone ;

            Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;
        }
    }

    public void DeleteFromLocalDB(int id) {

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;

        String[] argu = { String.valueOf(id) } ;

        // 回傳的值是「所影響Row」的數量
        int affectRow = myLocalDB.delete(this.tableName, this.uID + " = ?", argu) ;

        if (affectRow == 0) {

            Toast.makeText(this.nowContext, "刪除失敗，請重新再試！", Toast.LENGTH_LONG).show() ;
        } else {

            Toast.makeText(this.nowContext, "刪除成功！", Toast.LENGTH_LONG).show() ;
        }
    }

    // 讀取 Local Database 的 Data Set
    public ArrayList<ContactInfo> GetTotalContactInfo() {

        ArrayList<ContactInfo> totalContactInfo = new ArrayList<ContactInfo>() ;

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getReadableDatabase() ;
        String[] myColumn = { this.uID, this.userName, this.phoneNum } ;

        // 設定 Traversal
        Cursor myCursor = myLocalDB.query(this.tableName, myColumn, null, null, null, null, null) ;

        while ( myCursor.moveToNext() ) {
            int id = myCursor.getInt(myCursor.getColumnIndex(this.uID)) ;
            String name = myCursor.getString(myCursor.getColumnIndex(this.userName)) ;
            String phone = myCursor.getString(myCursor.getColumnIndex(this.phoneNum)) ;

            // 每次進 While 都去讀取「一個人的所有資料」
            ContactInfo eachContactInfo = new ContactInfo() ;
            eachContactInfo.init(id, name, phone) ;

            // 把「所有人」的 ContactInfo 都加到  TotalContactInfo 之中，來形成通訊錄
            totalContactInfo.add(eachContactInfo) ;
        }

        return totalContactInfo ;
     }
}

