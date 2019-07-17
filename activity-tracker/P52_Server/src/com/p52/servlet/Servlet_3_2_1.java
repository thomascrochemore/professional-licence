package com.p52.servlet;

import com.p52.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet_3_2_1 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User expected = new User("Otruch","Robin","Couty");
        expected.setPassword("jeSuisUnCanard");

        try{
            if(!request.getParameter("username").equals(expected.getUsername())){
                throw new RuntimeException("Le nom d'utilisateur est incorrect !");
            }
            if(!request.getParameter("password").equals(expected.getPassword())){
                throw  new RuntimeException("Le mot de passe est incorrect");
            }
            request.getSession().setAttribute("user",expected);
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() +"/?q=3.2.2"));
        }catch (Exception e){
            request.setAttribute("error",e.getMessage());
            this.getServletContext().getRequestDispatcher("/WEB-INF/question3/2/form.jsp").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("/WEB-INF/question3/2/form.jsp").forward(request,response);
    }
}
