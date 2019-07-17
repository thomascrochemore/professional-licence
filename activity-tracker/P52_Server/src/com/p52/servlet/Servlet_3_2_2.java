package com.p52.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet_3_2_2 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("user",request.getSession().getAttribute("user"));
        this.getServletContext().getRequestDispatcher("/WEB-INF/question3/showUser.jsp").forward(request,response);
    }
}
