/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.controllers;

import com.photoshop.models.UserType;
import com.photoshop.models.photographer.Photographer;
import com.photoshop.models.photographer.PhotographerDao;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Bram
 */
@RequestMapping("/photographer")
@Controller
public class PhotographerController {
    
    @Autowired
    private PhotographerDao photographerDao;
    
   //Toevoegen fotograaf
   @RequestMapping(value = "/add", method = RequestMethod.GET)
   public String add(ModelMap map) {
       return "photographer/add";
   }
   
       
    @RequestMapping(value="/add/addphotographer", method = RequestMethod.POST)
    public String Addphotographer(@RequestParam("name") String name,
            @RequestParam("password") String password, ModelMap map, HttpServletRequest request) {
        if (name.equals("") && password.equals("")) {
            System.out.println("mislukt");
            return "photographer/add";
        } else {
            System.out.println("gelukt");
            Photographer Photographer = new Photographer();
            Photographer.setName(name);
            Photographer.setPassword(password);
            Photographer.setUsername(name);
            if(photographerDao.save(Photographer))
            {
                request.getSession().setAttribute("Accountmade", "Accountmade");
            }
            
            return "photographer/login";
        }
    }
    
    @RequestMapping(value = "/accountgegevens", method = RequestMethod.GET)
   public String VraagAccountInfoOp(ModelMap map, HttpServletRequest request) {
       int userID = 0;
       UserType userType;
       String userName = "";
       Photographer photographer = null;
       
       try {
           userType = (UserType)request.getSession().getAttribute("UserType");
           userID = (int)request.getSession().getAttribute("UserID");
           switch(userType){
                case PHOTOGRAPHER: photographer = photographerDao.getById(userID);
                    System.out.println("User ID = " + userID);
                    map.put("Type", "photographer");
                    map.put("UserName", photographer.getUsername());
                    map.put("Name", photographer.getName());
                    return "photographer/accountgegevens";
                default: System.out.println("invalid type");
                     break;

           }
       } catch (Exception e) {
           System.out.println(e.getMessage());
           return "home";
       }
       return "home";
    }
}
