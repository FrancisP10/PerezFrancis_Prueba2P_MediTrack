package com.espe.meditrack.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
/**Pruebas unitarias del modelo Appointment: validan getters y copias defensivas.*/
public class AppointmentTest {

    @Test
    public void getters_dadoUnaCitaCreada_debenDevolverLosMismosValoresDelConstructor() {
        // Arrange
        List<String> emails = Arrays.asList("correo1@mail.com", "correo2@gmail.com");
        Appointment cita = new Appointment("A1", "Ana Torres", "Cardiologia", 45.0, emails);
        // Act
        String id = cita.getId();
        String nombre = cita.getPatientName();
        String especialidad = cita.getSpecialty();
        Double costo = cita.getCostUsd();
        List<String> correos = cita.getNotifyEmails();
        // Assert
        assertEquals("A1", id);
        assertEquals("Ana Torres", nombre);
        assertEquals("Cardiologia", especialidad);
        assertEquals(45.0, costo, 0.0001);
        assertEquals(emails, correos);
    }

    @Test
    public void constructor_dadoQueLaListaOriginalSeModificaDespuesDeCrearElObjeto_noDebeAlterarElEstadoInterno() {
        // Arrange
        List<String> emailsOriginales = new ArrayList<>();
        emailsOriginales.add("correo1@gmail.com");
        Appointment cita = new Appointment("A2", "Luis Perez", "Pediatria", 30.0, emailsOriginales);
        // Act: se modifica la lista ORIGINAL despues de construir el objeto
        emailsOriginales.add("correo-intruso@gmail.com");
        // Assert: el tamano interno de la cita no cambia (copia defensiva en el constructor)
        assertEquals(1, cita.getNotifyEmails().size());
    }

    @Test
    public void getNotifyEmails_alSerInvocado_debeDevolverUnaListaDistintaALaOriginal() {
        // Arrange
        List<String> emails = new ArrayList<>();
        emails.add("correo1@gmail.com");
        Appointment cita = new Appointment("A3", "Maria Gomez", "Dermatologia", 25.5, emails);
        // Act
        List<String> correosDevueltos = cita.getNotifyEmails();
        // Assert: la referencia devuelta NO es la misma que la lista original (copia defensiva en el getter)
        assertNotSame(emails, correosDevueltos);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getNotifyEmails_alIntentarModificarse_debeLanzarUnsupportedOperationException() {
        // Arrange
        List<String> emails = Arrays.asList("correo1@gmail.com");
        Appointment cita = new Appointment("A4", "Pedro Ruiz", "Traumatologia", 50.0, emails);
        // Act: se intenta modificar la lista de solo lectura devuelta por el getter
        cita.getNotifyEmails().add("correo-nuevo@gmail.com");
        // Assert: se espera UnsupportedOperationException (declarado en @Test)
    }
}
