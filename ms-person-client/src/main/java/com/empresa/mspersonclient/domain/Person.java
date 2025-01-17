package com.empresa.mspersonclient.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@MappedSuperclass
public class Person {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private String id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El género no puede estar vacío")
    @Pattern(regexp = "Masculino|Femenino|Otro", message = "El género debe ser 'Masculino', 'Femenino' o 'Otro'")
    @Column(nullable = false)
    private String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    @Column(nullable = false)
    private Integer edad;

    @NotBlank(message = "La identificación no puede estar vacía")
    @Size(max = 20, message = "La identificación no puede tener más de 20 caracteres")
    @Column(unique = true, nullable = false, length = 20)
    private String identificacion;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    @Column(nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "\\d{10,15}", message = "El teléfono debe contener entre 10 y 15 dígitos")
    @Column(nullable = false, length = 15)
    private String telefono;


    public Person(String nombre, String genero, Integer edad, String identificacion, String direccion, String telefono) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Person() {
    }

    @PrePersist
    private void generateId() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }

}
