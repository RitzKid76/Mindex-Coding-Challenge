package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    private String compensationListIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        compensationListIdUrl = "http://localhost:" + port + "/compensation/all/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        final String testEmployeeId = UUID.randomUUID().toString();
        final int testSalary = testEmployeeId.hashCode();
        final LocalDate testDate = LocalDate.parse("2025-01-31");

        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(testEmployeeId);
        testCompensation.setSalary(testSalary);
        testCompensation.setEffectiveDate(testDate);

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(createdCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, createdCompensation);

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, testEmployeeId).getBody();
        assertEquals(createdCompensation.getEmployeeId(), readCompensation.getEmployeeId());
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    @Test
    public void testListAndMostRecentEntryReturn() {
        final String testEmployeeId = UUID.randomUUID().toString();
        final int entriesToGenerate = 10;
        final LocalDate startDate = LocalDate.now().minusYears(entriesToGenerate);

        // Added quite a few comments below since this may appear to be pretty involved.

        // Generate random compensation entries under testEmployeeId and push them to the server
        List<Compensation> generatedEntries = new ArrayList<>();

        for (int i = 0; i < entriesToGenerate; i++) {
            int salary = i;
            LocalDate date = startDate.plusYears(i);

            Compensation generatedCompensationEntry = new Compensation();
            generatedCompensationEntry.setEmployeeId(testEmployeeId);
            generatedCompensationEntry.setSalary(salary);
            generatedCompensationEntry.setEffectiveDate(date);

            generatedEntries.add(generatedCompensationEntry);
        }

        // Save the most recent entry to be used later
        Compensation mostRecent = generatedEntries.get(entriesToGenerate - 1);

        // Scramble the entries order to prove that https://localhost:8080/compensation/{id} is not returning the top/bottom of the list
        Collections.shuffle(generatedEntries);

        for (Compensation compensation : generatedEntries) {
            restTemplate.postForEntity(compensationUrl, compensation, Compensation.class, testEmployeeId);
        }

        // Read the full list and check if each entry is present on the one sent to the server
        Compensation[] compensationList = restTemplate.getForEntity(compensationListIdUrl, Compensation[].class, testEmployeeId).getBody();
        for (Compensation compensation : compensationList) {
            assertTrue(
                "The compensation object in the list returned by the server " + compensation + " was not found in the previously generated list",
                generatedEntries.stream().anyMatch(
                    c -> c.equals(compensation)
                )
            );
        }

        // Use that saved top entry now by checking that the most recent entry is correct
        Compensation mostRecentCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, testEmployeeId).getBody();

        assertCompensationEquivalence(mostRecent, mostRecentCompensation);
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
