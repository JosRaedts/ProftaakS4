/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.models.cartproduct;

import com.photoshop.models.Database;
import com.photoshop.models.product.Product;
import com.photoshop.models.product.ProductDao;
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
 * @author pc
 */
@Component
public class CartproductDao extends Database {

    public CartproductDao() {
        super();
    }

    public List<Cartproduct> getList(int id) {
        List<Cartproduct> products = new ArrayList();
        try {
            String querystring = "SELECT * FROM cartproducts WHERE student_id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                products.add(build(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return products;
    }

    public Cartproduct getById(int id) {
        Cartproduct product = null;
        try {
            String querystring = "SELECT * FROM cartproducts WHERE id = ?";
            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                product = build(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }

    public boolean idExists(int id) {
        boolean exists = false;
        try {
            String querystring = "SELECT * FROM cartproducts WHERE id = ?";
            PreparedStatement stat;
            stat = conn.prepareStatement(querystring);
            stat.setInt(1, id);
            ResultSet rs = stat.executeQuery();

            while (rs.next()) {
                exists = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return exists;
    }

    public boolean save(Cartproduct product) {
        try {
            String querystring = null;
            boolean exists = idExists(product.getId());
            if (exists) {
                querystring = "UPDATE cartproducts SET price = ?, amount = ? WHERE id = ?";
            } else {
                querystring = "INSERT INTO cartproducts(price, amount) VALUES(?, ?)";
            }

            PreparedStatement stat = conn.prepareStatement(querystring);

            stat.setDouble(1, product.getPrice());
            stat.setInt(2, product.getAmount());

            if (exists) {
                stat.setInt(3, product.getId());
            }
            stat.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private Cartproduct build(ResultSet rs) {
        Cartproduct product = null;
        try {
            product = new Cartproduct(this);
            product.setId(rs.getInt("id"));
            //product.setContent(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setAmount(rs.getInt("amount"));

        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return product;
    }

    public boolean delete(Cartproduct product) {
        try {
            String querystring = null;
            boolean exists = idExists(product.getId());
            if (exists) {
                querystring = "DELETE FROM cartproducts WHERE id = ?";
            } else {
                return false;
            }

            PreparedStatement stat = conn.prepareStatement(querystring);
            stat.setInt(1, product.getId());
            stat.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    public boolean addToCart()
    {
        
        
        
        return false;
    }
}
