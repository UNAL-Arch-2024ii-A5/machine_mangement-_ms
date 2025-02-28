package com.gymmaster.machine_management_ms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "machines")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único de la máquina
    private String name; // Nombre de la máquina (e.g., "Caminadora")
    private String description; // Breve descripción de la máquina
    private String state; // Estado actual (e.g., "Disponible", "Ocupada", "En Mantenimiento")
    @Column(name = "last_service")
    private LocalDate lastService;//Fecha del ultimo mantenimiento realizado
    @Column(name = "service_interval")
    private Integer serviceInterval;// Intervalo de mantenimiento recomendado (en días)
    private String type;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "machine_id", referencedColumnName = "id", nullable = false)
    private List<MachineServices> machineServices;
}
