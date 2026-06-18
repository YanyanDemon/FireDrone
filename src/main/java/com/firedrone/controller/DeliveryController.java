package com.firedrone.controller;

import com.firedrone.entity.DeliveryTaskEntity;
import com.firedrone.repository.AlarmRepository;
import com.firedrone.repository.DeliveryTaskRepository;
import com.firedrone.repository.DroneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryTaskRepository deliveryTaskRepository;
    private final DroneRepository droneRepository;
    private final AlarmRepository alarmRepository;

    public DeliveryController(DeliveryTaskRepository deliveryTaskRepository,
                              DroneRepository droneRepository,
                              AlarmRepository alarmRepository) {
        this.deliveryTaskRepository = deliveryTaskRepository;
        this.droneRepository = droneRepository;
        this.alarmRepository = alarmRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("taskList", deliveryTaskRepository.findAllWithDrone());
        return "delivery";
    }

    @PostMapping
    @Transactional
    public String finish(@RequestParam Integer taskId) {
        DeliveryTaskEntity task = deliveryTaskRepository.findById(taskId).orElse(null);
        if (task == null) {
            return "redirect:/delivery";
        }

        deliveryTaskRepository.finishTask(taskId);
        droneRepository.updateStatus(task.getDroneId(), "空闲");
        alarmRepository.updateStatus(task.getAlarmId(), "已处理");

        return "redirect:/delivery";
    }
}
