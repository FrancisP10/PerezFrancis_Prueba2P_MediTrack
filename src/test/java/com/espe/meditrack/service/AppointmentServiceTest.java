package com.espe.meditrack.service;

import com.espe.meditrack.model.Appointment;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

public class AppointmentServiceTest {

    @Test
    public void getValidAppointments_debeEmitirSoloLasTresValidas() {
        // Arrange
        AppointmentService service = new AppointmentService();
        // Act
        Flux<Appointment> flujo = service.getValidAppointments();
        // Assert
        StepVerifier.create(flujo)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void procesarCitas_dadoQueTodasLasCitasSonInvalidas_debeEmitirSoloLaCitaGenerica() {
        // Arrange
        AppointmentService service = new AppointmentService();
        Flux<Appointment> citasInvalidas = Flux.just(
                new Appointment("X1", "Test Uno", "General", 0.0, Arrays.asList("a@mail.com")), // costo invalido
                new Appointment("X2", "Test Dos", "General", 10.0, Collections.emptyList())      // sin correos
        );
        // Act
        Flux<Appointment> resultado = service.procesarCitas(citasInvalidas);
        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(cita -> "GEN-000".equals(cita.getId()))
                .verifyComplete();
    }

    @Test
    public void findById_dadoUnIdExistente_debeEmitirLaCitaCorrespondiente() {
        // Arrange
        AppointmentService service = new AppointmentService();
        // Act
        Mono<Appointment> mono = service.findById("A1");
        // Assert
        StepVerifier.create(mono)
                .expectNextMatches(cita -> "A1".equals(cita.getId()))
                .verifyComplete();
    }

    @Test
    public void findById_dadoUnIdInexistente_debeTerminarEnError() {
        // Arrange
        AppointmentService service = new AppointmentService();
        // Act
        Mono<Appointment> mono = service.findById("NO-EXISTE");
        // Assert
        StepVerifier.create(mono)
                .expectError(AppointmentNotFoundException.class)
                .verify();
    }
}
