package co.com.bancolombia.usecase.sendapplication;

import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.common.BusinessException;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

public class DefaultSendApplicationValidator implements SendApplicationValidator {

    // Valida formato básico de email
    private static final Pattern EMAIL_RX =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final int MIN_AMOUNT = 1;
    private static final int MIN_TERM   = 1;
    private static final int MAX_TERM   = 120;
    private static final int MIN_DOC_LEN = 5;
    private static final int MAX_DOC_LEN = 20;

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
            .build();
    }

    /** Reglas locales (sin acceder a gateways). */
    private Mono<Application> validateRules(Application a) {
        // identityDocument
        if (isBlank(a.getIdentityDocument()))
            return Mono.error(BusinessException.invalidField("identityDocument", "obligatorio"));
        if (a.getIdentityDocument().length() < MIN_DOC_LEN || a.getIdentityDocument().length() > MAX_DOC_LEN)
            return Mono.error(BusinessException.invalidField("identityDocument",
                "longitud inválida (" + MIN_DOC_LEN + ".." + MAX_DOC_LEN + ")"));

        // email
        if (isBlank(a.getEmail()))
            return Mono.error(BusinessException.invalidField("email", "obligatorio"));
        if (!EMAIL_RX.matcher(a.getEmail()).matches())
            return Mono.error(BusinessException.invalidField("email", "formato inválido"));

        // amount
        if (a.getAmount() == null)
            return Mono.error(BusinessException.invalidField("amount", "obligatorio"));
        if (a.getAmount() < MIN_AMOUNT)
            return Mono.error(BusinessException.invalidField("amount", "debe ser mayor a 0"));

        // term (plazo) -> ¡clave para no violar NOT NULL en DB!
        if (a.getTerm() == null)
            return Mono.error(BusinessException.invalidField("term", "obligatorio"));
        if (a.getTerm() < MIN_TERM || a.getTerm() > MAX_TERM)
            return Mono.error(BusinessException.invalidField("term",
                "fuera de rango [" + MIN_TERM + ".." + MAX_TERM + "]"));

        // loanTypeId
        if (a.getLoanTypeId() == null)
            return Mono.error(BusinessException.invalidField("loanTypeId", "obligatorio"));
        if (a.getLoanTypeId() <= 0)
            return Mono.error(BusinessException.invalidField("loanTypeId", "debe ser > 0"));

        // applicationDate: NO lo exijas; el use case lo setea a now()
        return Mono.just(a);
    }

    // ---- Helpers de normalización ----
    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static String lower(String s) { return s == null ? null : s.toLowerCase(); }
    private static String onlyDigits(String s) { return s == null ? null : s.replaceAll("\\D+", ""); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
