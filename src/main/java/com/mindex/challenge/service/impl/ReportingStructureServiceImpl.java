package com.mindex.challenge.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Creating reportingStructure with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null)
            throw new RuntimeException("Invalid employeeId: " + id);

        int numberOfReports = countNumberOfReports(employee) - 1;
        // removing one since this function always includes the caller. would be silly to include the employee as a reporter to themself
        
        return new ReportingStructure(employee, numberOfReports);
    }

    private int countNumberOfReports(Employee employee) {
        List<Employee> directReports = employee.getDirectReports();

        int totalReports = 1; // count this employee

        if (directReports == null)
            return totalReports; 

        for (Employee directReport : directReports) {
            Employee filledDirectReport = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
            totalReports += countNumberOfReports(filledDirectReport);
        }

        return totalReports;
    }
}
