package com.example.telephonebook ;

import androidx.appcompat.app.AppCompatActivity ;

import android.content.Context;
import android.os.Bundle ;

import android.view.View ;

import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button ;
import android.widget.EditText ;
import android.widget.Toast ;

import android.widget.ArrayAdapter ;
import android.widget.ListView ;

import java.util.ArrayList ;

// Implements View.OnClickListener can ask MainActivity to implement onClick() function
//
// Also, we can use switch case to handle each specific operation
//
// button.setOnClickListener(new View.OnClickListener() {
//
//      @Override
//      public void onClick(View view) {
//          switch (view.getId()) {
//              case ...
//              case ...
//          }
//      }
// });
//

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Determine the Specific Operation
    private enum HandleType {

        Insert, Update, Delete
    }

    // Record the currentID of this Clicked Row
    private int nowUID = -1 ;

    // Dynamic Array
    // Array Needs to Declare with Size, but ArrayList Don't
    // ArrayList is Similar With Vector Object

    // Using myTelephoneBook to Hold Data Set of Local Database
    private ArrayList<ContactInfo> myTelephoneBook = new ArrayList<ContactInfo>() ;

    // Using totalListViewData to Hold Data Source of ListView
    private ArrayList<String> totalListViewData = new ArrayList<String>() ;

    // ArrayAdapter is the Container of ListView's Data Source
    private ArrayAdapter listAdapter ;

    // Call LocalDB to Use
    private DBHelper myDBHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main) ;

        this.SetInit() ;
    }

    private void SetInit() {

        // Let LocalDB Bind to Current Context
        this.myDBHelper = new DBHelper(this) ;

        // Read Data Set of this Local Database
        this.myTelephoneBook.addAll(myDBHelper.GetTotalContactInfo()) ;

        // Also, Setting the Data Source to ListView
        this.totalListViewData = this.GetTotalDataSource() ;

        // Assign Function for Each Button
        Button insertButton = (Button) findViewById(R.id.insertButton) ;
        insertButton.setOnClickListener(this) ;

        Button updateButton = (Button) findViewById(R.id.updateButton) ;
        updateButton.setOnClickListener(this) ;

        Button deleteButton = (Button) findViewById(R.id.deleteButton) ;
        deleteButton.setOnClickListener(this) ;

        // Constraint Needs to Set as Match Constraints

        // Assign Function for ListView
        ListView personList = (ListView) findViewById(R.id.personList) ;
        personList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int indexOfClickItem = parent.getPositionForView(view) ;

                ContactInfo personContactInfo = myTelephoneBook.get(indexOfClickItem) ;

                int uID = personContactInfo.GetUserID() ;
                String name = personContactInfo.GetUserName() ;
                String phone = personContactInfo.GetPhoneNumber() ;

                // Passing UserName & PhoneNumber of this Clicked Row in ListView
                EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
                EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

                nameInputEditText.setText(name);
                phoneInputEditText.setText(phone);

                // Also, Setting the currentID of this Clicked Row
                nowUID = uID ;
            }
        });

        // The Third Parameter is the Data Source(Showing Information) of ListView
        this.listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.totalListViewData) ;

        // Setting ArrayAdapter(The Container of ListView's Data Source) to ListView
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

        // Close the Keyboard
        DismissKeyboard() ;

        // Binding Each EditText by Each R.id
        EditText nameInputEditText = (EditText) findViewById(R.id.uNameInput) ;
        EditText phoneInputEditText = (EditText) findViewById(R.id.pNumInput) ;

        String personName = nameInputEditText.getText().toString() ;
        String personPhoneNumber = phoneInputEditText.getText().toString() ;

        if ( this.IsLeagalForOperation(personName, personPhoneNumber) ) {

            // Handle Operation Success
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

            // Update Data Set & Data Source When Operation Success

            // Update Data Set
            this.myTelephoneBook.clear() ;
            this.myTelephoneBook.addAll(myDBHelper.GetTotalContactInfo()) ;

            // Update Data Source
            this.totalListViewData.clear() ;
            this.totalListViewData.addAll(this.GetTotalDataSource()) ;

            // Notify ArrayAdapter to Check Whether Data Source is Changed or Not
            // If Yes, then System will Refresh ListView Automatically
            this.listAdapter.notifyDataSetChanged() ;
        } else {

            Toast.makeText(this, "Your information is not complete :(\nPlease try again！", Toast.LENGTH_LONG).show() ;
        }
    }

    private void DismissKeyboard() {

        View view = this.getCurrentFocus() ;

        if (view == null) { return ; }

        InputMethodManager myKeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE) ;
        myKeyboard.hideSoftInputFromWindow(view.getWindowToken(), 0) ;
    }

    private Boolean IsLeagalForOperation(String name, String phone) {

        if ( name.length() == 0 || phone.length() == 0 ) {
            return false ;
        }

        return true ;
    }

    // Get the Data Source of ListView
    private ArrayList<String> GetTotalDataSource() {

        ArrayList<String> totalListViewData = new ArrayList<String>() ;

        for(int index = 0 ; index < this.myTelephoneBook.size() ; index ++) {

            ContactInfo eachPersonContactInfo = this.myTelephoneBook.get(index) ;

            String eachListViewData = "[No. " + (index + 1) + "]" + "\n" +
                                      "Name : " + eachPersonContactInfo.GetUserName() + "\n" +
                                      "Phone : " + eachPersonContactInfo.GetPhoneNumber() ;

            totalListViewData.add(eachListViewData) ;
        }

        return totalListViewData ;
    }

}
