package co.com.bancolombia.model.ioantype.gateways;

import co.com.bancolombia.model.ioantype.LoanType;
import reactor.core.publisher.Mono;

public interface IoanTypeRepository {
    Mono<LoanType> findById(Long id);
}
