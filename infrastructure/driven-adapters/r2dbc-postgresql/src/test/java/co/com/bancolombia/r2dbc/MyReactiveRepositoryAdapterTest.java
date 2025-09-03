package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.r2dbc.adapter.ApplicationReactiveRepositoryAdapter;
import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import co.com.bancolombia.r2dbc.repository.ApplicationReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    ApplicationReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    ApplicationReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Test
    void mustFindValueById() {
        ApplicationEntity entity = ApplicationEntity.builder()
                .id(1L)
                .amount(10000)
                .term(12)
                .email("test@email.com")
                .applicationDate(LocalDateTime.now())
                .identityDocument("123456789")
                .statusId(1)
                .loanTypeId(2L)
                .build();
        Application app = new Application(); // ajusta si tienes builder
        when(repository.findById(1L)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(app);

        Mono<Application> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNext(app)
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        ApplicationEntity entity = ApplicationEntity.builder()
                .id(2L)
                .amount(5000)
                .term(6)
                .email("all@email.com")
                .applicationDate(LocalDateTime.now())
                .identityDocument("987654321")
                .statusId(2)
                .loanTypeId(3L)
                .build();
        Application app = new Application(); // ajusta si tienes builder
        when(repository.findAll()).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(app);

        Flux<Application> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNext(app)
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        ApplicationEntity entity = ApplicationEntity.builder()
                .id(3L)
                .amount(7000)
                .term(24)
                .email("example@email.com")
                .applicationDate(LocalDateTime.now())
                .identityDocument("555555555")
                .statusId(3)
                .loanTypeId(4L)
                .build();
        Application app = Application.builder()
                .id(3L)
                .amount(7000)
                .term(24)
                .email("example@email.com")
                .applicationDate(entity.getApplicationDate())
                .identityDocument("555555555")
                .statusId(3)
                .loanTypeId(4L)
                .build();
        Example<ApplicationEntity> example = Example.of(entity);
        when(mapper.map(app, ApplicationEntity.class)).thenReturn(entity);
        when(repository.findAll(example)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(app);

        Flux<Application> result = repositoryAdapter.findByExample(app);

        StepVerifier.create(result)
                .expectNext(app)
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        ApplicationEntity entity = ApplicationEntity.builder()
                .id(4L)
                .amount(8000)
                .term(36)
                .email("save@email.com")
                .applicationDate(LocalDateTime.now())
                .identityDocument("444444444")
                .statusId(4)
                .loanTypeId(5L)
                .build();
        Application app = Application.builder()
                .id(4L)
                .amount(8000)
                .term(36)
                .email("save@email.com")
                .applicationDate(entity.getApplicationDate())
                .identityDocument("444444444")
                .statusId(4)
                .loanTypeId(5L)
                .build();
        when(mapper.map(app, ApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(app);

        Mono<Application> result = repositoryAdapter.save(app);

        StepVerifier.create(result)
                .expectNext(app)
                .verifyComplete();
    }

    // Guardar una aplicación con campos nulos opcionales
    @Test
    void mustSaveValueWithNullFields() {
        ApplicationEntity entity = ApplicationEntity.builder()
            .id(10L)
            .amount(5000)
            .term(24)
            .email(null)
            .applicationDate(LocalDateTime.now())
            .identityDocument(null)
            .statusId(1)
            .loanTypeId(2L)
            .build();
        Application app = Application.builder()
            .id(10L)
            .amount(5000)
            .term(24)
            .email(null)
            .applicationDate(entity.getApplicationDate())
            .identityDocument(null)
            .statusId(1)
            .loanTypeId(2L)
            .build();
        when(mapper.map(app, ApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Application.class)).thenReturn(app);

        Mono<Application> result = repositoryAdapter.save(app);
        StepVerifier.create(result)
            .expectNext(app)
            .verifyComplete();
    }

    // Guardar y simular error en el repositorio
    @Test
    void mustHandleSaveError() {
        Application app = Application.builder()
            .id(11L)
            .amount(10000)
            .term(36)
            .email("error@email.com")
            .applicationDate(LocalDateTime.now())
            .identityDocument("error")
            .statusId(2)
            .loanTypeId(3L)
            .build();
        ApplicationEntity entity = ApplicationEntity.builder()
            .id(11L)
            .amount(10000)
            .term(36)
            .email("error@email.com")
            .applicationDate(app.getApplicationDate())
            .identityDocument("error")
            .statusId(2)
            .loanTypeId(3L)
            .build();
        when(mapper.map(app, ApplicationEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<Application> result = repositoryAdapter.save(app);

        StepVerifier.create(result)
            .expectErrorMatches(e -> e instanceof RuntimeException && e.getMessage().equals("DB error"))
            .verify();
    }
}
