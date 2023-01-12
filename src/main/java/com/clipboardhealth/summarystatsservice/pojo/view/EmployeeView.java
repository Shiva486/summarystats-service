package com.clipboardhealth.summarystatsservice.pojo.view;

import com.clipboardhealth.summarystatsservice.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeView {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
    private Date updatedAt;

    private String name;

    private Integer salary;

    private Currency currency;

    private String department;

    @JsonProperty("sub_department")
    private String subDepartment;

    @JsonProperty("on_contract")
    private Boolean onContract;
}
