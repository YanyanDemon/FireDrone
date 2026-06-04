package com.firedrone.controller;

import com.firedrone.dao.DeliveryDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/delivery")
public class DeliveryServlet extends HttpServlet {

    private final DeliveryDao deliveryDao = new DeliveryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("list", deliveryDao.findAll());
        request.getRequestDispatcher("delivery.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String taskIdParam = request.getParameter("taskId");

        if (taskIdParam == null || !taskIdParam.matches("\\d+")) {
            response.sendRedirect("delivery");
            return;
        }

        int taskId = Integer.parseInt(taskIdParam);
        deliveryDao.finishTask(taskId);
        response.sendRedirect("delivery");
    }
}