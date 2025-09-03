package co.com.bancolombia.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table("estados")
public class StatusEntity {
    @Id
    @Column("id_estado")
    private Long id;

    @Column("nombre")
    private String name;

    @Column("descripcion")
    private String description;
}
