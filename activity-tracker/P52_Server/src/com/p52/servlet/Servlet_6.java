package com.p52.servlet;

import com.p52.model.User;
import com.sun.deploy.net.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet_6 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("action") == null){
            throw new IOException("Aucune action n'est spécifiée.");
        }
        if(request.getParameter("action").equals("add")){
            addCookie(response);
        }
        else if(request.getParameter("action").equals("delete")){
            deleteCookie(response);
        }
        else {
            throw new IOException("Action invalide.");
        }
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() +"/?q=6"));
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = getCookie(request,"username");
        if(cookie != null){
            request.setAttribute("username",cookie.getValue());
        }
        this.getServletContext().getRequestDispatcher("/WEB-INF/question6/question6.jsp").forward(request,response);
    }
    private void addCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("username","cedric");
        cookie.setMaxAge(2 * 3600 * 24 * 30); // 2 mois
        response.addCookie(cookie);
    }
    private void deleteCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("username",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    private  Cookie getCookie(HttpServletRequest request,String key){
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals(key)){
                return cookie;
            }
        }
        return null;
    }
}
