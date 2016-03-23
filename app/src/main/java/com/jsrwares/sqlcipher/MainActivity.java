package com.jsrwares.sqlcipher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jsrwares.sqlcipher.db.ContactDao;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ContactDao mContactDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.main_activity_header));
        setSupportActionBar(toolbar);
        Button addContactButton = (Button) findViewById(R.id.addButton);
        addContactButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        mContactDao = new ContactDao(this, getString(R.string.database_password));
        contactList = mContactDao.selectAll();
        Contact[] contactArray = new Contact[contactList.size()];
        contactArray = contactList.toArray(contactArray);

        ContactArrayAdapter adapter;
        adapter = new ContactArrayAdapter(this, R.layout.list_view_row_item, contactArray);

        ListView contactListView = (ListView) findViewById(R.id.mainList);
        if (contactListView != null) {
            contactListView.setAdapter(adapter);
        }
        mContactDao.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                Intent intent = new Intent(this, AddContactActivity.class);
                startActivity(intent);
                break;
        }
    }
}
