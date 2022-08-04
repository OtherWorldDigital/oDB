package com.ruverq.odb.database.sql;

import com.ruverq.odb.ODB;
import com.ruverq.odb.database.sql.types.OSQLStatement;
import com.ruverq.odb.database.sql.types.OSQLType;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OSQLRequestBuilder{

    final static List<String> numericTypes = List.of(
            OSQLType.BIT.name(),
            OSQLType.TINYINT.name(),
            OSQLType.BOOL.name(),
            OSQLType.BOOLEAN.name(),
            OSQLType.SMALLINT.name(),
            OSQLType.MEDIUMINT.name(),
            OSQLType.INT.name(),
            OSQLType.INTEGER.name(),
            OSQLType.BIGINT.name(),
            OSQLType.FLOAT.name(),
            OSQLType.DOUBLE.name(),
            OSQLType.DOUBLE_PRECISION.name(),
            OSQLType.DECIMAL.name(),
            OSQLType.DEC.name());

    OSQLTable table;

    public OSQLRequestBuilder(OSQLTable table){
        this.table = table;
    }

    StringBuilder whereStringBuilder = new StringBuilder();

    OSQLStatement statement;
    public OSQLRequestBuilder setStatement(OSQLStatement statement){
        this.statement = statement;
        return this;
    }

    List<String> valuesStatement = new ArrayList<>();
    List<String> columnsStatement = new ArrayList<>();
    public OSQLRequestBuilder addValueToStatement(String value){
        valuesStatement.add(value);
        return this;
    }

    @SneakyThrows
    public OSQLRequestBuilder addColumnToStatement(String column){
        HashMap<String, OSQLType> columns = table.getColumns();
        OSQLType type = columns.get(column);
        if(type == null){
            ODB.getInstance().getLogger().warning("Column " + column + " does not exists");
            throw new SQLException("Column " + column + " does not exists");
        }

        columnsStatement.add(column);

        return this;
    }

    // For building
    private String getColumnsStatementListFormatted(){
        if(columnsStatement.isEmpty()){
            return "* ";
        }

        return getListFormatted(columnsStatement);
    }
    private String getValuesStatementListFormatted(){
        if(valuesStatement.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        HashMap<String, OSQLType> columns = table.getColumns();

        int i = 0;
        for(String column : columnsStatement) {
            if (i >= valuesStatement.size()) {
                ODB.getInstance().getLogger().warning("Not enough values for building SQLRequest");
                return null;
            }

            String value = valuesStatement.get(i);
            OSQLType type = columns.get(column);
            if (numericTypes.contains(type.name())) {
                sb.append(value);
            } else {
                sb.append("'").append(value).append("'");
            }

            sb.append(", ");

            i++;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
    private String getListFormatted(List<String> list){
        StringBuilder sb = new StringBuilder();

        for(String a : list){
            sb.append(a).append(",").append(" ");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }


    private String getColumnEqualsValuesListFormatted(){
        if(columnsStatement.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        HashMap<String, OSQLType> columns = table.getColumns();

        int i = 0;
        for(String column : columnsStatement){
            if(i >= valuesStatement.size()){
                ODB.getInstance().getLogger().warning("Not enough values for building SQLRequest");
                return null;
            }

            sb.append(column).append("=");

            String value = valuesStatement.get(i);
            OSQLType type = columns.get(column);
            if(numericTypes.contains(type.name())){
                sb.append(value);
            }else{
                sb.append("'").append(value).append("'");
            }

            sb.append(", ");

            i++;
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
    //



    public OSQLRequestBuilder addWhere(String column, String result){
        return addWhere(column, result, '=', false, "AND");
    }
    @SneakyThrows
    public OSQLRequestBuilder addWhere(String column, String result, char relOperator, boolean isNot, String operator){
        HashMap<String, OSQLType> columns = table.getColumns();
        OSQLType type = columns.get(column);
        if(type == null){
            ODB.getInstance().getLogger().warning("Column " + column + " does not exists");
            throw new SQLException("Column " + column + " does not exists");
        }

        if(!numericTypes.contains(type.name())){
            result = "'" + result + "'";
        }

        if(whereStringBuilder.isEmpty()){
            whereStringBuilder.append("WHERE ");
        }else{
            whereStringBuilder.append(operator).append(" ");
        }

        if(isNot) whereStringBuilder.append("NOT ");

        whereStringBuilder.append(column).append(relOperator).append(result);
        return this;
    }



    public OSQLRequest build(){
        StringBuilder sb = new StringBuilder();

        String statementTemplate = statement.getTemplate();
        statementTemplate = statementTemplate
                .replace("%table_name%", table.name)
                .replace("%columns%", getColumnsStatementListFormatted())
                .replace("%values%", getValuesStatementListFormatted())
                .replace("%column_value%", getColumnEqualsValuesListFormatted());

        sb.append(statementTemplate);
        sb.append(" ").append(whereStringBuilder.toString());

        return new OSQLRequest(sb.toString(), statement);
    }
}
