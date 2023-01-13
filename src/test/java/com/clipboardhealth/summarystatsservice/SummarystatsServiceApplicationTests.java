package com.clipboardhealth.summarystatsservice;

import com.clipboardhealth.summarystatsservice.enums.Currency;
import com.clipboardhealth.summarystatsservice.pojo.request.CreateEmployeeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@WebAppConfiguration
public class SummarystatsServiceApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void createEmployee() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test")
                                                                                .salary(1000000d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data.name").value("Test"));
    }

    @Test
    public void getEmployee() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test")
                                                                                .salary(1000000d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));

        mvc.perform(get("/employee").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data[0].name").isString());
    }

    @Test
    public void deleteEmployee() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test")
                                                                                .salary(1000000d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));

        mvc.perform(delete("/employee/1").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getSS() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test1")
                                                                                .salary(1d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test2")
                                                                                .salary(2d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Data")
                                                                                .onContract(true)
                                                                                .build())));

        mvc.perform(get("/employee/SS").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data.mean").isNumber());
    }

    @Test
    public void getOnContractSS() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test1")
                                                                                .salary(1d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test2")
                                                                                .salary(2d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Data")
                                                                                .onContract(false)
                                                                                .build())));

        mvc.perform(get("/employee/onContractSS").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data.mean").isNumber());
    }

    @Test
    public void getDepartmentWiseSS() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test1")
                                                                                .salary(1d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test2")
                                                                                .salary(4d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Data")
                                                                                .onContract(false)
                                                                                .build())));

        mvc.perform(get("/employee/departmentWiseSS").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data.Engineering.mean").isNumber());
    }

    @Test
    public void getSubDepartmentWiseSS() throws Exception {
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test1")
                                                                                .salary(1d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Software")
                                                                                .onContract(true)
                                                                                .build())));
        mvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON)
                                     .content(asJsonString(CreateEmployeeRequest.builder()
                                                                                .name("Test2")
                                                                                .salary(4d)
                                                                                .currency(Currency.USD)
                                                                                .department("Engineering")
                                                                                .subDepartment("Data")
                                                                                .onContract(false)
                                                                                .build())));

        mvc.perform(get("/employee/subDepartmentWiseSS").contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$.data.Engineering.Software.mean").isNumber());
    }

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
