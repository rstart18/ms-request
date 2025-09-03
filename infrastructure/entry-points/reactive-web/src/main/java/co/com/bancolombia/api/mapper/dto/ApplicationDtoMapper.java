package co.com.bancolombia.api.mapper.dto;

import co.com.bancolombia.api.dto.request.ApplicationRequest;
import co.com.bancolombia.api.dto.response.ApplicationResponse;
import co.com.bancolombia.model.application.Application;
import co.com.bancolombia.model.ioantype.LoanType;
import co.com.bancolombia.model.status.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApplicationDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "loanType", ignore = true)
    Application toDomain(ApplicationRequest request);

    @Mapping(target = "status", source = "status")
    @Mapping(source = "loanType.id", target = "loanTypeId")
    @Mapping(source = "loanType", target = "loanType")
    ApplicationResponse toResponse(Application domain);

    default ApplicationResponse.StatusResponse toStatusResponse(Status s) {
        if (s == null) return null;
        return ApplicationResponse.StatusResponse.builder()
            .id(s.getId())
            .name(s.getName())
            .build();
    }

    ApplicationResponse.LoanTypeResponse toLoanTypeResponse(co.com.bancolombia.model.ioantype.LoanType loanType);

    default LoanType map(Long loanTypeId) {
        return loanTypeId == null ? null : LoanType.builder().id(loanTypeId).build();
    }

    default Long map(LoanType loanType) {
        return loanType != null ? loanType.getId() : null;
    }
}
