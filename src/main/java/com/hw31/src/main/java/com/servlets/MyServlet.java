package com.servlets;

import com.servlets.model.Visitor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MyServlet", value = "/MyServlet")
public class MyServlet extends HttpServlet {

    private static List<Visitor> visitors = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Visitor visitor = new Visitor(req.getLocalAddr(), req.getHeader("User-Agent"), LocalDateTime.now());
        visitors.add(visitor);
        req.setAttribute("visitors", visitors);
        req.getRequestDispatcher("ip.jsp").forward(req, resp);
    }
}
