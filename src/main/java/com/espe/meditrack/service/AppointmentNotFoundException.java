package com.espe.meditrack.service;

/**Se lanza de forma reactiva (envuelta en Mono.error) cuando findById() no encuentra ninguna cita valida con el id solicitado.*/
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String message) {
        super(message);
    }
}
