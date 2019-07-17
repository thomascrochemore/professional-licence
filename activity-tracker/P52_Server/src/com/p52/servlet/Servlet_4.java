package com.p52.servlet;

import com.p52.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Servlet_4 extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<User> users = new ArrayList<User>();
        users.add(new User("Otruch","Robin","Couty"));
        users.add(new User("Rushmore","Thomas","Crochemore"));
        users.add(new User("Mister Lego","Pierre","Kraemer"));
        request.setAttribute("users",users);
        this.getServletContext().getRequestDispatcher("/WEB-INF/question4/get.jsp").forward(request,response);
    }
}
