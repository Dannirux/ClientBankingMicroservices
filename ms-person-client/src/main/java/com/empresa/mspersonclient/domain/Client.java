package com.empresa.mspersonclient.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@EqualsAndHashCode(callSuper = true)
public class Client extends Person {

    public Client(String nombre, String genero, int edad, String identificacion,
                  String direccion, String telefono, String clienteId, String contraseña, boolean estado) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contraseña = contraseña;
        this.estado = estado;
    }

    @Column(nullable = false, unique = true)
    private String clienteId;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contraseña;
    private boolean estado;

    @PrePersist
    private void generateClientId() {
        if (this.clienteId == null || this.clienteId.isBlank()) {
            this.clienteId = UUID.randomUUID().toString();
        }
    }

}