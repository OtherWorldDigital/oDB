package com.ruverq.odb.database.sql;

import com.ruverq.odb.database.sql.types.OSQLStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class OSQLRequest {

    OSQLRequest(String rr, OSQLStatement s){
        rawRequest = rr;
        statement = s;
    }

    @Getter
    String rawRequest;

    @Getter
    OSQLStatement statement;



}
