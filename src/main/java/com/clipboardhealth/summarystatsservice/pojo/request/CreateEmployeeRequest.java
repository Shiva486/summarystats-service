package com.clipboardhealth.summarystatsservice.pojo.request;

import com.clipboardhealth.summarystatsservice.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @PositiveOrZero
    private Double salary;

    private Currency currency = Currency.USD;

    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String department;

    @JsonProperty("sub_department")
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String subDepartment;

    @JsonProperty("on_contract")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean onContract = false;
}
