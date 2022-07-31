package com.ruverq.odb.database.sql.types;

import com.ruverq.odb.database.sql.OSQLTable;
import lombok.Getter;

public enum OSQLStatement {

    INSERT_INTO("INSERT INTO %table_name% (%columns%) VALUES (%values%)"),
    DELETE("DELETE FROM %table_name%"),
    SELECT("SELECT %columns% FROM %table_name%", true),
    UPDATE("UPDATE %table_name% SET %column_value%");

    @Getter
    final String template;

    @Getter
    boolean query = false;

    OSQLStatement(String template){
        this.template = template;
    }

    OSQLStatement(String template, boolean query){
        this.template = template;
        this.query = query;
    }

}
