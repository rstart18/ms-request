package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.application.gateways.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@DataR2dbcTest
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository repository;

    @Test
    void shouldSaveApplication() {
        Application app = new Application(/* inicializa campos */);
        Mono<Application> result = repository.save(app);

        StepVerifier.create(result)
            .expectNextMatches(saved -> saved.getId() != null)
            .verifyComplete();
    }

    @Test
    void shouldSaveApplicationWithAllFields() {
        Application app = Application.builder()
                .id(null)
                .amount(10000)
                .term(12)
                .applicationDate(LocalDateTime.now())
                .email("test@email.com")
                .identityDocument("123456789")
                .status(null)
                .statusId(1)
                .loanType(null)
                .loanTypeId(2L)
                .build();
        Mono<Application> result = repository.save(app);
        StepVerifier.create(result)
            .expectNextMatches(saved -> saved.getId() != null && saved.getEmail().equals("test@email.com"))
            .verifyComplete();
    }

    @Test
    void shouldSaveApplicationWithNullOptionalFields() {
        Application app = Application.builder()
                .id(null)
                .amount(5000)
                .term(6)
                .applicationDate(LocalDateTime.now())
                .email("nullfields@email.com")
                .identityDocument(null)
                .status(null)
                .statusId(null)
                .loanType(null)
                .loanTypeId(null)
                .build();
        Mono<Application> result = repository.save(app);
        StepVerifier.create(result)
            .expectNextMatches(saved -> saved.getEmail().equals("nullfields@email.com"))
            .verifyComplete();
    }

    @Test
    void shouldNotSaveApplicationWithInvalidAmount() {
        Application app = Application.builder()
                .id(null)
                .amount(-100)
                .term(12)
                .applicationDate(LocalDateTime.now())
                .email("invalid@email.com")
                .identityDocument("123456789")
                .status(null)
                .statusId(1)
                .loanType(null)
                .loanTypeId(2L)
                .build();
        Mono<Application> result = repository.save(app);
        StepVerifier.create(result)
            .expectError()
            .verify();
    }

    @Test
    void shouldNotSaveApplicationWithMissingEmail() {
        Application app = Application.builder()
                .id(null)
                .amount(10000)
                .term(12)
                .applicationDate(LocalDateTime.now())
                .email(null)
                .identityDocument("123456789")
                .status(null)
                .statusId(1)
                .loanType(null)
                .loanTypeId(2L)
                .build();
        Mono<Application> result = repository.save(app);
        StepVerifier.create(result)
            .expectError()
            .verify();
    }
}
