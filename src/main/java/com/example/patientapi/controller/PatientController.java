package com.example.patientapi.controller;

import com.example.patientapi.model.Patient;
import com.example.patientapi.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    // Tek hasta ekleme
    @PostMapping
    public ResponseEntity<Patient> add(@RequestBody Patient patient) {
        return ResponseEntity.ok(service.save(patient));
    }

    // Tüm hastaları listele
    @GetMapping
    public ResponseEntity<List<Patient>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ID'ye göre hasta getir
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Yaşa göre filtrele (Stream API)
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchByAge(@RequestParam int age) {
        return ResponseEntity.ok(service.getByAge(age));
    }

    // Ada göre arama
    @GetMapping("/searchByName")
    public ResponseEntity<List<Patient>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchByName(keyword));
    }

    // Yaşa göre sıralama
    @GetMapping("/sort/age")
    public ResponseEntity<List<Patient>> sortByAge() {
        return ResponseEntity.ok(service.getPatientsSortedByAge());
    }

    // Ada göre sıralama
    @GetMapping("/sort/name")
    public ResponseEntity<List<Patient>> sortByName() {
        return ResponseEntity.ok(service.getPatientsSortedByName());
    }

    // Hasta sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Multithread ile hasta kaydetme
    @PostMapping("/multithread")
    public ResponseEntity<String> addMultiplePatients(@RequestBody List<Patient> patients) {
        service.saveMultiplePatientsConcurrently(patients);
        return ResponseEntity.ok("Patients are being saved in separate threads.");
    }
}
