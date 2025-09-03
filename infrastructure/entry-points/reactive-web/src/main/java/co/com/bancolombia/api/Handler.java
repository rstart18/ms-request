package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.ApplicationRequest;
import co.com.bancolombia.api.mapper.dto.ApplicationDtoMapper;
import co.com.bancolombia.usecase.sendapplication.SendApplicationUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final SendApplicationUseCase useCase;
    private final ApplicationDtoMapper mapper;
    private final Validator validator;

    public Mono<ServerResponse> createApplication(ServerRequest request) {
        return request.bodyToMono(ApplicationRequest.class)
            .doOnNext(body -> log.debug("[createApplication] body: {}", body))
            .flatMap(this::validateDto)
            .map(mapper::toDomain)                       // DTO -> Dominio
            .flatMap(useCase::execute)                   // Caso de uso
            .map(mapper::toResponse)                     // Dominio -> DTO resp
            .flatMap(resp -> ServerResponse
                .created(location(request, resp.getId())) // 201 + Location
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(resp))
            .onErrorResume(this::mapError);
    }

    // ---- helpers ----
    private Mono<ApplicationRequest> validateDto(ApplicationRequest dto) {
        Set<ConstraintViolation<ApplicationRequest>> violations = validator.validate(dto);
        if (violations.isEmpty()) return Mono.just(dto);

        ConstraintViolation<ApplicationRequest> v = violations.iterator().next();
        String field = v.getPropertyPath().toString();
        String msg   = v.getMessage();
        return Mono.error(new IllegalArgumentException(field + ": " + msg));
    }

    private URI location(ServerRequest req, Long id) {
        // Construye Location a partir de la URL de la petición + "/{id}"
        return org.springframework.web.util.UriComponentsBuilder
            .fromUri(req.uri())
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
    }

    private Mono<ServerResponse> mapError(Throwable t) {
        log.error("[createApplication] failed", t);
        int status = (t instanceof IllegalArgumentException) ? 400 : 500;
        var body = Map.of(
            "code", status == 400 ? "BAD_REQUEST" : "INTERNAL_ERROR",
            "message", t.getMessage()
        );
        return ServerResponse.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body);
    }
}
