/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotel.management.system;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author PC
 */
public class database {
    
    public static Connection connectDb(){
        
        try{
            
        Class.forName("com.mysql.jdbc.Driver");
        
        // root is the default username of database and "" or null is the default password 
        
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/hoteldata", "root","");
        
        return connect; 
        }catch(Exception e){e.printStackTrace();}
    return null;
    
    } //hoteldata our database name
}
