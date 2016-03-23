package com.jsrwares.sqlcipher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jsrwares.sqlcipher.db.ContactDao;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactDao mContactDao;
    private EditText mFirstNameInput;
    private EditText mLastNameInput;
    private EditText mEmailInput;
    private EditText mPhoneInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.add_activity_header));
        setSupportActionBar(toolbar);

        mFirstNameInput = (EditText) findViewById(R.id.firstNameInput);
        mLastNameInput = (EditText) findViewById(R.id.lastNameInput);
        mEmailInput = (EditText) findViewById(R.id.emailInput);
        mPhoneInput = (EditText) findViewById(R.id.phoneInput);

        Button saveContact = (Button) findViewById(R.id.saveContactButton);
        Button abortAddButton = (Button) findViewById(R.id.abortAddButton);
        saveContact.setOnClickListener(this);
        abortAddButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContactDao = new ContactDao(this, getString(R.string.database_password));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mContactDao.close();
    }

    private void performAdd() {
        String firstName = mFirstNameInput.getText().toString();
        String lastName = mLastNameInput.getText().toString();
        String email = mEmailInput.getText().toString();
        String phone = mPhoneInput.getText().toString();
        Contact contact = new Contact(null, firstName, lastName, email, phone);
        int id = -1;
        id = mContactDao.insert(contact);
        if (id >= 0) { // Success
            Toast.makeText(this, contact.toString() + " added.", Toast.LENGTH_SHORT).show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveContactButton:
                performAdd();
                break;
            case R.id.abortAddButton:
                finish();
                break;
            default:
                break;
        }
    }
}
