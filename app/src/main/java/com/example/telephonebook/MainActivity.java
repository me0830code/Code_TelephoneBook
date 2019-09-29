package com.example.telephonebook ;

import androidx.appcompat.app.AppCompatActivity ;
import android.os.Bundle ;

import android.view.View ;

import android.widget.AdapterView;
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

    // 用來記錄 Click 當下那筆資料的 uID，方便之後更新、刪除時使用
    private int nowUID = -1 ;

    // Dynamic Array
    // 不同以往的 Array 需要宣告Size，在這邊類似於 Vector 的 Object

    // myTelephoneBook 是裝 Local Database 的 Data Set
    private ArrayList<ContactInfo> myTelephoneBook = new ArrayList<ContactInfo>() ;

    // totalListViewData 是放 ListView 所要使用的 Data Source
    private ArrayList<String> totalListViewData = new ArrayList<String>() ;

    // ListView 裝 Data Source 的容器
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

        // 讀取 Local Database 的資料，即 Data Set
        this.myTelephoneBook.addAll(myDBHelper.GetTotalContactInfo()) ;

        // 同時設定 ListView 所要使用的 Data Source
        this.totalListViewData = this.GetTotalListViewData() ;

        // Assign Function 給 Button
        Button insertButton = (Button) findViewById(R.id.insertButton) ;
        insertButton.setOnClickListener(this) ;

        Button updateButton = (Button) findViewById(R.id.updateButton) ;
        updateButton.setOnClickListener(this) ;

        Button deleteButton = (Button) findViewById(R.id.deleteButton) ;
        deleteButton.setOnClickListener(this) ;

        // Constraint 要設定成 Match Constraints 才行
        // Assign Function 給 ListView
        ListView personList = (ListView) findViewById(R.id.personList) ;
        personList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int indexOfClickItem = parent.getPositionForView(view) ;

                ContactInfo personContactInfo = myTelephoneBook.get(indexOfClickItem) ;

                int uID = personContactInfo.GetUserID() ;
                String name = personContactInfo.GetUserName() ;
                String phone = personContactInfo.GetPhoneNumber() ;

                // 帶入當前所 Click 的 Item 的資訊，包含 UserName 和 PhoneNumber
                EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
                EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

                nameInputEditText.setText(name);
                phoneInputEditText.setText(phone);

                // 設定當前所 Click 的 Item 的 uID
                nowUID = uID ;
            }
        });

        // 第三個參數是放ListView 要顯示的內容，即是 Data Source
        this.listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.totalListViewData) ;

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

        // 先綁定 EditText
        EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
        EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

        String personName = nameInputEditText.getText().toString() ;
        String personPhoneNumber = phoneInputEditText.getText().toString() ;

        if ( this.IsLeagalForOperation(personName, personPhoneNumber) ) {

            // Handle 成功，清空兩個 Input
            nameInputEditText.setText("") ;
            phoneInputEditText.setText("") ;

            switch (type) {

                case Insert : {
                    this.myDBHelper.InsertToLocalDB(personName, personPhoneNumber) ;

                    break ;
                }

                case Update : {
                    this.myDBHelper.UpdateToLocalDB(this.nowUID, personName, personPhoneNumber) ;

                    break ;
                }

                case Delete : {
                    this.myDBHelper.DeleteFromLocalDB(this.nowUID) ;

                    break ;
                }
            }

            // 每次 Handle 完成後，更新 Data Set
            this.myTelephoneBook.clear() ;
            this.myTelephoneBook.addAll(myDBHelper.GetTotalContactInfo()) ;

            // 同時也更新 ListView 的 Data Source
            this.totalListViewData.clear() ;
            this.totalListViewData.addAll(this.GetTotalListViewData()) ;

            // ListView 的 Adapter 作監聽，去看 Data Source 使否有變動
            this.listAdapter.notifyDataSetChanged() ;
        } else {

            Toast.makeText(this, "您的資料不完整，請重新再試！", Toast.LENGTH_LONG).show() ;
        }
    }

    private Boolean IsLeagalForOperation(String name, String phone) {

        if ( name.length() == 0 || phone.length() == 0 ) {
            return false ;
        }

        return true ;
    }

    // 取得 ListView 所要用的 Data Source
    private ArrayList<String> GetTotalListViewData() {

        ArrayList<String> totalListViewData = new ArrayList<String>() ;

        for(int index = 0 ; index < this.myTelephoneBook.size() ; index ++) {

            ContactInfo eachPersonContactInfo = this.myTelephoneBook.get(index) ;

            String eachListViewData = "[" + eachPersonContactInfo.GetUserID() + "] " +
                                      "姓名 : " + eachPersonContactInfo.GetUserName() + " " +
                                      "電話 : " + eachPersonContactInfo.GetPhoneNumber() ;

            totalListViewData.add(eachListViewData) ;
        }

        return totalListViewData ;
    }

}
