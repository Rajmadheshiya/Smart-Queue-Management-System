package com.raj.smartqueue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.raj.smartqueue.service.PatientService;

@Controller
public class HomeController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("totalPatients", patientService.getAllPatients().size());
        model.addAttribute("waitingPatients", patientService.getWaitingCount());
        model.addAttribute("completedPatients", patientService.getCompletedCount());

        model.addAttribute("currentToken", patientService.getCurrentToken());

        return "home";
    }
}