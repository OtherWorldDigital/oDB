package com.ruverq.odb.database.sql;

import com.ruverq.odb.database.sql.types.OSQLStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class OSQLRequest {

    @Getter
    String rawRequest;

    @Getter
    OSQLStatement statement;



}
