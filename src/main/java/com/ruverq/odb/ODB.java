package com.ruverq.odb;

import com.ruverq.odb.database.sql.*;
import com.ruverq.odb.database.sql.types.OSQLStatement;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.util.Random;

public final class ODB extends JavaPlugin {

    @Getter
    static ODB instance;

    @Override
    @SneakyThrows
    public void onEnable() {
        instance = this;

        //Creating database
        OSQL testDB = new OSQL("testdb");

        //Creating table in database
        OSQLTableBuilder tableBuilder = new OSQLTableBuilder("testtable1");
        tableBuilder.addIntColumn("intthing");
        tableBuilder.addBoolColumn("boolthing");
        tableBuilder.addStringColumn("stringthing");
        tableBuilder.addStringColumn("stringthing2");
        testDB.createTable(tableBuilder);

        OSQLTable table = testDB.getTable("testtable1");

        //Creating a INSERT request to the table in database
        OSQLRequestBuilder requestBuilder = new OSQLRequestBuilder(table);
        requestBuilder.setStatement(OSQLStatement.INSERT_INTO);

        float a = new Random().nextFloat();

        requestBuilder
                .addColumnToStatement("intthing")
                .addValueToStatement("2")

                .addColumnToStatement("boolthing")
                .addValueToStatement("true")

                .addColumnToStatement("stringthing")
                .addValueToStatement("cringe" + a)

                .addColumnToStatement("stringthing2")
                .addValueToStatement(a + "bruh" + new Random().nextFloat());

        table.sendRequest(requestBuilder.build());

        //Creating a SELECT request to the table to the database
        OSQLRequestBuilder requestBuilder1 = new OSQLRequestBuilder(table);
        requestBuilder1.setStatement(OSQLStatement.SELECT);

        requestBuilder1.addWhere("stringthing", "cringe" + a);
        requestBuilder1.addWhere("boolthing", "true");

        OSQLResult result = table.sendRequest(requestBuilder1.build());
        String bruh = result.getString("stringthing2");
        System.out.println(bruh);
    }

    @Override
    public void onDisable() {

    }

}
