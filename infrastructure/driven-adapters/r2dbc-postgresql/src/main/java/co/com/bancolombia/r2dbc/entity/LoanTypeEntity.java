package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table("tipo_prestamo")
public class IoanTypeEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Long id;

    @Column("nombre")
    private String name;

    @Column("monto_minimo")
    private Integer minAmount;

    @Column("monto_maximo")
    private Integer maxAmount;

    @Column("tasa_interes")
    private Double interestRate;

    @Column("validacion_automatica")
    private Boolean autoValidation;
}
