/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.photoshop.controllers;

import com.photoshop.models.UserType;
import com.photoshop.models.admin.Admin;
import com.photoshop.models.admin.AdminDao;
import com.photoshop.models.photographer.Photographer;
import com.photoshop.models.photographer.PhotographerDao;
import com.photoshop.models.student.Student;
import com.photoshop.models.student.StudentDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *

 * @author casper
**/

@Controller
public class DefaultController extends AbstractController {
   
    @Autowired
    private PhotographerDao photographerDao;
    
    @Autowired
    private AdminDao adminDao;
    
            
   @RequestMapping(value = "/", method = RequestMethod.GET)
   public String index(ModelMap map, HttpServletRequest request) {
       try {
            HttpSession session = request.getSession();       
            int userID = (int)session.getAttribute("UserID");
            String userName = (String)session.getAttribute("UserName");
            UserType type= (UserType)session.getAttribute("UserType");
            
            map.put("msg", "Hello " + userName + " met ID: " + userID +" van type: "+ type.toString());
            map.put("test", "testen van github account");
            if(authenticate(UserType.STUDENT))
            {
                System.out.println("Student ingelogd");
            }
            if(authenticate(UserType.PHOTOGRAPHER))
            {
                System.out.println("Photographer ingelogd");
            }
            System.out.println("test123");
            return "home";
       } catch(Exception e) {
            map.put("msg", "Hello new user, please sign in");
            map.put("test", "testen van github account");
            System.out.println("testdefault");
            return "home";
       }
   }  
   
   @RequestMapping(value = "/contact", method = RequestMethod.GET)
   public String contact(ModelMap map) {
       map.put("msg", "Hello photoshop users");
       map.put("test", "testen van github account");
       return "contact";
   }
   
   @RequestMapping(value = "/admin", method = RequestMethod.GET)
   public String admin(ModelMap map) {
        if(authenticate(UserType.ADMIN,UserType.PHOTOGRAPHER))
        {    
            return "admin/home";
        }
        return "redirect:admin/login";
       
   }
   
   @RequestMapping(value = "/admin/login", method = RequestMethod.GET)
    public String Login(ModelMap map,HttpServletRequest request) {
        map.put("Accountmade", request.getSession().getAttribute("Accountmade"));
        request.getSession().removeAttribute("Accountmade");
        return "login";
    }
    
    @RequestMapping(value="/admin/checkLogin", method = RequestMethod.POST)
    public String checkLogin(@RequestParam("name") String name,
            @RequestParam("password") String password, ModelMap map, HttpServletRequest request) {
        Admin admin = adminDao.authenticate(name, password);
        Photographer photographer = photographerDao.authenticate(name, password);
        if(admin != null){
            request.getSession().setAttribute("UserID", admin.getId());
            request.getSession().setAttribute("UserName", admin.getUsername());
            request.getSession().setAttribute("UserType", UserType.ADMIN);
            return "redirect:../../admin";
        } else if (photographer != null) {
            request.getSession().setAttribute("UserID", photographer.getId());
            request.getSession().setAttribute("UserName", photographer.getUsername());
            request.getSession().setAttribute("UserType", UserType.PHOTOGRAPHER);
            return "redirect:../../admin"; 
        } else{
            request.getSession().setAttribute("UserID", null);
            request.getSession().setAttribute("UserName", "");
            request.getSession().setAttribute("UserType", "");
            return "redirect:../login";
        }
    }
   
   @RequestMapping(value = "/logout", method = RequestMethod.GET)
   public String logout(ModelMap map, HttpServletRequest request) {
        request.getSession().setAttribute("UserID", null);
        request.getSession().setAttribute("UserName", "");
        request.getSession().setAttribute("UserType", "");
        return "redirect:";      
   }
   
   @Autowired
   private StudentDao studentdao;
   @Autowired
   private PhotographerDao photographerdao;
   @Autowired
   private AdminDao admindoa;
   
   @RequestMapping(value = "/test", method = RequestMethod.GET)
   public String test(ModelMap map, HttpServletRequest request) {
       BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for(Student s : studentdao.getList())
        {
            s.setPassword(passwordEncoder.encode(s.getPassword()));
            s.save();
        }
        for(Photographer s : photographerDao.getList())
        {
            s.setPassword(passwordEncoder.encode(s.getPassword()));
            s.save();
        }
        for(Admin s : adminDao.getList())
        {
            s.setPassword(passwordEncoder.encode(s.getPassword()));
            s.save();
        }
        return "home";      
   }
}
