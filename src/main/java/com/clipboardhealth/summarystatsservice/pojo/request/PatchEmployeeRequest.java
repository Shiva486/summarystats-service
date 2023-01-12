package com.clipboardhealth.summarystatsservice.pojo.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatchEmployeeRequest {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    private Long id;

    private String name;

    @PositiveOrZero
    private Integer salary;

    private String department;

    @JsonProperty("sub_department")
    private String subDepartment;

    @JsonProperty("on_contract")
    private Boolean onContract;
}
