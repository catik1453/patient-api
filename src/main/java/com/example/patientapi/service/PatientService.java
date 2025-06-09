package com.example.patientapi.service;

import com.example.patientapi.model.Patient;
import com.example.patientapi.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient save(Patient patient) {
        return repository.save(patient);
    }

    public List<Patient> getAll() {
        return repository.findAll();
    }

    public Optional<Patient> getById(Long id) {
        return repository.findById(id);
    }

    public List<Patient> getByAge(int age) {
        return repository.findAll()
                .stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ✅ Multithread ile hasta kaydetme
    public void saveMultiplePatientsConcurrently(List<Patient> patients) {
        for (Patient patient : patients) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread: " + Thread.currentThread().getName() + " - Saving: " + patient.getName());
                save(patient);
            });
            thread.start();
        }
    }

    // ✅ Yaşa göre sıralama
    public List<Patient> getPatientsSortedByAge() {
        List<Patient> patients = repository.findAll();
        patients.sort(Comparator.comparingInt(Patient::getAge));
        return patients;
    }

    // ✅ Ada göre sıralama
    public List<Patient> getPatientsSortedByName() {
        List<Patient> patients = repository.findAll();
        patients.sort(Comparator.comparing(Patient::getName));
        return patients;
    }

    // ✅ Ada göre arama
    public List<Patient> searchByName(String keyword) {
        return repository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
