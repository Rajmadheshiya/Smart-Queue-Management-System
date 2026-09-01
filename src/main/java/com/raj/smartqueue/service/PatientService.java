package com.raj.smartqueue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raj.smartqueue.entity.Patient;
import com.raj.smartqueue.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    // =========================================================
    // Save / Join Queue
    // =========================================================
    public Patient savePatient(Patient patient) {

        // New Patient
        if (patient.getId() == null) {

            // Check if this phone number is already in the queue
            boolean alreadyWaiting =
                    patientRepository.existsByPhoneAndStatus(
                            patient.getPhone(),
                            "Waiting"
                    );

            if (alreadyWaiting) {

                throw new RuntimeException(
                        "Patient with this phone number is already in the queue."
                );
            }

            // Generate next token safely
            int nextToken = 101;

            List<Patient> patients = patientRepository.findAll();

            for (Patient p : patients) {

                if (p.getTokenNumber() != null
                        && p.getTokenNumber() >= nextToken) {

                    nextToken = p.getTokenNumber() + 1;
                }
            }

            // Set token number
            patient.setTokenNumber(nextToken);

            // Set initial status
            patient.setStatus("Waiting");

            // Calculate estimated waiting time
            int waitingPatients = (int) getWaitingCount();

            patient.setEstimatedTime(waitingPatients * 5);
        }

        return patientRepository.save(patient);
    }


    // =========================================================
    // Search Patients
    // =========================================================
    public List<Patient> searchPatient(String name) {

        List<Patient> patients;

        if (name == null || name.trim().isEmpty()) {

            patients = patientRepository.findAll();

        } else {

            patients =
                    patientRepository.findByNameContainingIgnoreCase(name);
        }

        // Recalculate waiting time for displayed patients
        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())) {

                int patientsAhead =
                        getPatientsAhead(patient.getTokenNumber());

                patient.setEstimatedTime(patientsAhead * 5);

            } else {

                patient.setEstimatedTime(0);
            }
        }

        return patients;
    }


    // =========================================================
    // Get All Patients
    // =========================================================
    public List<Patient> getAllPatients() {

        List<Patient> patients = patientRepository.findAll();

        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())) {

                int patientsAhead =
                        getPatientsAhead(patient.getTokenNumber());

                patient.setEstimatedTime(patientsAhead * 5);

            } else {

                patient.setEstimatedTime(0);
            }
        }

        return patients;
    }


    // =========================================================
    // Get Patient By ID
    // =========================================================
    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElse(null);
    }


    // =========================================================
    // Get Patient By Token
    // =========================================================
    public Patient getPatientByToken(Integer tokenNumber) {

        Patient patient =
                patientRepository.findByTokenNumber(tokenNumber)
                        .orElse(null);

        if (patient != null) {

            if ("Waiting".equals(patient.getStatus())) {

                int patientsAhead =
                        getPatientsAhead(patient.getTokenNumber());

                patient.setEstimatedTime(patientsAhead * 5);

            } else {

                patient.setEstimatedTime(0);
            }
        }

        return patient;
    }


    // =========================================================
    // Get Patients Ahead
    // =========================================================
    public int getPatientsAhead(Integer tokenNumber) {

        List<Patient> patients =
                patientRepository.findAll();

        int patientsAhead = 0;

        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())
                    && patient.getTokenNumber() != null
                    && patient.getTokenNumber() < tokenNumber) {

                patientsAhead++;
            }
        }

        return patientsAhead;
    }


    // =========================================================
    // Delete Patient
    // =========================================================
    public void deletePatient(Long id) {

        patientRepository.deleteById(id);

        // Recalculate waiting times after deletion
        recalculateWaitingTimes();
    }


    // =========================================================
    // Complete Patient
    // =========================================================
    public void completePatient(Long id) {

        Patient patient =
                patientRepository.findById(id)
                        .orElse(null);

        if (patient != null) {

            patient.setStatus("Completed");
            patient.setEstimatedTime(0);

            patientRepository.save(patient);

            // Recalculate remaining waiting patients
            recalculateWaitingTimes();
        }
    }


    // =========================================================
    // Recalculate Waiting Times
    // =========================================================
    private void recalculateWaitingTimes() {

        List<Patient> patients =
                patientRepository.findAll();

        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())
                    && patient.getTokenNumber() != null) {

                int patientsAhead =
                        getPatientsAhead(patient.getTokenNumber());

                patient.setEstimatedTime(
                        patientsAhead * 5
                );

            } else {

                patient.setEstimatedTime(0);
            }
        }

        patientRepository.saveAll(patients);
    }


    // =========================================================
    // Waiting Count
    // =========================================================
    public long getWaitingCount() {

        return patientRepository.getWaitingCount();
    }


    // =========================================================
    // Completed Count
    // =========================================================
    public long getCompletedCount() {

        return patientRepository.getCompletedCount();
    }


    // =========================================================
    // Current Token
    // =========================================================
    public int getCurrentToken() {

        List<Patient> patients =
                patientRepository.findAll();

        int currentToken = 0;

        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())
                    && patient.getTokenNumber() != null) {

                if (currentToken == 0
                        || patient.getTokenNumber() < currentToken) {

                    currentToken =
                            patient.getTokenNumber();
                }
            }
        }

        return currentToken;
    }


    // =========================================================
    // Call Next Patient
    // =========================================================
    public void nextPatient() {

        List<Patient> patients =
                patientRepository.findAll();

        Patient nextPatient = null;

        // Find the waiting patient with the smallest token
        for (Patient patient : patients) {

            if ("Waiting".equals(patient.getStatus())
                    && patient.getTokenNumber() != null) {

                if (nextPatient == null
                        || patient.getTokenNumber()
                        < nextPatient.getTokenNumber()) {

                    nextPatient = patient;
                }
            }
        }

        if (nextPatient != null) {

            nextPatient.setStatus("Completed");
            nextPatient.setEstimatedTime(0);

            patientRepository.save(nextPatient);

            // Recalculate remaining waiting patients
            recalculateWaitingTimes();
        }
    }
}