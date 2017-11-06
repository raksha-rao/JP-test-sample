package com.jptest.payments.fulfillment.testonia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Singleton;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jpinc.integ.dal.DalInit;
import com.jpinc.integ.dal.DalInitException;
import com.jpinc.integ.dal.map.ConnectionManager;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;


@Singleton
public class DBHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBHelper.class);

    private final AtomicBoolean isInitialized = new AtomicBoolean();

    public void initialize() {
        try {
            DalInit.init();
            this.isInitialized.compareAndSet(false, true);
        }
        catch (final DalInitException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeIfNotAlreadyInitialized() {
        if (!this.isInitialized.get()) {
            this.initialize();
            return;
        }
    }

    /**
     * Executes SELECT query and returns first result
     *
     * @param dbName
     * @param query
     * @return first result or null if no result is found
     */
    public Map<String, Object> executeSelectQueryForSingleResult(String dbName, String query) {
        final List<Map<String, Object>> results = this.executeSelectQuery(dbName, query);
        if (!results.isEmpty()) {
            return results.get(0);
        }
        return null;
    }

    /**
     * Executes SELECT query and returns all results
     * 
     * @param dbName
     * @param selectQuery
     * @return
     */
    public List<Map<String, Object>> executeSelectQuery(String dbName, String selectQuery) {
        try {
            return this.executeSelectQueryUsingJDBC(dbName, selectQuery);
        }
        catch (final Exception e) {
            throw new TestExecutionException(e);
        }
    }

    public List<Map<String, Object>> executeSelectQueryUsingJDBC(String dbName, String query) {
        this.initializeIfNotAlreadyInitialized();
        LOGGER.info("Executing query '{}' on {} DB using JDBC", query, dbName);
        final List<Map<String, Object>> records = new ArrayList<>();
        try {
            try (Connection conn = ConnectionManager.getInstance().getConnection(dbName)) {
                try (Statement statement = conn.createStatement()) {
                    try (ResultSet rs = statement.executeQuery(query)) {
                        final ResultSetMetaData meta = rs.getMetaData();
                        final int cols = meta.getColumnCount();
                        while (rs.next()) {
                            final Map<String, Object> record = new HashMap<>();
                            for (int i = 1; i <= cols; i++) {
                                final String columnName = meta.getColumnName(i);
                                if (meta.getColumnType(i) == Types.BLOB) {
                                    record.put(columnName,
                                            rs.getBlob(i) == null ? null : rs.getBlob(i).getBinaryStream());
                                }
                                else {
                                    record.put(columnName, rs.getString(i));
                                }
                            }
                            records.add(record);
                        }
                    }
                }
            }
        }
        catch (SQLException | NamingException e) {
            throw new TestExecutionException(e);
        }
        LOGGER.info("Query '{}' executed. Row count:{}", query, records.size());
        return records;
    }

    /**
     * Executes INSERT, UPDATE or DELETE query.
     * 
     * @param dbName
     * @param updateQuery
     * @return count of rows impacted
     */
    public int executeUpdateQuery(String dbName, String updateQuery) {
        try {
            return this.executeUpdateQueryUsingJDBC(dbName, updateQuery);
        }
        catch (final Exception e) {
            throw new TestExecutionException("Failed executing the query " + updateQuery, e);
        }
    }

    /**
     * Fires DML queries using JDBC.
     * 
     * @param dbName
     * @param updateQuery
     * @return
     * @throws SQLException
     * @throws NamingException
     */
    private int executeUpdateQueryUsingJDBC(String dbName, String updateQuery) {
        this.initializeIfNotAlreadyInitialized();
        LOGGER.info("Executing update query '{}' on {} DB using JDBC", updateQuery, dbName);
        int count = -1;
        try {
            try (Connection conn = ConnectionManager.getInstance().getConnection(dbName)) {
                try (Statement statement = conn.createStatement()) {
                    count = statement.executeUpdate(updateQuery);
                }
            }
        }
        catch (SQLException | NamingException e1) {
            throw new TestExecutionException("Failed executing the query " + updateQuery, e1);
        }
        LOGGER.info("Query '{}' executed. Rows impacted:{}", updateQuery, count);
        return count;
    }

    /**
     * This method executes the update or insert queries involving blob columns. It gets the query in the string format
     * along with db name and an array of byte array blob fields. The query passed needs to be in the prepared statement
     * compliant query where it has ? for every column where blob fields needs to be added. All non-blob columns are
     * supposed to be already replaced with actual values. This method will only substitute the blob columns and execute
     * the query.
     * 
     * @param query
     * @param dbName
     * @param blobVO
     * @return
     */
    public int executeUpdateQueryForBlobData(String dbName, String query, byte[]... blobVO) {
        this.initializeIfNotAlreadyInitialized();
        LOGGER.info("Executing query '{}' on {} DB using JDBC", query, dbName);
        int count = -1;
        try (Connection conn = ConnectionManager.getInstance().getConnection(dbName)) {
            try (PreparedStatement preparedStmt = conn.prepareStatement(query)) {
                for (int i = 0; i < blobVO.length; i++) {
                    preparedStmt.setBytes((i + 1), blobVO[i]);
                }
                count = preparedStmt.executeUpdate();
            }
        }
        catch (final Exception e) {
            throw new TestExecutionException("Error executing executeUpdateQueryForBlobData for query " + query, e);
        }
        LOGGER.info("Query '{}' executed. Rows impacted:{}", query, count);
        return count;
    }

}
