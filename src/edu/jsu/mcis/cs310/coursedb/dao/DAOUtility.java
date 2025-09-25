package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.*;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;

public class DAOUtility {
    
    public static final int TERMID_FA25 = 1;
    
    public static String getResultSetAsJson(ResultSet rs) {
        
        JsonArray records = new JsonArray();
        
        try {
        
            if (rs != null) {
                
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {

                    int numColumns = rsmd.getColumnCount();
                    JsonObject obj = new JsonObject();
                    for (int i=1; i<=numColumns; i++) {
                        String column_name = rsmd.getColumnName(i);
                        obj.put(column_name, rs.getObject(column_name));
                    }
                    records.add(obj);
                }
                

            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return Jsoner.serialize(records);
        
    }
    
}
