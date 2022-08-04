package com.ruverq.odb.database.sql;

import com.ruverq.odb.database.sql.types.OSQLType;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        try(Connection connection = osql.getConnection()){
            Statement statement = connection.createStatement();

            if(request.getStatement().isQuery()) {
                ResultSet set = statement.executeQuery(request.getRawRequest());

                ResultSetMetaData metadata = set.getMetaData();
                int numberOfCols = metadata.getColumnCount();

                List<HashMap<String, Object>> list = new ArrayList<>();
                while(set.next()){
                    HashMap<String, Object> map = new HashMap<>();
                    for(int i = 0; i < numberOfCols; i++){
                        Object obj = set.getObject(i + 1);

                        map.put(metadata.getColumnLabel(i + 1), obj);
                    }

                    list.add(map);
                }

                OSQLResult result = new OSQLResult(list);

                return result;

            }

            statement.execute(request.getRawRequest());
            return null;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


}
