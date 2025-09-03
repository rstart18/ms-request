package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.ioantype.LoanType;
import co.com.bancolombia.model.ioantype.gateways.IoanTypeRepository;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.repository.LoanReactiveRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepositoryAdapter extends ReactiveAdapterOperations<
    LoanType,
        LoanTypeEntity,
    Long,
    LoanReactiveRepository
> implements IoanTypeRepository {
    public LoanRepositoryAdapter(LoanReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }

}
