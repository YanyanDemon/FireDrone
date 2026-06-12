package com.firedrone.controller;

import com.firedrone.dao.DroneDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/drone")
public class DroneServlet extends HttpServlet {

    private final DroneDao droneDao = new DroneDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("list", droneDao.findAll());
        request.getRequestDispatcher("drone.jsp").forward(request, response);
    }
}
