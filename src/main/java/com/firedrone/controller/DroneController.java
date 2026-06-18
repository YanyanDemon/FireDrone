package com.firedrone.controller;

import com.firedrone.entity.DroneEntity;
import com.firedrone.repository.DroneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drone")
public class DroneController {

    private final DroneRepository droneRepository;

    public DroneController(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("droneList", droneRepository.findAllByOrderByIdDesc());
        return "drone";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam String area,
                      @RequestParam(defaultValue = "100") Integer battery,
                      @RequestParam(defaultValue = "5.0") Double maxLoad,
                      @RequestParam(defaultValue = "10") Integer maskCapacity,
                      RedirectAttributes redirectAttributes) {

        if (name.isBlank()) {
            redirectAttributes.addFlashAttribute("msg", "无人机名称不能为空");
            return "redirect:/drone";
        }

        DroneEntity drone = new DroneEntity();
        drone.setName(name.trim());
        drone.setArea(area.trim());
        drone.setBattery(battery);
        drone.setMaxLoad(maxLoad);
        drone.setMaskCapacity(maskCapacity);
        drone.setStatus("空闲");
        droneRepository.save(drone);

        redirectAttributes.addFlashAttribute("msg", "无人机「" + name + "」已添加");
        return "redirect:/drone";
    }
}
