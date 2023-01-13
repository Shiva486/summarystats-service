package com.clipboardhealth.summarystatsservice.service;

import com.clipboardhealth.summarystatsservice.entity.Employee;
import com.clipboardhealth.summarystatsservice.entity.QEmployee;
import com.clipboardhealth.summarystatsservice.exception.CustomException;
import com.clipboardhealth.summarystatsservice.exception.ErrorCodes;
import com.clipboardhealth.summarystatsservice.mapper.EmployeeMapper;
import com.clipboardhealth.summarystatsservice.pojo.request.CreateEmployeeRequest;
import com.clipboardhealth.summarystatsservice.pojo.request.PatchEmployeeRequest;
import com.clipboardhealth.summarystatsservice.pojo.view.EmployeeView;
import com.clipboardhealth.summarystatsservice.pojo.view.SSView;
import com.clipboardhealth.summarystatsservice.repository.EmployeeRepository;
import com.clipboardhealth.summarystatsservice.utils.CommonFunctions;
import com.clipboardhealth.summarystatsservice.utils.NullAwareBeanUtils;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class EmployeeService {

    @PersistenceContext
    private EntityManager entityManager;

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeMapper employeeMapper,
                           EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EmployeeView createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        Employee employee = new Employee();
        NullAwareBeanUtils.copyProperties(createEmployeeRequest, employee);

        employee = employeeRepository.save(employee);
        EmployeeView employeeView = new EmployeeView();
        employeeMapper.entityToView(employee, employeeView);

        return employeeView;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EmployeeView patchUpdateEmployee(PatchEmployeeRequest patchEmployeeRequest) {
        Optional<Employee> employeeOptional = employeeRepository.findById(patchEmployeeRequest.getId());

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            NullAwareBeanUtils.copyPropertiesWithoutNull(patchEmployeeRequest, employee);

            employee = employeeRepository.save(employee);
            EmployeeView employeeView = new EmployeeView();
            employeeMapper.entityToView(employee, employeeView);

            return employeeView;
        }
        else
            throw new CustomException(ErrorCodes.NO_ENTITY_FOUND);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeView> getEmployeesByFilter(List<Long> ids, List<String> names, List<String> departments,
                                                   List<String> subDepartments, Boolean onContract, Integer pageSize,
                                                   Integer pageNumber, String sortColumn, String sortOrder) {
        BooleanExpression booleanExpression = this.getBooleanExpression(ids, names, departments, subDepartments, onContract);
        Pageable pageable = CommonFunctions.getPageable(pageSize, pageNumber, sortColumn, sortOrder);

        List<Employee> employeeList = pageable == null ?
                employeeRepository.findAll(booleanExpression)
                : employeeRepository.findAll(booleanExpression, pageable).getContent();

        List<EmployeeView> employeeViewList = new ArrayList<>();
        for (Employee employee: employeeList) {
            EmployeeView employeeView = new EmployeeView();
            employeeMapper.entityToView(employee, employeeView);
            employeeViewList.add(employeeView);
        }

        return employeeViewList;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public SSView getSSByFilter(List<Long> ids, List<String> names, List<String> departments,
                                List<String> subDepartments, Boolean onContract) {
        JPAQuery<Employee> employeeJpaQuery = new JPAQuery<>(entityManager);
        QEmployee qEmployee = QEmployee.employee;
        BooleanExpression booleanExpression = this.getBooleanExpression(ids, names, departments, subDepartments, onContract);

        Double maxSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression).select(qEmployee.salary.max()).fetchOne();
        Double minSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression).select(qEmployee.salary.min()).fetchOne();
        Double avgSalary =  employeeJpaQuery.from(qEmployee).where(booleanExpression).select(qEmployee.salary.avg()).fetchOne();

        return SSView.builder()
                     .max(Optional.ofNullable(maxSalary).orElse(0d))
                     .min(Optional.ofNullable(minSalary).orElse(0d))
                     .mean(Optional.ofNullable(avgSalary).orElse(0d))
                     .build();
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map<String, SSView> getSSByFilterGroupByDepartment(List<Long> ids, List<String> names, List<String> departments,
                                                              List<String> subDepartments, Boolean onContract) {
        JPAQuery<Employee> employeeJpaQuery = new JPAQuery<>(entityManager);
        QEmployee qEmployee = QEmployee.employee;
        BooleanExpression booleanExpression = this.getBooleanExpression(ids, names, departments, subDepartments, onContract);

        Map<String, Double> departmentMaxSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                  .transform(GroupBy.groupBy(qEmployee.department).as(GroupBy.max(qEmployee.salary)));
        Map<String, Double> departmentMinSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                  .transform(GroupBy.groupBy(qEmployee.department).as(GroupBy.min(qEmployee.salary)));
        Map<String, Double> departmentAvgSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                  .transform(GroupBy.groupBy(qEmployee.department).as(GroupBy.avg(qEmployee.salary)));

        Map<String, SSView> departmentWiseSSMap = new HashMap<>();
        departmentMaxSalary.forEach((key, value) -> this.populateMaxValueInMap(departmentWiseSSMap, key, value));
        departmentMinSalary.forEach((key, value) -> this.populateMinValueInMap(departmentWiseSSMap, key, value));
        departmentAvgSalary.forEach((key, value) -> this.populateMeanValueInMap(departmentWiseSSMap, key, value));

        return departmentWiseSSMap;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map<String, Map<String, SSView>> getSSByFilterGroupBySubDepartment(List<Long> ids, List<String> names, List<String> departments,
                                                                              List<String> subDepartments, Boolean onContract) {
        JPAQuery<Employee> employeeJpaQuery = new JPAQuery<>(entityManager);
        QEmployee qEmployee = QEmployee.employee;
        BooleanExpression booleanExpression = this.getBooleanExpression(ids, names, departments, subDepartments, onContract);

        Map<List<?>, Double> departmentMaxSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                   .transform(GroupBy.groupBy(qEmployee.department, qEmployee.subDepartment).as(GroupBy.max(qEmployee.salary)));
        Map<List<?>, Double> departmentMinSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                   .transform(GroupBy.groupBy(qEmployee.department, qEmployee.subDepartment).as(GroupBy.min(qEmployee.salary)));
        Map<List<?>, Double> departmentAvgSalary = employeeJpaQuery.from(qEmployee).where(booleanExpression)
                                                                   .transform(GroupBy.groupBy(qEmployee.department, qEmployee.subDepartment).as(GroupBy.avg(qEmployee.salary)));

        Map<String, Map<String, SSView>> subDepartmentWiseSSMap = new HashMap<>();
        departmentMaxSalary.forEach((key, value) -> this.populateMaxValueInNestedMap(subDepartmentWiseSSMap, key.get(0).toString(), key.get(1).toString(), value));
        departmentMinSalary.forEach((key, value) -> this.populateMinValueInNestedMap(subDepartmentWiseSSMap, key.get(0).toString(), key.get(1).toString(), value));
        departmentAvgSalary.forEach((key, value) -> this.populateMeanValueInNestedMap(subDepartmentWiseSSMap, key.get(0).toString(), key.get(1).toString(), value));

        return subDepartmentWiseSSMap;
    }

    private BooleanExpression getBooleanExpression(List<Long> ids, List<String> names, List<String> departments,
                                                   List<String> subDepartments, Boolean onContract) {
        QEmployee qEmployee = QEmployee.employee;

        BooleanExpression booleanExpression = Expressions.asBoolean(true).isTrue();

        if (!CollectionUtils.isEmpty(ids)) {
            booleanExpression = booleanExpression.and(qEmployee.id.in(ids));
        }

        if (onContract != null) {
            booleanExpression = booleanExpression.and(qEmployee.onContract.eq(onContract));
        }

        if (!CollectionUtils.isEmpty(names)) {
            List<BooleanExpression> nameBooleanExpressions = new ArrayList<>();
            for (String name: names) {
                nameBooleanExpressions.add(Expressions.asBoolean(true).isTrue()
                                                      .and(qEmployee.name.equalsIgnoreCase(name)));
            }

            booleanExpression.andAnyOf(nameBooleanExpressions.toArray(new BooleanExpression[0]));
        }

        if (!CollectionUtils.isEmpty(departments)) {
            List<BooleanExpression> departmentBooleanExpressions = new ArrayList<>();
            for (String department: departments) {
                departmentBooleanExpressions.add(Expressions.asBoolean(true).isTrue()
                                                            .and(qEmployee.department.equalsIgnoreCase(department)));
            }

            booleanExpression.andAnyOf(departmentBooleanExpressions.toArray(new BooleanExpression[0]));
        }

        if (!CollectionUtils.isEmpty(subDepartments)) {
            List<BooleanExpression> subDepartmentBooleanExpressions = new ArrayList<>();
            for (String subDepartment: subDepartments) {
                subDepartmentBooleanExpressions.add(Expressions.asBoolean(true).isTrue()
                                                               .and(qEmployee.subDepartment.equalsIgnoreCase(subDepartment)));
            }

            booleanExpression.andAnyOf(subDepartmentBooleanExpressions.toArray(new BooleanExpression[0]));
        }

        return booleanExpression;
    }

    private void populateMaxValueInMap(Map<String, SSView> ssViewMap, String department, Double value) {
        if (ssViewMap.get(department) == null) {
            ssViewMap.put(department, SSView.builder().max(Optional.ofNullable(value).orElse(0d)).build());
        } else {
            ssViewMap.get(department).setMax(Optional.ofNullable(value).orElse(0d));
        }
    }

    private void populateMinValueInMap(Map<String, SSView> departmentSSViewMap, String department, Double value) {
        if (departmentSSViewMap.get(department) == null) {
            departmentSSViewMap.put(department, SSView.builder().min(Optional.ofNullable(value).orElse(0d)).build());
        } else {
            departmentSSViewMap.get(department).setMin(Optional.ofNullable(value).orElse(0d));
        }
    }

    private void populateMeanValueInMap(Map<String, SSView> departmentSSViewMap, String department, Double value) {
        if (departmentSSViewMap.get(department) == null) {
            departmentSSViewMap.put(department, SSView.builder().mean(Optional.ofNullable(value).orElse(0d)).build());
        } else {
            departmentSSViewMap.get(department).setMean(Optional.ofNullable(value).orElse(0d));
        }
    }

    private void populateMaxValueInNestedMap(Map<String, Map<String, SSView>> departmentSubDepartmentSSViewMap,
                                             String department, String subDepartment, Double value) {
        if (departmentSubDepartmentSSViewMap.get(department) == null) {
            Map<String, SSView> subDepartmentViewMap = new HashMap<>();
            this.populateMaxValueInMap(subDepartmentViewMap, subDepartment, value);
            departmentSubDepartmentSSViewMap.put(department, subDepartmentViewMap);
        }
        else {
            this.populateMaxValueInMap(departmentSubDepartmentSSViewMap.get(department), subDepartment, value);
        }
    }

    private void populateMinValueInNestedMap(Map<String, Map<String, SSView>> departmentSubDepartmentSSViewMap,
                                             String department, String subDepartment, Double value) {
        if (departmentSubDepartmentSSViewMap.get(department) == null) {
            Map<String, SSView> subDepartmentViewMap = new HashMap<>();
            this.populateMinValueInMap(subDepartmentViewMap, subDepartment, value);
            departmentSubDepartmentSSViewMap.put(department, subDepartmentViewMap);
        }
        else {
            this.populateMinValueInMap(departmentSubDepartmentSSViewMap.get(department), subDepartment, value);
        }
    }

    private void populateMeanValueInNestedMap(Map<String, Map<String, SSView>> departmentSubDepartmentSSViewMap,
                                              String department, String subDepartment, Double value) {
        if (departmentSubDepartmentSSViewMap.get(department) == null) {
            Map<String, SSView> subDepartmentViewMap = new HashMap<>();
            this.populateMeanValueInMap(subDepartmentViewMap, subDepartment, value);
            departmentSubDepartmentSSViewMap.put(department, subDepartmentViewMap);
        }
        else {
            this.populateMeanValueInMap(departmentSubDepartmentSSViewMap.get(department), subDepartment, value);
        }
    }
}
