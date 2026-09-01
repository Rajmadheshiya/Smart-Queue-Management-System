package com.raj.smartqueue.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.raj.smartqueue.entity.Patient;
import com.raj.smartqueue.service.PatientService;

@Controller
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Open HTML Form
    @GetMapping("/form")
    public String showForm(Model model) {

        model.addAttribute("patient", new Patient());

        return "patient-form";
    }
    
 // Patient side - Open Join Queue page
    @GetMapping("/join")
    public String showJoinQueueForm(Model model) {

        model.addAttribute("patient", new Patient());

        return "patient-join";
    }

    // Patient side - Join Queue
    @PostMapping("/join")
    public String joinQueue(Patient patient, Model model) {

        try {

            Patient savedPatient = patientService.savePatient(patient);

            model.addAttribute("patient", savedPatient);

            model.addAttribute("patientsAhead",
                    patientService.getPatientsAhead(
                            savedPatient.getTokenNumber()
                    ));

            return "patient-status";

        } catch (RuntimeException e) {

            model.addAttribute("error",
                    "This phone number is already in the queue.");

            return "patient-join";
        }
    }
    
    @GetMapping("/status")
    public String showStatusForm() {

        return "patient-check-status";
    }
    @PostMapping("/status")
    public String checkStatus(
            @RequestParam Integer tokenNumber,
            Model model) {

        Patient patient = patientService.getPatientByToken(tokenNumber);

        if (patient == null) {
            model.addAttribute("error", "Token not found!");
            return "patient-check-status";
        }

        model.addAttribute("patient", patient);
        
        model.addAttribute("patientsAhead",
                patientService.getPatientsAhead(tokenNumber));

        return "patient-status";
    }
 // Save Patient
    @PostMapping("/save")
    public String savePatient(Patient patient, Model model) {

        try {

            patientService.savePatient(patient);

            return "redirect:/patient/list";

        } catch (RuntimeException e) {

            model.addAttribute("error",
                    "This phone number is already in the queue.");

            return "patient-form";
        }
    }
    // Get All Patients
    @GetMapping("/all")
    @ResponseBody
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    @GetMapping("/list")
    public String showPatients(
            @RequestParam(required = false) String keyword,
            Model model) {

        model.addAttribute("patients", patientService.searchPatient(keyword));
        model.addAttribute("keyword", keyword);
        
        model.addAttribute("currentToken",
                patientService.getCurrentToken());

        return "patient-list";
    }
    @GetMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, Model model) {

        Patient patient = patientService.getPatientById(id);
        
        System.out.println("Token = " + patient.getTokenNumber());


        model.addAttribute("patient", patient);

        return "patient-form";
    }
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);

        return "redirect:/patient/list";
    }
    @GetMapping("/complete/{id}")
    public String completePatient(@PathVariable Long id) {

        patientService.completePatient(id);

        return "redirect:/patient/list";
    }
    
    @GetMapping("/next")
    public String nextPatient() {

        patientService.nextPatient();

        return "redirect:/";
    }
}