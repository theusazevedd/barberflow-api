package com.azevedo.barberflow.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "availabilities")
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant dateTime;

    @ManyToOne
    private User barber;

    private boolean available;

}
