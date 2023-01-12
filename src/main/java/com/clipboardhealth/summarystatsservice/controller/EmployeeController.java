package com.clipboardhealth.summarystatsservice.controller;

import com.clipboardhealth.summarystatsservice.pojo.request.CreateEmployeeRequest;
import com.clipboardhealth.summarystatsservice.pojo.request.PatchEmployeeRequest;
import com.clipboardhealth.summarystatsservice.pojo.response.Response;
import com.clipboardhealth.summarystatsservice.pojo.view.EmployeeView;
import com.clipboardhealth.summarystatsservice.pojo.view.SSView;
import com.clipboardhealth.summarystatsservice.service.EmployeeService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/employee", consumes = "application/json", produces = "application/json")
public class EmployeeController extends AbstractController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @Timed
    @Operation(description = "Create an employee record",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeView.class))))
    public Response<?> createEmployee(@RequestBody @Valid CreateEmployeeRequest createEmployeeRequest) {
        EmployeeView employeeView = employeeService.createEmployee(createEmployeeRequest);
        return getSuccessResponse(employeeView);
    }

    @RequestMapping(method = RequestMethod.PATCH)
    @Timed
    @Operation(description = "Patch update an employee record",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = EmployeeView.class))))
    public Response<?> patchUpdateEmployee(@RequestBody @Valid PatchEmployeeRequest patchEmployeeRequest) {
        EmployeeView employeeView = employeeService.patchUpdateEmployee(patchEmployeeRequest);
        return getSuccessResponse(employeeView);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @Timed
    @Operation(description = "Delete an employee record by id",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")))
    public Response<?> delete(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return getSuccessResponse(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Timed
    @Operation(description = "Get employees by filters",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeView.class)))))
    public Response<?> getByFilter(@RequestParam(value = "ids", required = false) List<Long> ids,
                                   @RequestParam(value = "names", required = false) List<String> names,
                                   @RequestParam(value = "departments", required = false) List<String> departments,
                                   @RequestParam(value = "subDepartments", required = false) List<String> subDepartments,
                                   @RequestParam(value = "onContract", required = false) Boolean onContract,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                   @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @RequestParam(value = "sortColumn", required = false) String sortColumn,
                                   @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        List<EmployeeView> employeeViewList = employeeService.getEmployeesByFilter(ids, names, departments,
                                                                                   subDepartments, onContract,
                                                                                   pageSize, pageNumber,
                                                                                   sortColumn, sortOrder);
        return getSuccessResponse(employeeViewList);
    }

    @RequestMapping(value = "/SS", method = RequestMethod.GET)
    @Timed
    @Operation(description = "Get summary statistics for all employees",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SSView.class))))
    public Response<?> getSS() {
        SSView ssView = employeeService.getSSByFilter(null, null, null, null, null);
        return getSuccessResponse(ssView);
    }

    @RequestMapping(value = "/onContractSS", method = RequestMethod.GET)
    @Timed
    @Operation(description = "Get summary statistics for on_contract=true employees",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SSView.class))))
    public Response<?> getOnContractSS() {
        SSView ssView = employeeService.getSSByFilter(null, null, null, null, true);
        return getSuccessResponse(ssView);
    }

    @RequestMapping(value = "/departmentWiseSS", method = RequestMethod.GET)
    @Timed
    @Operation(description = "Get summary statistics for all employees group by department",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SSView.class))))
    public Response<?> getDepartmentWiseSS() {
        Map<String, SSView> departmentWiseSSMap = employeeService.getSSByFilterGroupByDepartment(null, null, null, null, null);
        return getSuccessResponse(departmentWiseSSMap);
    }

    @RequestMapping(value = "/subDepartmentWiseSS", method = RequestMethod.GET)
    @Timed
    @Operation(description = "Get summary statistics for all employees group by department, sub_department",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
            responses = @ApiResponse(content = @Content(schema = @Schema(implementation = SSView.class))))
    public Response<?> getSubDepartmentWiseSS() {
        Map<String, Map<String, SSView>> subDepartmentWiseSSMap = employeeService.getSSByFilterGroupBySubDepartment(null, null, null, null, null);
        return getSuccessResponse(subDepartmentWiseSSMap);
    }
}
