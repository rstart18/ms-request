package co.com.bancolombia.usecase.sendapplication;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.common.BusinessException;
import co.com.bancolombia.model.commons.BusinessException;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public class DefaultSendApplicationValidator implements SendApplicationValidator {

    // Valida formato básico de email
    private static final Pattern EMAIL_RX =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Override
    public Mono<Application> validateForCreate(Application input) {
        return Mono.justOrEmpty(input)
            .switchIfEmpty(Mono.error(
                BusinessException.invalidField("application", "no puede ser null")))
            .map(this::normalize)
            .flatMap(this::validateRules);
    }

    /** Normalizaciones no destructivas. */
    private Application normalize(Application a) {
        return a.toBuilder()
            .identityDocument(onlyDigits(trim(a.getIdentityDocument())))
            .email(lower(trim(a.getEmail())))
            // Si tienes teléfono en el modelo, podrías normalizarlo igual:
            // .phone(onlyDigits(trim(a.getPhone())))
            .build();
    }

    /** Reglas locales (sin acceder a gateways). */
    private Mono<Application> validateRules(Application a) {
        // Documento
        if (isBlank(a.getIdentityDocument()))
            return Mono.error(BusinessException.invalidField("identityDocument", "obligatorio"));

        // Email
        if (isBlank(a.getEmail()))
            return Mono.error(BusinessException.invalidField("email", "obligatorio"));
        if (!EMAIL_RX.matcher(a.getEmail()).matches())
            return Mono.error(BusinessException.invalidField("email", "formato inválido"));

        // Monto
        if (a.getAmount() == null)
            return Mono.error(BusinessException.invalidField("amount", "obligatorio"));
        if (a.getAmount() <= 0)
            return Mono.error(BusinessException.invalidField("amount", "debe ser mayor a 0"));

        // Plazo
        if (a.getApplicationDate() == null)
            return Mono.error(BusinessException.invalidField("term", "obligatorio"));

        // Tipo de préstamo
        if (a.getLoanTypeId() == null)
            return Mono.error(BusinessException.invalidField("loanTypeId", "obligatorio"));

        // Nota: las validaciones contra el LoanType (rangos min/max de monto, etc.)
        // se hacen en el UseCase, consultando el gateway correspondiente.

        return Mono.just(a);
    }

    // ---- Helpers de normalización ----
    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static String lower(String s) { return s == null ? null : s.toLowerCase(); }
    private static String onlyDigits(String s) { return s == null ? null : s.replaceAll("\\D+", ""); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
