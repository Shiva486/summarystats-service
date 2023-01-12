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
import com.querydsl.jpa.JPAExpressions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class EmployeeService {

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

    public SSView getSSByFilter(List<Long> ids, List<String> names, List<String> departments,
                                List<String> subDepartments, Boolean onContract) {
        QEmployee employee = QEmployee.employee;

        BooleanExpression booleanExpression = this.getBooleanExpression(ids, names, departments, subDepartments, onContract);
        Map<Employee, Integer> maxSalary = JPAExpressions.selectFrom(employee).where(booleanExpression).transform(GroupBy.groupBy(employee).as(GroupBy.max(employee.salary)));
        Map<Employee, Integer> minSalary = JPAExpressions.selectFrom(employee).where(booleanExpression).transform(GroupBy.groupBy(employee).as(GroupBy.min(employee.salary)));
        Map<Employee, Integer> avgSalary = JPAExpressions.selectFrom(employee).where(booleanExpression).transform(GroupBy.groupBy(employee).as(GroupBy.avg(employee.salary)));

        return SSView.builder()
                     .build();
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
}
