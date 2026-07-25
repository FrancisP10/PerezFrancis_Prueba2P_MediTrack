package com.espe.meditrack.controller;

import com.espe.meditrack.model.Appointment;
import com.espe.meditrack.service.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**Expone las citas de forma reactiva. Ninguna firma publica devuelve un tipo bloqueante: nunca List<Appointment>, nunca Appointment "pelado", nunca block().*/
/**Controlador REST reactivo de citas médicas. Expone endpoints usando Flux y Mono sin operaciones bloqueantes.*/
@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/api/appointments")
    public Flux<Appointment> getAppointments() {
        return appointmentService.getValidAppointments();
    }

    @GetMapping("/api/appointments/{id}")
    public Mono<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.findById(id);
    }
}
