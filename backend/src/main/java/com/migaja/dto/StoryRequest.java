package com.migaja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoryRequest {
    @NotBlank(message = "El título es requerido")
    private String title;

    @NotBlank(message = "El contenido es requerido")
    private String content;

    @NotNull(message = "La fecha de inicio de la relación es requerida")
    private LocalDateTime relationshipStartDate;

    private LocalDateTime relationshipEndDate;

    // Ubicación
    private String city;

    private String municipality;

    private String department;

    private String country;

    // Duración en días
    private Integer relationshipDurationDays;
}
