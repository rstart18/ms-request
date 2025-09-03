package co.com.bancolombia.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ApplicationResponse {
    Long id;
    Integer amount;
    Integer term;
    String email;
    String identityDocument;
    Long loanTypeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDateTime applicationDate;

    StatusResponse status;
    LoanTypeResponse loanType;

    @Value
    @Builder
    public static class StatusResponse {
        Integer id;
        String name;
    }

    @Value
    @Builder
    public static class LoanTypeResponse {
        Long id;
        String name;
    }
}
