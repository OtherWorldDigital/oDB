package com.ruverq.odb.database.sql;

import lombok.Getter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OSQLResult{

    @Getter
    List<HashMap<String, Object>> resultList;

    int count = -1;

    public OSQLResult(List<HashMap<String, Object>> resultList){
        this.resultList = resultList;
    }

    public Object getObject(int columnNumber){
        if (count < 0) count = 0;
        HashMap<String, Object> map = resultList.get(count);
        if(map == null) return null;
        Object obj = map.values().toArray()[columnNumber];

        return obj;
    }

    public Object getObject(String columnLabel){
        if (count < 0) count = 0;
        HashMap<String, Object> map = resultList.get(count);
        if(map == null) return null;
        Object obj = map.get(columnLabel);

        return obj;
    }

    public boolean getBoolean(int columnNumber){
        return (boolean) getObject(columnNumber);
    }

    public boolean getBoolean(String columnLabel){
        return (boolean) getObject(columnLabel);
    }

    public float getFloat(int columnNumber){
        return (float) getObject(columnNumber);
    }

    public float getFloat(String columnLabel){
        return (float) getObject(columnLabel);
    }

    public double getDouble(int columnNumber){
        return (double) getObject(columnNumber);
    }

    public double getDouble(String columnLabel){
        return (double) getObject(columnLabel);
    }


    public int getInt(int columnNumber){
        return (int) getObject(columnNumber);
    }

    public int getInt(String columnLabel){
        return (int) getObject(columnLabel);
    }

    public String getString(int columnNumber){
        return (String) getObject(columnNumber);
    }
    public String getString(String columnLabel){
        return (String) getObject(columnLabel);
    }

    public boolean next(){
        count++;
        return resultList.size() >= count;
    }


}
