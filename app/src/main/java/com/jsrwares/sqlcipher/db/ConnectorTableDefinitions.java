package com.jsrwares.sqlcipher.db;

import java.util.List;

public interface ConnectorTableDefinitions {

    void addTable(String tableName, String createQuery);

    int getTableCount();

    List<String> getTables();

    List<String> getCreateQuerys();
}