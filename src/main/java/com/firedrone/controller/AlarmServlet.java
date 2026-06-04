package com.firedrone.controller;

import com.firedrone.dao.AlarmDao;
import com.firedrone.model.Alarm;
import com.firedrone.service.DispatchService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/alarm")
public class AlarmServlet extends HttpServlet {

    private final AlarmDao alarmDao = new AlarmDao();
    private final DispatchService dispatchService = new DispatchService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("list", alarmDao.findAll());
        request.getRequestDispatcher("alarm.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String alarmIdParam = request.getParameter("alarmId");
        String maskCountParam = request.getParameter("maskCount");
        String targetArea = request.getParameter("targetArea");

        if (alarmIdParam == null || !alarmIdParam.matches("\\d+")
                || maskCountParam == null || !maskCountParam.matches("\\d+")) {
            response.sendRedirect("alarm");
            return;
        }

        int alarmId = Integer.parseInt(alarmIdParam);
        int maskCount = Integer.parseInt(maskCountParam);

        boolean success = dispatchService.dispatchMask(alarmId, targetArea, maskCount);

        if (!success) {
            request.getSession().setAttribute("msg", "暂无可用的无人机，任务派发失败");
        } else {
            request.getSession().setAttribute("msg", "无人机配送任务已生成");
        }

        response.sendRedirect("alarm");
    }
}