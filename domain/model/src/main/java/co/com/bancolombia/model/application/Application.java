package co.com.bancolombia.model.application;
import co.com.bancolombia.model.ioantype.LoanType;
import co.com.bancolombia.model.status.Status;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Application {
    private Long id;
    private Integer amount;
    private Integer term;
    private LocalDateTime applicationDate;
    private String email;
    private String identityDocument;
    private Status status;
    private Integer statusId;
    private LoanType loanType;
    private Long loanTypeId;
}
