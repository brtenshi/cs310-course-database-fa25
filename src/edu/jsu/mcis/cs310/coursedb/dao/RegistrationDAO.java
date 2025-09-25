package edu.jsu.mcis.cs310.coursedb.dao;

import com.github.cliftonlabs.json_simple.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class RegistrationDAO {
    
    private final DAOFactory daoFactory;
    
    RegistrationDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
    
    public boolean create(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                String query = "INSERT INTO registration (studentid, termid, crn) VALUES ('" + studentid + "', '" + termid + "', '" + crn + "')";
                System.out.println("**INSERT QUERY TEST**:" + query);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery(query);
                result = true;
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }

    public boolean delete(int studentid, int termid, int crn) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                String query = "DELETE FROM registration WHERE studentid='" + studentid + "' AND termid='" + termid + "' AND crn='" + crn + "'";
                System.out.println("**DELETE QUERY TEST**:" + query);
                ps = conn.prepareStatement(query);
                result = true;
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }
    
    public boolean delete(int studentid, int termid) {
        
        boolean result = false;
        
        PreparedStatement ps = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                String query = "DELETE FROM registration WHERE studentid='" + studentid + "' AND termid='" + termid + "'";
                System.out.println("**OVERLOAD DELETE QUERY TEST**:" + query);
                ps = conn.prepareStatement(query);
                result = true;
                
            }
            
        }
        
        catch (Exception e) { e.printStackTrace(); }
        
        finally {

            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
        
    }

    public String list(int studentid, int termid) {
        
        String result = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                String query = "";
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                rsmd = rs.getMetaData();
                JsonArray jsonResult = new JsonArray();
                
                while (rs.next()) {
                    int  currentStudID = Integer.parseInt(rs.getString("studentid"));
                    int currentTermID = Integer.parseInt(rs.getString("termid"));
                    if ((studentid == currentStudID) && (termid == currentTermID)){
                        int numColumns = rsmd.getColumnCount();
                        JsonObject obj = new JsonObject();
                        for (int i=1; i<=numColumns; i++) {
                            String column_name = rsmd.getColumnName(i);
                            obj.put(column_name, rs.getObject(column_name));
                        }
                        jsonResult.add(obj);
                    }
                }
                
                
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
