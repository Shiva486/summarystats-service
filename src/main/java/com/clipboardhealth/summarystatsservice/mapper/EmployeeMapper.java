package com.clipboardhealth.summarystatsservice.mapper;

import com.clipboardhealth.summarystatsservice.entity.Employee;
import com.clipboardhealth.summarystatsservice.pojo.view.EmployeeView;
import com.clipboardhealth.summarystatsservice.utils.NullAwareBeanUtils;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper implements AbstractMapper<Employee, EmployeeView> {

    @Override
    public void entityToView(Employee employee, EmployeeView employeeView) {
        if (employee != null)
            NullAwareBeanUtils.copyPropertiesWithoutNull(employee, employeeView);
    }
}
