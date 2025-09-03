package co.com.bancolombia.model.ioantype;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanType {
    private Long id;
    private String name;
    private Integer minAmount;
    private Integer maxAmount;
    private Integer interestRate;
    private Boolean automaticValidation;
}
