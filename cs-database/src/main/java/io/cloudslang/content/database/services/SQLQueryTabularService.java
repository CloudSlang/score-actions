/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.services;

import io.cloudslang.content.database.utils.Format;
import io.cloudslang.content.database.utils.SQLInputs;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static io.cloudslang.content.database.utils.SQLUtils.getResultSetValue;

/**
 * Created by victor on 13.01.2017.
 */
public class SQLQueryTabularService {

    /**
     * Run a SQL query with given configuration
     *
     * @return the formatted result set by colDelimiter and rowDelimiter
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public String execSqlQueryTabular(SQLInputs sqlInputs) throws Exception {

        if (StringUtils.isEmpty(sqlInputs.getSqlCommand())) {
            throw new Exception("command input is empty.");
        }
        Connection connection = null;
        ConnectionService connectionService = new ConnectionService();
        try {
            connection = connectionService.setUpConnection(sqlInputs);
            connection.setReadOnly(true);

            Statement statement = connection.createStatement(getResultSetValue(sqlInputs.getResultSetType()), getResultSetValue(sqlInputs.getResultSetConcurrency()));

            statement.setQueryTimeout(sqlInputs.getTimeout());
            final ResultSet resultSet = statement.executeQuery(sqlInputs.getSqlCommand());

            final String resultSetToTable = Format.resultSetToTable(resultSet, sqlInputs.isNetcool());
            if (resultSet != null) {
                resultSet.close();
            }
            return resultSetToTable;
        } finally {
            connectionService.closeConnection(connection);
        }
    }
}
