package com.ruverq.odb.database.sql.types;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public enum OSQLType {
    CHAR(255),
    VARCHAR(65535),
    BINARY(1),
    VARBINARY(1),
    TINYBLOB,
    TINYTEXT,
    TEXT(2048),
    BLOB,
    MEDIUMTEXT,
    MEDIUMBLOB,
    LONGTEXT,
    LONGBLOB,
    ENUM,
    SET,

    BIT(64),
    TINYINT(255),
    BOOL,
    BOOLEAN,
    SMALLINT(255),
    MEDIUMINT(255),
    INT(255),
    INTEGER(255),
    BIGINT(255),
    FLOAT(255, 5),
    DOUBLE(255, 5),
    DOUBLE_PRECISION(255, 5),
    DECIMAL(65, 2),
    DEC(65, 2),

    DATE,
    DATETIME(""),
    TIMESTAMP(""),
    TIME(""),
    YEAR;

    @Getter
    public int sizeDefault = -1;

    @Getter
    public int size;

    @Setter
    @Getter
    public int d = -1;

    @Setter
    @Getter
    String fsp;

    OSQLType(){

    }

    OSQLType(String fsp){
        this.fsp = fsp;
    }

    OSQLType(int sizeDefault){
        this.sizeDefault = sizeDefault;
        this.size = sizeDefault;
    }

    OSQLType(int sizeDefault, int d){
        this.sizeDefault = sizeDefault;
        this.size = sizeDefault;
        this.d = d;
    }

    public void setSize(int size){
        if(size > sizeDefault) size = sizeDefault;

        this.size = size;
    }

    static public OSQLType getTypeByString(String raw){
        raw = raw.replace(" ", "");

        String typeName;
        Pattern pattern = Pattern.compile("([^,\\((.*?)\\)]+)");
        Matcher m = pattern.matcher(raw);
        m.find();

        typeName = m.group();

        OSQLType type = OSQLType.valueOf(typeName);

        //Numeric values
        if(type.sizeDefault > -1 && m.find()){
            type.size = Integer.parseInt(m.group());

            if(type.d > -1 && m.find()){
                type.d = Integer.parseInt(m.group());
            }
        }else if(type.fsp != null && m.find()){
            type.fsp = m.group();
        }

        return type;
    }

    //CRINGE
    public String getRaw(){
        if(fsp != null){
            return this.name() + "(" + fsp + ")";
        }

        if(sizeDefault > -1){

            if(d > -1)
                return this.name() + "(" + size + ", " + d + ")";

            return this.name() + "(" + size + ")";
        }

        return this.name();
    }

}

