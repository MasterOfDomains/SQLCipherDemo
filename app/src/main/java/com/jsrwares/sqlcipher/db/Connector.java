package com.jsrwares.sqlcipher.db;

import android.content.Context;

import android.util.Log;

import java.io.File;
import java.util.List;

import com.jsrwares.sqlcipher.R;

import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

class Connector extends SQLiteOpenHelper {

    SQLiteDatabase mDb;
    private Context mContext;
    private ConnectorTableDefinitions mTableDefs;
    private static Connector mConnector = null;

    private Connector(Context context, ConnectorTableDefinitions tableDefs) {
        super(context, context.getString(R.string.database_name), null,
                Integer.parseInt(context.getString(R.string.database_version)));
        mContext = context;
        this.mTableDefs = tableDefs;
    }

    public static Connector getInstance(Context context, ConnectorTableDefinitions tableDefs) {
        if (mConnector == null) {
            mConnector = new Connector(context.getApplicationContext(), tableDefs);
        }
        return mConnector;
    }

    @Override
    public synchronized SQLiteDatabase getWritableDatabase(String password) {
        SQLiteDatabase.loadLibs(mContext);
        File databaseFile = mContext.getDatabasePath(mContext.getString(R.string.database_password));
        databaseFile.mkdirs();
        databaseFile.delete();
        mDb = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
        return super.getWritableDatabase(password);
    }

    @Override
    public synchronized void close() {
        super.close();
        mDb.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";
        List<String> createQuerys;
        createQuerys = mTableDefs.getCreateQuerys();

        for (String createQuery : createQuerys) {
            executeUpdate(db, createQuery);
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        // Drop older table if existed
        List<String> tables = mTableDefs.getTables();

        for (String table : tables) {
            sql = getQueryDrop(table);
            executeUpdate(db, sql);
        }

        // Create tables again
        onCreate(db);
    }

    public String getQueryDrop(String table) {
        String sql = "DROP TABLE IF EXISTS " + table;
        return sql;
    }

    //this method exists to have only one try-catch for create, delete, update, insert queries
    public void executeUpdate(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
        } catch (SQLException e) {
            Log.d("SQLException query =" + sql + " " + e.getStackTrace().toString(), "");
        }
    }
}

