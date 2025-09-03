package co.com.bancolombia.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationRequest {

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que 0")
    private Integer amount;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es 1 mes")
    @Max(value = 360, message = "El plazo máximo es 360 meses")
    private Integer term;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Pattern(
        regexp = "[A-Za-z0-9.-]{5,20}",
        message = "El documento debe tener entre 5 y 20 caracteres alfanuméricos (se permite . y -)"
    )
    private String identityDocument;

    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 254, message = "El correo no puede exceder 254 caracteres")
    private String email;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    @Positive(message = "El tipo de préstamo debe ser un id positivo")
    private Long loanTypeId;

    @NotNull(message = "La fecha de solicitud es obligatoria")
    @Future(message = "La fecha de solicitud debe ser en el futuro")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applicationDate;
}
