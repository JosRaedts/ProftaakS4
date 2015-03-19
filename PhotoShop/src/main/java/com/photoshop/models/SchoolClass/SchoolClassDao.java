/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.models.schoolClass;

import com.photoshop.models.Database;
import com.photoshop.models.school.School;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Casper
 */
@Component
public class SchoolClassDao extends Database  {
    
    public SchoolClassDao()
    {
        super();
    }
    
    public List<SchoolClass> getList()
    {
        List<SchoolClass> schoolclasses = new ArrayList();
        try {
            String querystring = "SELECT * FROM schoolclasses";
            PreparedStatement stat = conn.prepareStatement(querystring);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                schoolclasses.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schoolclasses;
    }
    

    
    public List<SchoolClass> getSchoolClassesBySchool(School school)
    {
        List<SchoolClass> schoolclasses = new ArrayList();
        try {
            String querystring = "SELECT * FROM schoolclasses WHERE school_id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, school.getId());
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                schoolclasses.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schoolclasses;
    }
    
    public SchoolClass getById(int id)
    {
        SchoolClass schoolclass = null;
        try {
            String querystring = "SELECT * FROM schoolclasses WHERE id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                schoolclass = build(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schoolclass;
    }
    
    public boolean idExists(int id)
    {
        boolean exists = false;
        try {
            String querystring = "SELECT * FROM schoolclasses WHERE id = ?";
            PreparedStatement stat;
            stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
               exists = true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exists;
    }
    
    public void save(SchoolClass schoolclass)
    {
        try {
            String querystring = null;
            boolean exists = idExists(schoolclass.getId());
            if(exists)
            {
                querystring = "UPDATE schoolclasses SET name = ? WHERE id = ?";                                
            }
            else
            {
                querystring = "INSERT INTO schoolclasses(name) VALUES(?)";
            }
            
            PreparedStatement stat = this.conn.prepareStatement(querystring);
            stat.setString(1, schoolclass.getName());
            if(exists)
            {
                stat.setInt(2, schoolclass.getId());
            }
            stat.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(SchoolClass schoolclass)
    {
        try {
            String querystring = "DELETE FROM schoolclasses WHERE id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, schoolclass.getId());
            stat.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private SchoolClass build(ResultSet rs)
    {
        SchoolClass schoolclass = null;
        try {
            schoolclass = new SchoolClass(this);
            schoolclass.setId(rs.getInt("id"));
            schoolclass.setName(rs.getString("name"));
        } catch (SQLException ex) {
            Logger.getLogger(SchoolClassDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return schoolclass;
    }
}
