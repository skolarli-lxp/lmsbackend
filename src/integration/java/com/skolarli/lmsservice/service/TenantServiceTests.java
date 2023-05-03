package com.skolarli.lmsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skolarli.lmsservice.AbstractContainerBaseTest;
import com.skolarli.lmsservice.authentications.TenantAuthenticationToken;
import com.skolarli.lmsservice.exception.OperationNotSupportedException;
import com.skolarli.lmsservice.exception.ResourceNotFoundException;
import com.skolarli.lmsservice.models.db.Tenant;
import com.skolarli.lmsservice.services.TenantService;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TenantServiceTests extends AbstractContainerBaseTest {

    @Autowired
    TenantService tenantService;

    @Autowired
    private ObjectMapper mapper;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeAll
    public static void setUp(){
        container.withReuse(true);
        container.withInitScript("tenantservicetestdata.sql");
        container.start();
    }

    @BeforeEach
    public void setup() throws Exception {

    }

    @Test
    @Order(1)
    void newDomainTestSuccess() throws Exception {

        Tenant tenant = tenantService.getTenantByDomainName("domainName1");
        System.out.println(tenant);

    }

    @Test
    @Order(2)
    void getAllTenantSuccess(){
        List<Tenant> tenants = tenantService.getAllTenants();
        assert tenants.size() == 1;
        assert tenants.get(0).getDomainName().equals("domainName1");
    }

    @Test
    void getTenantById_Success(){
        Tenant tenant = tenantService.getTenantById(1);
        assert tenant.getDomainName().equals("domainName1");
    }

    @Test
    void getTenantById_Failure() {
        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class, () -> {
                    Tenant tenant = tenantService.getTenantById(2);
                });
        assertEquals("Tenant not found with Id : '2'", resourceNotFoundException.getMessage());
    }

    @Test
    void getTenantByDomainName_Success(){
        Tenant tenant = tenantService.getTenantByDomainName("domainName1");
        assert tenant.getId() == 1;
    }

    @Test
    void getTenantByDomainName_Failure() {
        ResourceNotFoundException resourceNotFoundException = assertThrows(
                ResourceNotFoundException.class, () -> {
                    Tenant tenant = tenantService.getTenantByDomainName("domainName2");
                });
        assertEquals("Tenant not found with Domain Name : 'domainName2'",
                    resourceNotFoundException.getMessage());
    }

    @Test
    void saveTenant_Success(){
        Tenant tenant = new Tenant();
        tenant.setDomainName("domainName3");
        tenant.setCompanyName("companyName2");
        tenant.setCurrency("INR");
        tenant.setAddress("address2");
        tenant.setCountryCode("+91");
        tenant.setPhoneNumber("1234567890");


        Tenant savedTenant = tenantService.saveTenant(tenant);
        assert savedTenant.getDomainName().equals("domainName3");
    }

    @Test
    void saveTenant_Failure(){
        Tenant tenant = new Tenant();
        tenant.setDomainName("domainName2");
        tenant.setCompanyName("companyName2");
        tenant.setCurrency("INR");
        tenant.setAddress("address2");
        tenant.setCountryCode("+91");
        tenant.setPhoneNumber("1234567890");

        DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class,
                () -> { tenantService.saveTenant(tenant);
        });
        assertThat(e.getMessage(), containsString("constraint [tenants.domainname]"));
    }

    @Test
    void saveTenant_Failure_IgnoreCase(){
        Tenant tenant = new Tenant();
        tenant.setDomainName("domainname2");
        tenant.setCompanyName("companyName2");
        tenant.setCurrency("INR");
        tenant.setAddress("address2");
        tenant.setCountryCode("+91");
        tenant.setPhoneNumber("1234567890");

        DataIntegrityViolationException e = assertThrows(
                DataIntegrityViolationException.class, () -> {
                    tenantService.saveTenant(tenant);
                });
        assertThat(e.getMessage(), containsString("constraint [tenants.domainname]"));
    }

    @Test
    void updateTenant_Success(){
        Tenant tenant = new Tenant();
        tenant.setDomainName("domainName1");
        tenant.setCompanyName("updatedCompanyame");

        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("tenantemail@domain.com", 1));

        Tenant updatedTenant = tenantService.updateTenant(tenant);
        assert updatedTenant.getCompanyName().equals("updatedCompanyame");
    }

    @Test
    void updateTenant_Failure() {
        Tenant tenant = new Tenant();
        tenant.setId(3);
        tenant.setDomainName("domainName1");
        tenant.setCompanyName("updatedCompanyame");

        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("tenantemail@domain.com", 1));

        OperationNotSupportedException e = assertThrows(
                OperationNotSupportedException.class, () -> {
                    Tenant updatedTenant = tenantService.updateTenant(tenant);
                });

        assertEquals("Cannot change tenant id", e.getMessage());
    }

    @Test
    void updateTenant_Failure_RestrictedName() {
        Tenant tenant = new Tenant();
        tenant.setDomainName("admin");
        tenant.setCompanyName("updatedCompanyame");

        SecurityContextHolder.getContext().setAuthentication(
                new TenantAuthenticationToken("tenantemail@domain.com", 1));

        OperationNotSupportedException e = assertThrows(OperationNotSupportedException.class,
                () -> { Tenant updatedTenant = tenantService.updateTenant(tenant);
        });

        assertEquals("Cannot update domainname for existing tenant", e.getMessage());
    }

    @Test
    void isUniqueDomainName_Success(){
        boolean isUnique = tenantService.isUniqueDomainName("domainName2");
        assert !isUnique;
    }

}
