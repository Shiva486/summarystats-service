package com.clipboardhealth.summarystatsservice.repository;

import com.clipboardhealth.summarystatsservice.entity.Employee;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>, QuerydslPredicateExecutor<Employee> {

    @Override
    List<Employee> findAll(Predicate predicate);
}
