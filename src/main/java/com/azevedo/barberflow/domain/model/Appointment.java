package com.azevedo.barberflow.domain.model;

import com.azevedo.barberflow.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"barber_id", "date_time"}))
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;


    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private User barber;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

}
