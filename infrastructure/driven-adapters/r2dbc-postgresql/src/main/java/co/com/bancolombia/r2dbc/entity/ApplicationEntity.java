package co.com.bancolombia.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder(toBuilder = true)
@Table("solicitud")
public class ApplicationEntity {

    @Id
    @Column("id_solicitud")
    private Long id;

    @Column("monto")
    private Integer amount;

    @Column("plazo")
    private Integer term;               // agrega si existe en tu tabla

    @Column("email")
    private String email;

    @Column("fecha_solicitud")
    private LocalDateTime applicationDate;  // si existe esa columna (opcional)

    @Column("documento_identidad")
    private String identityDocument;        // si existe esa columna (opcional)

    // FK puras (persisten en DB)
    @Column("id_estado")
    private Integer statusId;

    @Column("id_tipo_prestamo")
    private Long loanTypeId;

    // Relacionados (no se persisten automáticamente)
    @Transient
    private StatusEntity status;

    @Transient
    private LoanTypeEntity loanType; // (si tu dominio se llama "LoanType", mantenlo)
}
