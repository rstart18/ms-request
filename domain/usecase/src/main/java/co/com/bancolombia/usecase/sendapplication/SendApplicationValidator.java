package co.com.bancolombia.usecase.sendapplication;

import co.com.bancolombia.model.application.Application;
import reactor.core.publisher.Mono;

/** Valida y normaliza una Application para operación de "crear/enviar". */
public interface SendApplicationValidator {
    Mono<Application> validateForCreate(Application input);
}
