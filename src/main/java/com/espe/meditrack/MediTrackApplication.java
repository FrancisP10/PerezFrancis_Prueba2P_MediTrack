package com.espe.meditrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**Punto de entrada de la aplicacion MediTrack. Levanta un servidor reactivo (Netty) gracias a spring-boot-starter-webflux.*/
@SpringBootApplication
public class MediTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediTrackApplication.class, args);
    }
}
