package com.clipboardhealth.summarystatsservice.entity;

import com.clipboardhealth.summarystatsservice.enums.Currency;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "employee")
@Table(name = "employee", indexes = {
        @Index(name = "name_idx", columnList = "name"),
        @Index(name = "salary_idx", columnList = "salary"),
        @Index(name = "department_idx", columnList = "department"),
        @Index(name = "sub_department_idx", columnList = "sub_department"),
        @Index(name = "on_contract_idx", columnList = "on_contract")
})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "salary", nullable = false)
    private Integer salary;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code", nullable = false)
    private Currency currency = Currency.USD;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "sub_department", nullable = false)
    private String subDepartment;

    @Column(name = "on_contract", nullable = false)
    private Boolean onContract = false;
}
