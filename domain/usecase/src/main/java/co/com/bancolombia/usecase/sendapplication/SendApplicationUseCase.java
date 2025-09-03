package co.com.bancolombia.usecase.sendapplication;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import co.com.bancolombia.model.common.BusinessException;
import co.com.bancolombia.model.ioantype.gateways.IoanTypeRepository;
import co.com.bancolombia.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class SendApplicationUseCase {
    private static final String INITIAL_STATUS_NAME = "PENDIENTE_DE_REVISION";

    private final ApplicationRepository applicationGateway;
    private final IoanTypeRepository loanTypeGateway;
    private final StatusRepository statusGateway;
    private final SendApplicationValidator validator;

    public Mono<Application> execute(Application draft) {
        return validator.validateForCreate(draft)
            .map(app -> app.getApplicationDate() == null
                ? app.toBuilder().applicationDate(LocalDateTime.now()).build()
                : app)
            .flatMap(app ->
                statusGateway.findByName(INITIAL_STATUS_NAME)
                    .switchIfEmpty(Mono.error(
                        BusinessException.invalidField("status", INITIAL_STATUS_NAME)))
                    .map(st -> app.toBuilder()
                        .status(st)
                        .statusId(st.getId())
                        .build())
            )
            .flatMap(app ->
                loanTypeGateway.findById(app.getLoanTypeId())
                    .switchIfEmpty(Mono.error(BusinessException.invalidField("loanTypeId", "Tipo de préstamo no encontrado")))
                    .map(loanType -> app.toBuilder()
                        .loanType(loanType)
                        .loanTypeId(loanType.getId())
                        .build())
            )
            .flatMap(applicationGateway::save);
    }
}
