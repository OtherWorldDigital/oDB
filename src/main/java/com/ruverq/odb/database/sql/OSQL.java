package com.ruverq.odb.database.sql;

import com.ruverq.odb.ODatabase;
import com.ruverq.odb.ODB;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;
import java.util.HashMap;

public class OSQL extends ODatabase {

    String name;
    String path;

    Plugin plugin;

    HikariDataSource ds;

    HashMap<String, OSQLTable> tables = new HashMap<>();

    OSQL(String name, String path, Plugin plugin){
        this.name = name;
        this.path = path;
        this.plugin = plugin;

        createConnection();
    }

    public OSQL(String name, Plugin plugin){
        this.name = name;
        this.plugin = plugin;


        createConnection();
    }

    @SneakyThrows
    public Connection getConnection(){
        return ds.getConnection();
    }

    @SneakyThrows
    public void createConnection(){

        //Creating database file
        File dataFolder = ODB.getInstance().getDataFolder();
        File dbFolder = new File(dataFolder.getAbsolutePath() + File.separator + "databases");
        dbFolder.mkdirs();

        File db = new File(dbFolder.getAbsolutePath() + File.separator + name + ".db");
        if(!db.exists()) db.createNewFile();

        //Connecting to that database File
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + db.getAbsolutePath());
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);

        loadTables();
    }

    private void loadTables(){
        tables.clear();

        String commandTables = "SELECT name FROM sqlite_schema WHERE type ='table' AND name NOT LIKE 'sqlite_%';";

        try(Connection connection = getConnection()){
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(commandTables);

            while(set.next()){
                String tableName = set.getString("name");
                OSQLTable table = new OSQLTable(tableName, this, true);
                tables.put(tableName, table);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public OSQLTable getTable(String name){
        return tables.get(name);
    }

    public void createTable(OSQLTableBuilder builder){
        String command = builder.buildRawCommand();

        try(final Connection connection = getConnection()) {
            PreparedStatement ps  = connection.prepareStatement(command);
            ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }

        tables.put(builder.tableName, new OSQLTable(this, builder));
    }



}
