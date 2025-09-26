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
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                JsonArray jsonResult = new JsonArray();
                
                while (rs.next()) {
 
                    JsonObject obj = new JsonObject();
                    obj.put("termid", rs.getString("termid"));
                    obj.put("scheduletypeid", rs.getString("scheduletypeid"));
                    obj.put("instructor", rs.getString("instructor"));
                    obj.put("num", rs.getString("num"));
                    obj.put("start", rs.getString("start"));
                    obj.put("days", rs.getString("days"));
                    obj.put("section", rs.getString("section"));
                    obj.put("end", rs.getString("end"));
                    obj.put("where", rs.getString("where"));
                    obj.put("crn", rs.getString("crn"));
                    obj.put("subjectid", rs.getString("subjectid"));
                    
                    jsonResult.add(obj);
                } /*I really tried to iterate through each column using the metadata and build each jsonobject that way 
                    but it just never matched right for the tests unless I brute forced it like this, 
                    if there's a way to get it to work with less lines using iteration that passes the tests properly 
                    I would really love to know. The same goes for the list method in RegistrationDAAO */
                
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
    
    

}