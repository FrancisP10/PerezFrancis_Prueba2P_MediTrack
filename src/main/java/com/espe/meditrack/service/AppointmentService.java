package com.espe.meditrack.service;

import com.espe.meditrack.model.Appointment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
/**Servicio reactivo que procesa citas médicas usando operadores de Project Reactor. Aplica filter, map y defaultIfEmpty sin bloquear el flujo.*/
@Service
public class AppointmentService {

    /**Cita generica que se emite unicamente cuando, tras aplicar el filtro de negocio, no queda ninguna cita valida en el flujo.*/
    private static final Appointment CITA_GENERICA = new Appointment(
            "GEN-000",
            "Paciente no asignado",
            "medicina general",
            0.0,
            Collections.emptyList()
    );

    /**Expone, de forma reactiva y no bloqueante, las citas validas.
     * Una cita es valida si costUsd > 0 y notifyEmails no esta vacia.*/
    public Flux<Appointment> getValidAppointments() {
        return procesarCitas(citasDeEjemplo());
    }

    /**Aplica la cadena de operadores de Project Reactor sobre un Flux de citas de entrada.
     * Se separa de getValidAppointments() (visibilidad de paquete) para poder probar el caso "todas invalidas" inyectando un
     * flujo de entrada distinto en las pruebas unitarias, sin duplicar logica.*/
    Flux<Appointment> procesarCitas(Flux<Appointment> citas) {
        return citas
                // .filter(...): deja pasar unicamente las citas que cumplen la regla de negocio (costUsd > 0 y notifyEmails no vacia).
                // Se descartan las demas de forma declarativa, sin necesidad de un "if" imperativo.
                .filter(cita -> cita.getCostUsd() != null && cita.getCostUsd() > 0
                        && cita.getNotifyEmails() != null && !cita.getNotifyEmails().isEmpty())
                // .map(...): transforma cada cita valida en una NUEVA instancia (Appointment es inmutable,
                // por eso no se modifica la original) con la especialidad en mayusculas, lista para mostrarse al usuario.
                .map(cita -> new Appointment(
                        cita.getId(),
                        cita.getPatientName(),
                        cita.getSpecialty() == null ? null : cita.getSpecialty().toUpperCase(),
                        cita.getCostUsd(),
                        cita.getNotifyEmails()))
                // .defaultIfEmpty(...): si el filtro elimino todas las citas (es decir, todas eran invalidas),
                // el Flux emite una cita generando una vez de completar vacio, garantizando que siempre haya una respuesta.
                .defaultIfEmpty(CITA_GENERICA);
    }

    /**Busca, de forma reactiva, una cita valida por su id.
     * Prohibido usar block(): se resuelve el caso "no encontrado" con switchIfEmpty(Mono.error(...)), manteniendo todo el flujo no bloqueante.*/
    public Mono<Appointment> findById(String id) {
        return getValidAppointments()
                .filter(cita -> cita.getId() != null && cita.getId().equalsIgnoreCase(id))
                .next() // toma el primer elemento que coincide (o completa vacio si ninguno coincide)
                // .switchIfEmpty(...): si el Mono anterior no emitio ningun valor (no se encontro la cita),
                // se sustituye por un Mono en estado de error, en lugar de bloquear el hilo o lanzar una excepcion imperativa.
                .switchIfEmpty(Mono.error(
                        new AppointmentNotFoundException("No se encontro una cita valida con id: " + id)));
    }

    /**Genera 5 citas en memoria: 3 validas y 2 invalidas (una con costUsd = 0 y otra con notifyEmails vacia).*/
    private Flux<Appointment> citasDeEjemplo() {
        return Flux.just(
                // 3 citas validas
                new Appointment("A1", "Ana Torres", "Cardiologia", 45.0,
                        Arrays.asList("ana.torres@mail.com", "recepcion@clinic.com")),
                new Appointment("A2", "Luis Perez", "Pediatria", 30.0,
                        Arrays.asList("luis.perez@mail.com")),
                new Appointment("A3", "Maria Gomez", "Dermatologia", 25.5,
                        Arrays.asList("maria.gomez@mail.com")),
                // 2 citas invalidas
                new Appointment("A4", "Pedro Ruiz", "Traumatologia", 0.0, // costUsd = 0 -> invalida
                        Arrays.asList("pedro.ruiz@mail.com")),
                new Appointment("A5", "Carla Diaz", "Neurologia", 50.0,
                        Collections.emptyList()) // notifyEmails vacia -> invalida
        );
    }
}
