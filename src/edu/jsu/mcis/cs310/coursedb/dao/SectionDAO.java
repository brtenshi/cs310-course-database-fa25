package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import com.github.cliftonlabs.json_simple.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;

public class SectionDAO {
    
    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid = ? AND subjectid = ? AND num = ? ORDER BY crn";
    
    private final DAOFactory daoFactory;
    
    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public String find(int termid, String subjectid, String num) {
        
        String result = "[]";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(QUERY_FIND);
                rs = ps.executeQuery(QUERY_FIND);
                rsmd = rs.getMetaData();
                JsonArray jsonResult = new JsonArray();
                
                while (rs.next()) {
                    String currentSubId = rs.getString("subjectid");
                    String currentNum = rs.getString("num");
                    if ((subjectid.equals(currentSubId)) && (num.equals(currentNum))){
                        int numColumns = rsmd.getColumnCount();
                        JsonObject obj = new JsonObject();
                        for (int i=1; i<=numColumns; i++) {
                            String column_name = rsmd.getColumnName(i);
                            obj.put(column_name, rs.getObject(column_name));
                        }
                        jsonResult.add(obj);
                    }
                }
                jsonResult = sortResult(jsonResult); //method implemented below
                result = Jsoner.serialize(jsonResult);
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    
    //Implementation for sorting based on https://stackoverflow.com/a/12903029 and https://stackoverflow.com/a/19546513
    //I'm later realizing this is redundant bc the query_find orders it by crn but I don't want to dedlete all of this
    public JsonArray sortResult(JsonArray jsonArr) {
        JsonArray sortedJsonArray = new JsonArray();
        List jsonValues = new ArrayList<JsonObject>();
        for (int i = 0; i < jsonArr.size(); i++) {
            jsonValues.add(jsonArr.get(i));
        }
        Collections.sort(jsonValues, new Comparator<JsonObject>() {

            @Override
            public int compare(JsonObject a, JsonObject b) {
                String valA = (String) a.get("num");
                String valB = (String) b.get("num");

                return valA.compareTo(valB);

            }
        });

        for (int i = 0; i < jsonArr.size(); i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
}