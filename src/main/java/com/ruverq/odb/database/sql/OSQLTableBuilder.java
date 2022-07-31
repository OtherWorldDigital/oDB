package com.ruverq.odb.database.sql;

import com.ruverq.odb.database.sql.types.OSQLType;

import java.util.HashMap;

public class OSQLTableBuilder {

    public OSQLTableBuilder(String name){
        this.tableName = name;
    }

    String tableName;

    HashMap<String, OSQLType> columns = new HashMap<>();

    public void addColumn(String name, OSQLType type){

        columns.remove(name);
        columns.put(name, type);
    }


    public void addStringColumn(String name){
        addColumn(name, OSQLType.VARCHAR);
    }

    public void addStringColumn(String name, int size){
        OSQLType type = OSQLType.VARCHAR;
        type.setSize(size);

        addColumn(name, type);
    }

    public void addBoolColumn(String name){
        addColumn(name, OSQLType.BOOL);
    }

    public void addIntColumn(String name){
        addColumn(name, OSQLType.INTEGER);
    }

    public String buildRawCommand(){
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS ")
        .append(tableName)
        .append(" (");

        sb.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");

        columns.forEach((s, t) ->{
            sb.append(s)
            .append(" ")
            .append(t.getRaw())
            .append(", ");
        });

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");

        return sb.toString();
    }

}
