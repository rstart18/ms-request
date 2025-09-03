package co.com.bancolombia.usecase.sendapplication;

import co.com.bancolombia.model.application.Application;
import reactor.core.publisher.Mono;

/** Valida y normaliza una Application para operación de "crear/enviar". */
public interface SendApplicationValidator {
    /**
     * Normaliza y valida reglas de negocio locales. Si todo está OK, devuelve la Application normalizada.
     * Si hay error de reglas, emite BusinessException.
     */
    Mono<Application> validateForCreate(Application input);
}
