package com.jsrwares.sqlcipher.db;

import android.app.Activity;

import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;
import android.util.Log;

import com.jsrwares.sqlcipher.Contact;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

public class ContactDao extends Dao {

    public ContactDao(Activity context, String password) {
        ConnectorTableDefinitions tableDefs = new ConnectorTableDefinitions();
        tableDefs.addTable("contact", getQueryCreate());
        mCconnector = Connector.getInstance(context, tableDefs);
        open(password);
    }

    public void create() {
        String sql = "";
        sql = getQueryCreate();
        executeUpdate(mCconnector.mDb, sql);
    }

    // insert contact
    public int insert(Contact contact) {
        int returnVal = -1;
        if (contact.getId() == null) {
            //Integer nextKey = -1;
            Integer nextKey = nextKey();
            returnVal = nextKey;
            contact.setId(nextKey);
        }

        String sql = getQueryInsert(contact);
        executeUpdate(mCconnector.mDb, sql); // test for success
        return returnVal;
    }

    // Updating single contact
    public boolean update(Contact contact) {
        String sql = getQueryUpdate(contact);
        String where = " WHERE id=" + contact.getId();
        sql = sql + where;
        return executeUpdate(mCconnector.mDb, sql);
    }

    // Deleting single contact
    public void delete(Contact contact) {
        String sql = getQueryDelete();
        String where = " WHERE id=" + contact.getId();
        sql = sql + where;
        executeUpdate(mCconnector.mDb, sql);
    }

    public void deleteAll() {
        String sql = getQueryDelete();
        executeUpdate(mCconnector.mDb, sql);
    }

    // Getting one contact
    public Contact select(Contact contact) {
        List<Contact> contacts;
        String sql = getQuerySelect();
        String where = " WHERE id=" + contact.getId();
        sql = sql + where;
        contacts = executeQuery(mCconnector.mDb, sql);
        if (contacts.size() > 0)
            contact = contacts.get(0);
        else
            contact = null;
        return contact;
    }

    public Contact select(int id) {
        List<Contact> contacts;
        Contact contact;
        String sql = getQuerySelect();
        String where = " WHERE id=" + id;
        sql = sql + where;
        contacts = executeQuery(mCconnector.mDb, sql);
        if (contacts.size() > 0)
            contact = contacts.get(0);
        else
            contact = null;
        return contact;
    }

    //Getting one contact
    public ArrayList<Contact> selectQueryString(String queryString) {
        ArrayList<Contact> contacts;
        String sql = getQuerySelect();
        queryString = formatQueryLike(queryString);
        String where = " WHERE nameFirst || nameLast LIKE '%" + queryString + "%'";
        String orderby = " ORDER BY contact.nameLast, contact.nameFirst";
        sql = sql + where + orderby;
        contacts = executeQuery(mCconnector.mDb, sql);
        return contacts;
    }

    private String formatQueryLike(String entry) {
        String formattedString = "";
        formattedString = entry.replace(' ', '%');
        return formattedString;
    }

    // Gets all contacts
    public ArrayList<Contact> selectAll() {
        ArrayList<Contact> contacts;
        String sql = getQuerySelect();
        String where = "";
        String orderby = " ORDER BY contact.nameLast, contact.nameFirst ASC";
        sql = sql + where + orderby;
        contacts = executeQuery(mCconnector.mDb, sql);
        contacts.size();
        return contacts;
    }

    // count contacts
    public int count() {
        int counter = 0;
        String sql = getQueryCount();
        counter = getCount(mCconnector.mDb, sql);
        return counter;
    }

    public Integer nextKey() {
        Integer nextKey = 0;
        String sql = getQueryNextKey();
        nextKey = getNextKey(mCconnector.mDb, sql);
        return nextKey;
    }

    public String getQueryNextKey() {
        String sql = "SELECT MAX(id)+1 AS id FROM contact";
        return sql;
    }

    public ArrayList<Contact> getPersonList(Cursor cursor) {
        ArrayList<Contact> contacts;
        contacts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex("id")));
                contact.setFirstName(cursor.getString(cursor.getColumnIndex("nameFirst")));
                contact.setLastName(cursor.getString(cursor.getColumnIndex("nameLast")));
                contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return contacts;
    }

    public ArrayList<Contact> executeQuery(SQLiteDatabase db, String sql) {
        ArrayList<Contact> contacts;
        Cursor cursor;

        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                contacts = getPersonList(cursor);
            } else {
                contacts = new ArrayList<Contact>();
            }
            if (cursor != null)
                cursor.close();
        } catch (SQLException e) {
            contacts = new ArrayList<Contact>();
            writeLog(e, this.toString(), "executeQuery", sql);
        }

        return contacts;
    }

    public String getQuerySelect() {
        String sql = "SELECT " +
                "contact.id, " +
                "contact.nameFirst, " +
                "contact.nameLast, " +
                "contact.email, " +
                "contact.phone " +
                "FROM contact ";
        return sql;
    }

    public String getQueryUpdate(Contact contact) {
        String sql = "UPDATE contact SET " +
                "nameFirst=" + sqlFormatValue(contact.getFirstName(), true) + ", " +
                "nameLast=" + sqlFormatValue(contact.getLastName(), true) + ", " +
                "email=" + sqlFormatValue(contact.getEmail(), true) + ", " +
                "phone=" + sqlFormatValue(contact.getPhone(), true);

        return sql;
    }

    public String getQueryInsert(Contact contact) {
        String sql = "INSERT INTO contact (" +
                "id, " +
                "nameFirst, " +
                "nameLast, " +
                "email, " +
                "phone) " +
                "values (" +
                contact.getId() + ", " +
                sqlFormatValue(contact.getFirstName(), true) + ", " +
                sqlFormatValue(contact.getLastName(), true) + ", " +
                sqlFormatValue(contact.getEmail(), true) + ", " +
                sqlFormatValue(contact.getPhone(), true) + ")";
        return sql;
    }

    private String sqlFormatValue(Object obj, boolean isQuoted) {
        String returnVal = "";
        if (obj == null)
            returnVal = "null";
        else {
            if (isQuoted) {
                String strValue = obj.toString();
                strValue = strValue.replace("'", "''");
                returnVal = "'" + strValue + "'";
            } else
                returnVal = obj.toString();
        }
        return returnVal;
    }

    public String getQueryDelete() {
        String sql = "DELETE FROM contact";
        return sql;
    }

    public String getQueryDrop() {
        String sql = "DROP TABLE IF EXISTS contact";
        return sql;
    }

    public String getQueryCount() {
        String sql = "SELECT * FROM contact";
        return sql;
    }

    public String getQueryCreate() {
        String sql = "";
        sql = "CREATE TABLE contact (" +
                "id" + " INTEGER PRIMARY KEY, " +
                "nameFirst" + " INTEGER, " +
                "nameLast" + " TEXT, " +
                "email" + " TEXT, " +
                "phone" + " TEXT" +
                ")";
        return sql;
    }
}
