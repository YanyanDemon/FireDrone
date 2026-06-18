package com.firedrone.controller;

import com.firedrone.entity.AlarmEntity;
import com.firedrone.entity.MonitorPointEntity;
import com.firedrone.repository.AlarmRepository;
import com.firedrone.repository.MonitorPointRepository;
import com.firedrone.service.FireRiskService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorPointRepository monitorPointRepository;
    private final AlarmRepository alarmRepository;
    private final FireRiskService fireRiskService;

    public MonitorController(MonitorPointRepository monitorPointRepository,
                             AlarmRepository alarmRepository,
                             FireRiskService fireRiskService) {
        this.monitorPointRepository = monitorPointRepository;
        this.alarmRepository = alarmRepository;
        this.fireRiskService = fireRiskService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("monitorList", monitorPointRepository.findAllByOrderByIdDesc());
        return "monitor";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam String area,
                      @RequestParam(defaultValue = "0") Double temperature,
                      @RequestParam(defaultValue = "0") Double smoke,
                      @RequestParam(defaultValue = "0") Double co,
                      @RequestParam(defaultValue = "0") Integer flame,
                      RedirectAttributes redirectAttributes) {

        if (name.isBlank()) {
            redirectAttributes.addFlashAttribute("msg", "监测点名称不能为空");
            return "redirect:/monitor";
        }

        MonitorPointEntity point = new MonitorPointEntity();
        point.setName(name.trim());
        point.setArea(area.trim());
        point.setTemperature(temperature);
        point.setSmoke(smoke);
        point.setCo(co);
        point.setFlame(flame);
        point.setRiskLevel("正常");
        point.setUpdateTime(LocalDateTime.now());
        monitorPointRepository.save(point);

        redirectAttributes.addFlashAttribute("msg", "监测点「" + name + "」已添加");
        return "redirect:/monitor";
    }

    @PostMapping
    @Transactional
    public String evaluate(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        MonitorPointEntity point = monitorPointRepository.findById(id).orElse(null);
        if (point == null) {
            return "redirect:/monitor";
        }

        String risk = fireRiskService.calculateRiskLevel(
                point.getTemperature(), point.getSmoke(), point.getCo(), point.getFlame());

        monitorPointRepository.updateRiskLevel(id, risk);

        if ("危险".equals(risk) || "紧急".equals(risk)) {
            AlarmEntity alarm = new AlarmEntity();
            alarm.setMonitorId(id);
            alarm.setLevel(risk);
            alarm.setContent(point.getArea() + " " + point.getName() + " 出现火情风险，当前等级：" + risk);
            alarm.setStatus("未处理");
            alarm.setCreateTime(LocalDateTime.now());
            alarmRepository.save(alarm);
        }

        redirectAttributes.addFlashAttribute("msg", point.getName() + " 风险评估完成，当前等级：" + risk);
        return "redirect:/monitor";
    }
}
