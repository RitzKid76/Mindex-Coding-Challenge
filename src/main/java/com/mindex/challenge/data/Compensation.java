package com.mindex.challenge.data;

import java.time.LocalDate;
import java.util.Objects;

public class Compensation {
    private String employeeId;
    private int salary;
    private LocalDate effectiveDate;

    public Compensation() { }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    // The equals, hashCode, and toString functions were created for the CompensationServiceImplTest for easier comparison and feedback
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Compensation other))
            return false;
        
        return (
            employeeId.equals(other.employeeId) &&
            salary == other.salary &&
            effectiveDate.equals(other.effectiveDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, salary, effectiveDate);
    }

    @Override
    public String toString() {
        return (
            "{ employeeId: " + employeeId +
            ", salary: " + salary +
            ", effectiveDate: " + effectiveDate +
            " }"
        );
    }
}
