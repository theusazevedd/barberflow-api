package com.azevedo.barberflow.domain.model;

import com.azevedo.barberflow.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant dateTime;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;


    @ManyToOne
    @JoinColumn(name = "barber_id")
    private User barber;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

}
