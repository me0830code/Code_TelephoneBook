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

    // Columns for Creating Table
    private final String uID = "ID" ;
    private final String userName = "User" ;
    private final String phoneNum = "Phone" ;

    // Database name & version
    private static final String databaseName = "LocalDB" ;
    private static final int databaseVersion = 1 ;

    // Table name
    private final String tableName = "TelephoneBook" ;

    // SQL Script for Creating Table
    // CREATE TABLE IF NOT EXISTS TelephoneBook ( ID INTEGER PRIMARY KEY AUTOINCREMENT, User VARCHAR(255), Phone VARCHAR(255) ) ;
    private final String createTableSQL = "CREATE TABLE IF NOT EXISTS " + this.tableName +
            " ( " + this.uID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + this.userName + " VARCHAR(255), "
            + this.phoneNum + " VARCHAR(255)" +
            " ) ; " ;

    // SQL Script for Deleting Table
    // DROP TABLE IF EXISTS TelephoneBook ;
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

            Toast.makeText(this.nowContext, "Create Table Fail :\n\n" + e.toString(), Toast.LENGTH_LONG).show() ;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            sqLiteDatabase.execSQL(this.deleteTableSQL) ;
        } catch ( Exception e ) {

            Toast.makeText(this.nowContext, "Delete Table Fail :\n\n" + e.toString(), Toast.LENGTH_LONG).show() ;
        }
    }

    public void InsertToLocalDB(String name, String phone) {

        SQLiteDatabase myLocalDB = this.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(this.userName, name) ;
        contentValues.put(this.phoneNum, phone) ;

        long nowID = myLocalDB.insert(this.tableName, null, contentValues) ;

        // The 「final」 property can make variables read-only
        final String successLog = "Insert success！" + "\n\n" +
                                  "Name : " + name + "\n" + "Phone : " + phone ;

        Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;
    }

    public void UpdateToLocalDB(int id, String newName, String newPhone) {

        SQLiteDatabase myLocalDB = this.getWritableDatabase() ;

        ContentValues contentValues = new ContentValues() ;
        contentValues.put(this.userName, newName) ;
        contentValues.put(this.phoneNum, newPhone) ;

        String[] argu = { String.valueOf(id) } ;

        // The 「return value」 is the number of affected rows
        int affectRow = myLocalDB.update(this.tableName, contentValues, this.uID + " = ? ", argu) ;

        if (affectRow == 0) {

            Toast.makeText(this.nowContext, "Update fail, please try again！", Toast.LENGTH_LONG).show() ;
        } else {

            // The 「final」 property can make variables read-only
            final String successLog = "Update success！" + "\n\n" +
                                      "Name : " + newName + "\n" + "Phone : " + newPhone ;

            Toast.makeText(this.nowContext, successLog, Toast.LENGTH_LONG).show() ;
        }
    }

    public void DeleteFromLocalDB(int id) {

        SQLiteDatabase myLocalDB = this.getWritableDatabase() ;

        String[] argu = { String.valueOf(id) } ;

        // The 「return value」 is the number of affected rows
        int affectRow = myLocalDB.delete(this.tableName, this.uID + " = ?", argu) ;

        if (affectRow == 0) {

            Toast.makeText(this.nowContext, "Delete fail, please try again！", Toast.LENGTH_LONG).show() ;
        } else {

            Toast.makeText(this.nowContext, "Delete success！", Toast.LENGTH_LONG).show() ;
        }
    }

    // Read the Data Set of this Local Database
    public ArrayList<ContactInfo> GetTotalContactInfo() {

        ArrayList<ContactInfo> totalContactInfo = new ArrayList<ContactInfo>() ;

        SQLiteDatabase myLocalDB = this.getReadableDatabase() ;
        String[] myColumn = { this.uID, this.userName, this.phoneNum } ;

        // Setting Database Traversal
        Cursor myCursor = myLocalDB.query(this.tableName, myColumn, null, null, null, null, null) ;

        while ( myCursor.moveToNext() ) {
            int id = myCursor.getInt(myCursor.getColumnIndex(this.uID)) ;
            String name = myCursor.getString(myCursor.getColumnIndex(this.userName)) ;
            String phone = myCursor.getString(myCursor.getColumnIndex(this.phoneNum)) ;

            // Read the eachContactInfo of Each Person at Each Time in Loop
            ContactInfo eachContactInfo = new ContactInfo() ;
            eachContactInfo.init(id, name, phone) ;

            // Add Total eachContactInfo into totalContactInfo Which is a Telephone Book
            totalContactInfo.add(eachContactInfo) ;
        }

        return totalContactInfo ;
     }
}

