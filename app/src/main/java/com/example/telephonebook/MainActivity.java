package com.example.telephonebook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button ;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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

    // Dynamic Array
    // 不同以往的 Array 需要宣告Size，在這邊類似於 Vector 的O bject
    private ArrayList<ContactInfo> myTelephoneBook = new ArrayList<ContactInfo>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.SetInit() ;
    }

    public void onClick(View view) {

        switch (view.getId()) {

            case  R.id.sendButton : {
                this.AddPersonToContactInfo() ;
                break ;
            }
        }
    }

    private void SetInit() {

        // Assign Function 給 Button
        Button sendButton = (Button) findViewById(R.id.sendButton) ;
        sendButton.setOnClickListener(this) ;

//        ListView listview = (ListView) findViewById(R.id.personList);


        // Constraint 要設定成 Match Constraints 才行
        ListView personList = (ListView) findViewById(R.id.personList) ;

        //ListView 要顯示的內容
        String[] str = {"新北市","台北市","台中市","台南市","高雄市","台中市","台南市","高雄市","台中市","台南市","高雄市"};

//        System.out.println(str.length) ;

        //android.R.layout.simple_list_item_1 為內建樣式，還有其他樣式可自行研究

        ArrayAdapter listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str) ;
//        ArrayAdapter<ContactInfo> listAdapter = new ArrayAdapter<ContactInfo>(this, android.R.layout.simple_list_item_1, this.myTelephoneBook) ;

        personList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, str);
//        listview.setAdapter(adapter);

//        ListView personList = (ListView) findViewById(R.id.personList) ;


    }

    private void AddPersonToContactInfo() {

        // 先綁定 EditText
        EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
        EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

        if ( nameInputEditText.getText().toString().length() == 0 || phoneInputEditText.getText().toString().length() == 0 ){

            Toast.makeText(this, "您輸入的資料不完整，請重新再試！", Toast.LENGTH_LONG).show();
        } else {

            String personName = nameInputEditText.getText().toString() ;
            String personPhoneNumber = phoneInputEditText.getText().toString() ;

            ContactInfo thisPerson = new ContactInfo() ;
            thisPerson.SetUserName(personName);
            thisPerson.SetPhoneNumber(personPhoneNumber);

            this.myTelephoneBook.add(thisPerson) ;
            Toast.makeText(this, "新增通訊錄成功！", Toast.LENGTH_LONG).show();
        }
    }

}
