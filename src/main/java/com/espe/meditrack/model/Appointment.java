package com.espe.meditrack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**Representa una cita medica.
 La clase es 100% inmutable:
 * Es "final" (no puede extenderse ni sobrescribirse su comportamiento).
 * Se implementa copias defensivas para proteger la lista notifyEmails tanto en el constructor (para que nadie pueda mutar el objeto modificando la lista
 * que se paso al crearlo) como en el getter (para que nadie pueda mutar el objeto modificando la lista que este devuelve hacia afuera).*/
public final class Appointment {

    private final String id;
    private final String patientName;
    private final String specialty;
    private final Double costUsd;
    private final List<String> notifyEmails;

    public Appointment(String id, String patientName, String specialty, Double costUsd, List<String> notifyEmails) {
        this.id = id;
        this.patientName = patientName;
        this.specialty = specialty;
        this.costUsd = costUsd;
        // Copia defensiva en el CONSTRUCTOR: se crea una nueva ArrayList a partir de la lista recibida.
        // Si el llamador modifica su lista original despues de construir el objeto, el estado interno de Appointment no se ve afectado.
        this.notifyEmails = (notifyEmails == null)
                ? new ArrayList<>()
                : new ArrayList<>(notifyEmails);
    }

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public Double getCostUsd() {
        return costUsd;
    }

    public List<String> getNotifyEmails() {
        // Copia defensiva en el GETTER: se envuelve una NUEVA copia de la lista interna en una vista de solo lectura (unmodifiableList).
        // Asi, quien reciba el resultado no puede ni mutarlo ni, indirectamente, alcanzar la referencia interna real de este objeto.
        return Collections.unmodifiableList(new ArrayList<>(notifyEmails));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment)) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id)
                && Objects.equals(patientName, that.patientName)
                && Objects.equals(specialty, that.specialty)
                && Objects.equals(costUsd, that.costUsd)
                && Objects.equals(notifyEmails, that.notifyEmails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientName, specialty, costUsd, notifyEmails);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientName='" + patientName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", costUsd=" + costUsd +
                ", notifyEmails=" + notifyEmails +
                '}';
    }
}
