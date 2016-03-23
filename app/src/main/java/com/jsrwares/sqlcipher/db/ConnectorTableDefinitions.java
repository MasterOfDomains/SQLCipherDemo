package com.jsrwares.sqlcipher.db;

import java.util.ArrayList;
import java.util.List;

public class ConnectorTableDefinitions {

    private int tableCount = 0;
    private List<String> tables = new ArrayList<String>();
    private List<String> createQuerys = new ArrayList<String>();

    public void addTable(String tableName, String createQuery) {
        tables.add(tableName);
        createQuerys.add(createQuery);
        tableCount++;
    }

    public int getTableCount() {
        return tableCount;
    }

    public List<String> getTables() {
        return tables;
    }

    public List<String> getCreateQuerys() {
        return createQuerys;
    }
}
