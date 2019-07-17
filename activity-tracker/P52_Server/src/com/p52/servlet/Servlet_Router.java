package com.p52.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet_Router extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardServletFromUrl(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardServletFromUrl(request,response);
    }
    protected  void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardServletFromUrl(request,response);
    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        forwardServletFromUrl(request,response);
    }
    private void forwardServletFromUrl(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        try {
            if(request.getParameter("q") == null ){
                this.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp").forward(request,response);
                return;
            }
            String[] splitParameter = request.getParameter("q").split("\\.");
            String servletRoute = "/question/" + String.join("/",splitParameter);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(servletRoute);
            if(dispatcher == null || (request.getAttribute("parent") != null && request.getAttribute("parent").equals("router"))) {
                throw new IOException("URL invalide !");
            }
            request.setAttribute("parent","router");
            dispatcher.forward(request,response);
        }catch (Exception e){
            request.setAttribute("error",e.getMessage());
            response.setStatus(500);
            this.getServletContext().getRequestDispatcher("/WEB-INF/error.jsp").forward(request,response);
        }

    }
}
