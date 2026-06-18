package com.firedrone.controller;

import com.firedrone.repository.AlarmRepository;
import com.firedrone.service.DispatchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/alarm")
public class AlarmController {

    private final AlarmRepository alarmRepository;
    private final DispatchService dispatchService;

    public AlarmController(AlarmRepository alarmRepository,
                           DispatchService dispatchService) {
        this.alarmRepository = alarmRepository;
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("alarmList", alarmRepository.findAllWithMonitorPoint());
        return "alarm";
    }

    @PostMapping
    public String dispatch(@RequestParam Integer alarmId,
                           @RequestParam String targetArea,
                           @RequestParam(defaultValue = "5") Integer maskCount,
                           RedirectAttributes redirectAttributes) {
        boolean success = dispatchService.dispatchMask(alarmId, targetArea, maskCount);
        if (!success) {
            redirectAttributes.addFlashAttribute("msg", "暂无可用的无人机，任务派发失败");
        } else {
            redirectAttributes.addFlashAttribute("msg", "无人机配送任务已生成");
        }
        return "redirect:/alarm";
    }
}
