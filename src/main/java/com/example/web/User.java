package com.example.web;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
@ManagedBean
@RequestScoped
public class User{
    int id;
    String name;
    String lastname;
    String email;
    String skills;
    ArrayList usersList ;
    private Map<String,Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    Connection connection;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSkills() {
        return skills;
    }
    public void setSkills(String skills) {
        this.skills = skills;
    }
    // Used to establish connection
    public Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");   
            connection = DriverManager.getConnection( "jdbc:mysql://localhost:3306/first?verifyServerCertificate=false&useSSL=true","test","test");
        }catch(Exception e){
            System.out.println(e);
        }
        return connection;
    }
    // Used to fetch all records
    public ArrayList usersList(){
        try{
            usersList = new ArrayList();
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from users");  
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setEmail(rs.getString("email"));
                user.setSkills(rs.getString("skills"));
                usersList.add(user);
            }
            connection.close();        
        }catch(Exception e){
            System.out.println(e);
        }
        return usersList;
    }
    // Used to save user record
    public String save(){
        int result = 0;
        try{
            connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement("insert into users(name,lastname,email) values(?,?,?)");
            stmt.setString(1, name);
            stmt.setString(2, lastname);
            stmt.setString(3, email);
            
            result = stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }
        if(result !=0)
            return "index.xhtml?faces-redirect=true";
        else return "create.xhtml?faces-redirect=true";
    }
    // Used to fetch record to update
    public String edit(int id){
        User user = null;
        System.out.println(id);
        try{
            connection = getConnection();
            Statement stmt=getConnection().createStatement();  
            ResultSet rs=stmt.executeQuery("select * from users where id = "+(id));
            rs.next();
            user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setLastname(rs.getString("lastname"));
            user.setEmail(rs.getString("email"));
            user.setSkills(rs.getString("skills"));
            sessionMap.put("editUser", user);
            connection.close();
        }catch(Exception e){
            System.out.println(e);
        }       
        return "/edit.xhtml?faces-redirect=true";
        
    }
    // Used to update user record
    public String update(User u){
        int result = 0;
        try{
            connection = getConnection();  
            PreparedStatement stmt=connection.prepareStatement("UPDATE users SET skills = CONCAT_WS(', ', skills, ?) WHERE id = ?");           
            stmt.setString(1,u.getSkills());   
            stmt.setInt(2,u.getId());  
            result=stmt.executeUpdate();
            connection.close();
        }catch(Exception e){
            System.out.println();
        }
        return "/index.xhtml?faces-redirect=true";  
    }
    // Used to delete user record
    public void delete(int id){
        try{
            connection = getConnection();  
            PreparedStatement stmt = connection.prepareStatement("delete from users where id = "+id);  
            stmt.executeUpdate();  
        }catch(Exception e){
            System.out.println(e);
        }
    }
}