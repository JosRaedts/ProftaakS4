/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.models.order;

import com.mysql.jdbc.Statement;
import com.photoshop.models.Database;
import com.photoshop.models.student.StudentDao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Willem
 */
@Component
public class OrderDao extends Database{
    
    public OrderDao()
    {
        super();
    }
    
    @Autowired
    StudentDao dao;
    
    public List<Order> getList()
    {
        List<Order> orders = new ArrayList();
        try {
            String querystring = "SELECT * FROM orders";
            PreparedStatement stat = conn.prepareStatement(querystring);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                orders.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders;
    }
    
    public List<Order> getOrderlistByStudentId(int studentid)
    {
        List<Order> orders = new ArrayList();
        try {
            String querystring = "SELECT * FROM orders where student_id = ? ";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1,studentid);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                orders.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders;
    }
        
    public Order getById(int id)
    {
        Order order = null;
        try {
            String querystring = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                order = build(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return order;
    }
    
    public boolean idExists(int id)
    {
        boolean exists = false;
        try {
            String querystring = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement stat;
            stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
               exists = true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exists;
    }
    
    public boolean save(Order order)
    {
        try {
            PreparedStatement stat;
            String querystring = null;
            boolean exists = idExists(order.getId());
            if(exists)
            {
                querystring = "UPDATE orders SET student_id = ?, datum = ?, status = ?,factuurUrl = ?,indexkaartUrl = ?";
                stat = conn.prepareStatement(querystring);
            }
            else
            {
                querystring = "INSERT INTO orders(student_id, datum, status,factuurUrl,indexkaartUrl) VALUES(?, ?, ?, ?, ?)";
                stat = conn.prepareStatement(querystring, Statement.RETURN_GENERATED_KEYS);
            }

            stat.setInt(1, order.getStudent().getId());
            stat.setTimestamp(2, order.getDatum());
            stat.setInt(3, Integer.parseInt(order.getStatus().toString()));
            stat.setString(4, order.getFactuur());
            stat.setString(5, order.getIndexkaart());
            
            stat.execute();
            if(!exists)
            {
                ResultSet rs = stat.getGeneratedKeys();
                rs.next();
                order.setId(rs.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    private Order build(ResultSet rs)
    {
        Order order = null;
        try {            
            order = new Order();
            order.setId(rs.getInt("id"));
            order.setStudent(this.dao.getById(rs.getInt("student_id")));
            order.setDatum(rs.getTimestamp(3));
            order.setFactuur(rs.getString("factuurUrl"));
            order.setIndexkaart(rs.getString("indexkaartUrl"));
            
            switch (rs.getInt("status")) {
            case 1:  order.setStatus(OrderEnum.NIET_BETAALD);
                     break;
            case 2:  order.setStatus(OrderEnum.BETAALD);
                     break;
            case 3:  order.setStatus(OrderEnum.IN_BEHANDELING);
                     break;
            case 4:  order.setStatus(OrderEnum.VERZONDEN);
                     break;
            case 5:  order.setStatus(OrderEnum.ONTVANGEN);
                     break;
            default: order.setStatus(OrderEnum.NIET_BETAALD);
                     break;

        }

        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return order;
    }
    
    public List<Order> getOrderlistByPhotographerId(int studentid)
    {
        List<Order> orders = new ArrayList();
        try {
            String querystring = "SELECT * FROM orders where photographer_id = ? ";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1,studentid);
            ResultSet rs = stat.executeQuery();
            
            while(rs.next())
            {
                orders.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orders;
    }
}
