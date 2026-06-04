package com.firedrone.controller;

import com.firedrone.dao.AlarmDao;
import com.firedrone.dao.MonitorDao;
import com.firedrone.model.MonitorPoint;
import com.firedrone.service.FireRiskService;
import com.firedrone.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/monitor")
public class MonitorServlet extends HttpServlet {

    private final MonitorDao monitorDao = new MonitorDao();
    private final AlarmDao alarmDao = new AlarmDao();
    private final FireRiskService riskService = new FireRiskService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("list", monitorDao.findAll());
        request.getRequestDispatcher("monitor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || !idParam.matches("\\d+")) {
            response.sendRedirect("monitor");
            return;
        }

        int id = Integer.parseInt(idParam);
        MonitorPoint point = monitorDao.findById(id);

        if (point == null) {
            response.sendRedirect("monitor");
            return;
        }

        String risk = riskService.calculateRiskLevel(
                point.getTemperature(),
                point.getSmoke(),
                point.getCo(),
                point.getFlame()
        );

        try {
            DBUtil.beginTransaction();

            monitorDao.updateRiskLevel(id, risk);

            if ("危险".equals(risk) || "紧急".equals(risk)) {
                String content = point.getArea() + " " + point.getName()
                        + " 出现火情风险，当前等级：" + risk;
                alarmDao.addAlarm(id, risk, content);
            }

            DBUtil.commitTransaction();
        } catch (Exception e) {
            DBUtil.rollbackTransaction();
            throw new RuntimeException("监控评估失败", e);
        }

        response.sendRedirect("monitor");
    }
}