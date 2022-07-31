package com.ruverq.odb.database.sql;

import com.ruverq.odb.database.sql.types.OSQLType;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class OSQLTable {

    String name;
    OSQL osql;

    @Getter
    HashMap<String, OSQLType> columns = new HashMap<>();

    public OSQLTable(OSQL osql, OSQLTableBuilder builder){
        this.osql = osql;
        this.name = builder.tableName;

        this.columns = builder.columns;
    }

    public OSQLTable(String name, OSQL osql, boolean loadColumns){
        this.name = name;
        this.osql = osql;

        if(loadColumns) loadColumns();
    }

    private OSQLType getTypeOfColumn(String columnName){
        return columns.get(columnName);
    }

    private void loadColumns(){

        try(Connection connection = osql.getConnection()){
            Statement statement = connection.createStatement();
            String command = "PRAGMA table_info(" + name + ");";
            ResultSet set = statement.executeQuery(command);

            while(set.next()){
                String columnName = set.getString("name");
                String typeName = set.getString("type");
                OSQLType type = OSQLType.getTypeByString(typeName);
                columns.put(columnName, type);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public OSQL getOSQL() {
        return osql;
    }

    //TODO
    public OSQLResult sendRequest(OSQLRequest request){

        System.out.println("Executing " + request.getRawRequest());

        try(Connection connection = osql.getConnection()){
            Statement statement = connection.createStatement();

            if(request.getStatement().isQuery()) return (OSQLResult) statement.executeQuery(request.getRawRequest());
            statement.execute(request.getRawRequest());
            return null;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
