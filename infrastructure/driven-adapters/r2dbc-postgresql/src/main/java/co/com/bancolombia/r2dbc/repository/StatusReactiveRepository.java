package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.ApplicationEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveRepository extends ReactiveCrudRepository<ApplicationEntity, Long>, ReactiveQueryByExampleExecutor<ApplicationEntity> {

}
