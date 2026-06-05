package main.com.firedrone.controller;

import com.firedrone.dao.AlarmDao;
import com.firedrone.dao.DeliveryDao;
import com.firedrone.dao.DroneDao;
import com.firedrone.dao.MonitorDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final MonitorDao monitorDao = new MonitorDao();
    private final AlarmDao alarmDao = new AlarmDao();
    private final DroneDao droneDao = new DroneDao();
    private final DeliveryDao deliveryDao = new DeliveryDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("monitorCount", monitorDao.count());
        request.setAttribute("alarmCount", alarmDao.count());
        request.setAttribute("droneCount", droneDao.count());
        request.setAttribute("taskCount", deliveryDao.count());

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}