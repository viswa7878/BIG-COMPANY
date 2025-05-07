package com.manager.logic;

import com.manager.DTO.Employe;

import java.util.*;
/**
 * Main method to perform analysis on employee data.
 * Includes salary benchmarking and reporting hierarchy evaluation.
 */
public class FindSalaryDiff {

    public static void analyze(List<Employe> employees)
    {
        validateManagerSalaries(employees);
        validateReportingDepth(employees); 
    }

    /**
     * Validates if each manager's salary falls within the allowed range
     * compared to the average salary of their direct subordinates.
     * The acceptable range is 20% to 50% more than the average.
     */
    private static void validateManagerSalaries(List<Employe> employees) {
    	System.out.println("    ******   Manager Salary Validation Started ******");

        for (Employe manager : employees) {
            List<Employe> subordinates = manager.getSubordinates();

            // Skip if no direct reports
            if (subordinates == null || subordinates.isEmpty()) continue;

            // Calculate average salary of subordinates
            double avgSubSalary = subordinates.stream()
                                              .mapToDouble(Employe::getSalary)
                                              .average()
                                              .orElse(0.0);

         // Based on the problem statement, a manager's salary should be 20% more than the average salary of their direct subordinates.
         // That means: average salary + 20% of average salary = 1.2 × average salary.
         // So, we multiply the average subordinate salary by 1.2 to get the minimum required manager salary.

            double lowerBound = avgSubSalary * 1.2;
            double upperBound = avgSubSalary * 1.5;
            double managerSalary = manager.getSalary();

            if (managerSalary < lowerBound) {
                System.out.printf("Manager %d earns below threshold. Should increase by %.2f (min %.2f).\n",manager.getEmpId(), lowerBound - managerSalary, lowerBound);
            } else if (managerSalary > upperBound) {
                System.out.printf("Manager %d exceeds salary cap. Consider reducing by %.2f (max %.2f).\n",manager.getEmpId(), managerSalary - upperBound, upperBound);
            }
        }
    	System.out.println("    ******   Manager Salary Validation Ended ******");
    }

    /**
     * Identifies employees whose reporting path to the CEO
     * exceeds the allowed depth (more than 4 levels).
     */
    private static void validateReportingDepth(List<Employe> employees) {
    	System.out.println("    ******   Deep Reporting Structure Check Started  ******");

        // Map for fast employee ID lookup
        Map<Integer, Employe> empLookup = new HashMap<>();
        for (Employe emp : employees) {
            empLookup.put(emp.getEmpId(), emp);
        }

        for (Employe emp : employees) {
            int chainLength = 0;
            Integer currentManagerId = emp.getManagerId();

            // Traverse up the hierarchy to count depth
            while (currentManagerId != null && currentManagerId != 0) {
                chainLength++;
                Employe manager = empLookup.get(currentManagerId);
                if (manager == null) break;
                currentManagerId = manager.getManagerId();
            }

            if (chainLength > 4) {
                System.out.printf("Employee %d exceeds reporting depth: %d levels (allowed: 4).\n",emp.getEmpId(), chainLength);
            }
        }
    	System.out.println("    ******   Deep Reporting Structure Check Ended ******");

    }
}
