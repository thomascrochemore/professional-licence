package com.p52.servlet;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

public class Servlet_1 extends HttpServlet {
    protected void doPost(HttpServletRequest request,HttpServletResponse  response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("Je suis un voleur !");
        writer.close();
    }

    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("Je suis un poney !");
        writer.close();
    }

    protected void doPut(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("Je suis une autruche !");
        writer.close();
    }

    protected void doDelete(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.println("Je suis une canard !");
        writer.close();
    }

}
