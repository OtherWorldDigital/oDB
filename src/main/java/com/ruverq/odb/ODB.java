package com.ruverq.odb;

import com.ruverq.odb.database.sql.OSQL;
import com.ruverq.odb.database.sql.OSQLRequestBuilder;
import com.ruverq.odb.database.sql.OSQLTable;
import com.ruverq.odb.database.sql.OSQLTableBuilder;
import com.ruverq.odb.database.sql.types.OSQLStatement;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class ODB extends JavaPlugin {

    @Getter
    static ODB instance;

    @Override
    public void onEnable() {
        instance = this;

        OSQL testDB = new OSQL("testdb");
        testDB.getConnection();

        OSQLTableBuilder tableBuilder = new OSQLTableBuilder("testtable");
        tableBuilder.addIntColumn("intthing");
        tableBuilder.addBoolColumn("boolthing");
        tableBuilder.addStringColumn("stringthing");
        testDB.createTable(tableBuilder);

        OSQLTable table = testDB.getTable("testtable");

        OSQLRequestBuilder requestBuilder = new OSQLRequestBuilder(table);
        requestBuilder.setStatement(OSQLStatement.INSERT_INTO);

        requestBuilder
                .addColumnToStatement("intthing")
                .addValueToStatement("2")

                .addColumnToStatement("boolthing")
                .addValueToStatement("true")

                .addColumnToStatement("stringthing")
                .addValueToStatement("cringe" + new Random().nextFloat());

        table.sendRequest(requestBuilder.build());

    }

    @Override
    public void onDisable() {

    }

}
