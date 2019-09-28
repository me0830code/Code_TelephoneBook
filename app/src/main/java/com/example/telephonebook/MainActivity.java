package com.example.telephonebook ;

import androidx.appcompat.app.AppCompatActivity ;
import android.os.Bundle ;

import android.util.Log;
import android.view.View ;

import android.widget.Button ;
import android.widget.EditText ;
import android.widget.Toast ;

import android.widget.ArrayAdapter ;
import android.widget.ListView ;

import java.util.ArrayList ;

// Implements View.OnClickListener 是為了讓整個 MainActivity 去實作整個 onClick()
//
// 比起之前的寫法會更清楚知道每個Button所要實作的 Function 是什麼
//
// button.setOnClickListener(new View.OnClickListener() {
//
//      @Override
//      public void onClick(View view) {
//          this.Function() ;
//      }
// });
//

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 用來判斷現在是作何種動作
    private enum HandleType {

        Insert, Update, Delete
    }

    // Dynamic Array
    // 不同以往的 Array 需要宣告Size，在這邊類似於 Vector 的O bject
//    private ArrayList<ContactInfo> myTelephoneBook = new ArrayList<ContactInfo>() ;
    private ArrayList<String> tempBook = new ArrayList<String>() ;

    // ListView 的 Data Source
    private ArrayAdapter listAdapter ;

    // 調用 LocalDB 來使用
    private DBHelper myDBHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        this.SetInit() ;
    }

    private void SetInit() {

        // 將 LocaDB 綁定 Context
        this.myDBHelper = new DBHelper(this) ;
//        this.tempBook = myDBHelper.GetContactInfo() ;
        this.tempBook.addAll(myDBHelper.GetContactInfo()) ;

        // Assign Function 給 Button
        Button insertButton = (Button) findViewById(R.id.insertButton) ;
        insertButton.setOnClickListener(this) ;

        Button updateButton = (Button) findViewById(R.id.updateButton) ;
        updateButton.setOnClickListener(this) ;

        Button deleteButton = (Button) findViewById(R.id.deleteButton) ;
        deleteButton.setOnClickListener(this) ;

        // Constraint 要設定成 Match Constraints 才行
        ListView personList = (ListView) findViewById(R.id.personList) ;

        // 第三個參數是放ListView 要顯示的內容
//        this.listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.myTelephoneBook) ;
        this.listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.tempBook) ;

        // 設定 ListView 的 Data Source
        personList.setAdapter(this.listAdapter) ;
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.insertButton : {
                this.HandleContactInfo(HandleType.Insert) ;

                break ;
            }

            case R.id.updateButton : {
                this.HandleContactInfo(HandleType.Update) ;

                break ;
            }

            case R.id.deleteButton : {
                this.HandleContactInfo(HandleType.Delete) ;

                break ;
            }
        }
    }

    private void HandleContactInfo(HandleType type) {
//    private void AddPersonToContactInfo() {

        // 先綁定 EditText
        EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
        EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

        String personName = nameInputEditText.getText().toString() ;
        String personPhoneNumber = phoneInputEditText.getText().toString() ;

        if ( this.IsLeagalForOperation(personName, personPhoneNumber) ) {

            // Handle 成功，清空兩個 Input
            nameInputEditText.setText("") ;
            phoneInputEditText.setText("") ;

//            Toast.makeText(this, "開始前 : " + String.valueOf(this.listAdapter.getCount()), Toast.LENGTH_LONG ).show() ;
//            Log.d("開始前 : ",  String.valueOf(this.listAdapter.getCount())) ;
//            Log.d("開始前 : ",  String.valueOf(myDBHelper.GetContactInfo().size())) ;
            Log.d("開始前 byDB: ",  String.valueOf(myDBHelper.GetContactInfo().size())) ;
            Log.d("開始前 byBook: ",  String.valueOf(this.tempBook.size())) ;

            switch (type) {

                case Insert : {
                    this.myDBHelper.InsertToLocalDB(personName, personPhoneNumber) ;
//                    this.tempBook = myDBHelper.GetContactInfo() ;
//                    this.listAdapter.notifyDataSetChanged() ;

                    break ;
                }

                case Update : {
                    this.myDBHelper.UpdateToLocalDB(5, personName, personPhoneNumber) ;

                    break ;
                }

                case Delete : {
                    this.myDBHelper.DeleteFromLocalDB(5) ;

                    break ;
                }
            }

            this.tempBook.clear() ;
            this.tempBook.addAll(myDBHelper.GetContactInfo()) ;
//            this.tempBook = myDBHelper.GetContactInfo() ;
            this.listAdapter.notifyDataSetChanged() ;
            Log.d("開始後 byDB: ",  String.valueOf(myDBHelper.GetContactInfo().size())) ;
            Log.d("開始後 byBook: ",  String.valueOf(this.tempBook.size())) ;
//            this.listAdapter.
//            myListView.smoothScrollToPosition(theListAdapter.getCount() -1);
        } else {

            Toast.makeText(this, "您的資料不完整，請重新再試！", Toast.LENGTH_LONG).show() ;
        }


//        if ( nameInputEditText.getText().toString().length() == 0 || phoneInputEditText.getText().toString().length() == 0 ) {
//
//            // final 用法就如同 constant，無法再更改變數的值
//            final String failLog = "您輸入的資料不完整，請重新再試！" ;
//            Toast.makeText(this, failLog, Toast.LENGTH_LONG).show() ;
//        } else {
//
//            String personName = nameInputEditText.getText().toString() ;
//            String personPhoneNumber = phoneInputEditText.getText().toString() ;
//
//            ContactInfo thisPerson = new ContactInfo() ;
//            thisPerson.init(personName, personPhoneNumber) ;
//
//
//
//
//            // 更新 Data Set
//            this.myTelephoneBook.add(thisPerson) ;
//
//            // 同時更新 ListView 中的 Data Source
//            this.listAdapter.notifyDataSetChanged() ;
//
//
//
//
//
//            // final 用法就如同 constant，無法再更改變數的值
//            final String successLog = "新增通訊錄成功！" + "\n" + "姓名 : " + personName + "\n" + "電話 : " + personPhoneNumber ;
//            Toast.makeText(this, successLog, Toast.LENGTH_LONG).show() ;
//        }
    }

    private Boolean IsLeagalForOperation(String name, String phone) {

        if ( name.length() == 0 || phone.length() == 0 ) {
            return false ;
        }

        return true ;
    }
}
