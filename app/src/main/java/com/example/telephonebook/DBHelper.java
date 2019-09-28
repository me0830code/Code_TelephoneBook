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

//    private static final String DATABASE_NAME = "myDatabase";    // Database Name
//    private static final String TABLE_NAME = "myTable";   // Table Name
//    private static final int DATABASE_Version = 1;.    // Database Version
//    private static final String UID="_id";     // Column I (Primary Key)
//    private static final String NAME = "Name";    //Column II
//    private static final String MyPASSWORD= "Password";    // Column III
//    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
//            " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255) ,"+ MyPASSWORD+" VARCHAR(225));";
//    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
//    private Context context;


    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion) ;

        this.nowContext = context ;
//        this.myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        super.onCreate(sqLiteDatabase)  ;

        try {

            sqLiteDatabase.execSQL(this.createTableSQL) ;
        } catch ( Exception e ) {

            Toast.makeText(this.nowContext, "Create Table Fail -> " + e.toString(), Toast.LENGTH_LONG).show() ;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        super.onUpgrade(sqLiteDatabase, i, i1) ;


        try {

            sqLiteDatabase.execSQL(this.deleteTableSQL) ;
//            onCreate(sqLiteDatabase);
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
        final String successLog = "新增成功！" + "\n\n" + "編號 : " + nowID + "\n" + "姓名 : " + name + "\n" + "電話 : " + phone ;
        Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;
    }

    public void UpdateToLocalDB(int id, String newName, String newPhone) {

        Toast.makeText(this.nowContext, "??? -> ", Toast.LENGTH_LONG).show() ;

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;

//        id = 1 ;

//        SQLiteDatabase db = myhelper.getWritableDatabase();
////        ContentValues contentValues = new ContentValues();
////        contentValues.put(myDbHelper.NAME,newName);
////        String[] whereArgs= {oldName};
////        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
////        return count;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(this.userName, newName) ;
        contentValues.put(this.phoneNum, newPhone) ;

        // The Third param of db.update() should be the where clause only, and the fourth is the actual condition values
        //You would need to put quotes around your Id.
        // Something like: String strFilter = "_id='" + Id + "'"; – Guillaume Jun 22 '16 at 17:27

        String[] argu = { String.valueOf(id) } ;
        int temp = myLocalDB.update(this.tableName, contentValues, this.uID + " = ? ", argu) ;
//        int temp = myLocalDB.update(this.tableName, contentValues, this.uID + " = " + String.valueOf(uID), null) ;
//
        final String successLog = "修改成功！" + "\n\n" + "編號 : " + uID + "\n" + "姓名 : " + newName + "\n" + "電話 : " + newPhone ;
//        Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;

        Toast.makeText(this.nowContext, "??? -> " + temp, Toast.LENGTH_LONG).show() ;
    }

    public void DeleteFromLocalDB(int id) {
        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getWritableDatabase() ;

        String[] argu = { String.valueOf(this.uID) } ;

//        int temp = myLocalDB.delete(this.tableName, this.uID + " = ?", argu) ;
        int temp = myLocalDB.delete(this.tableName, this.uID + " = ?", new String[] { String.valueOf(id) }) ;

//        myLocalDB.execSQL(("DELETE FROM "+ this.tableName +" WHERE "+ this.uID + " = ' " + String.valueOf(id) + " '"));

        Toast.makeText(this.nowContext, "??? -> " + temp, Toast.LENGTH_LONG).show() ;

//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        String[] whereArgs ={uname};
//
//        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
//        return  count;
    }

    public ArrayList<String> GetContactInfo() {



//        SQLiteDatabase db = myhelper.getWritableDatabase();
//        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.MyPASSWORD};
//        Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
//        StringBuffer buffer= new StringBuffer();
//        while (cursor.moveToNext())
//        {
//            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
//            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
//            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.MyPASSWORD));
//            buffer.append(cid+ "   " + name + "   " + password +" \n");
//        }
//        return buffer.toString();

        ArrayList<String> TotalContactInfo = new ArrayList<String>() ;

        SQLiteDatabase myLocalDB = new DBHelper(this.nowContext).getReadableDatabase() ;
        String[] myColumn = { this.uID, this.userName, this.phoneNum } ;

        Cursor myCursor = myLocalDB.query(this.tableName, myColumn, null, null, null, null, null) ;

        while ( myCursor.moveToNext() ) {
            int id = myCursor.getInt(myCursor.getColumnIndex(this.uID)) ;
            String name = myCursor.getString(myCursor.getColumnIndex(this.userName)) ;
            String phone = myCursor.getString(myCursor.getColumnIndex(this.phoneNum)) ;

            TotalContactInfo.add( "[" + String.valueOf(id) + "] " + "姓名 : " + name + " 電話 : " + phone) ;
        }

        return TotalContactInfo ;
    }

}

