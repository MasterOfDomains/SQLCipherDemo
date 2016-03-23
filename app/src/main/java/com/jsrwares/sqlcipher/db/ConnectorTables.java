package com.jsrwares.sqlcipher.db;

import java.util.ArrayList;
import java.util.List;

public class ConnectorTables implements ConnectorTableDefinitions {

    private int tableCount = 0;
    private List<String> tables = new ArrayList<String>();
    private List<String> createQuerys = new ArrayList<String>();

    @Override
    public void addTable(String tableName, String createQuery) {
        tables.add(tableName);
        createQuerys.add(createQuery);
        tableCount++;
    }

    @Override
    public int getTableCount() {
        return tableCount;
    }

    @Override
    public List<String> getTables() {
        return tables;
    }

    @Override
    public List<String> getCreateQuerys() {
        return createQuerys;
    }
}
