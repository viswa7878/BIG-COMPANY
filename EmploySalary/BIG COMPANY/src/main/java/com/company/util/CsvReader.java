package com.company.util;

import com.manager.DTO.Employe;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads employee data from a CSV file and returns a list of Employe objects.
 * Also builds the manager-subordinate relationships based on manager IDs.
 *
 * @param filePath The full path to the CSV file
 * @return List of populated Employe objects with hierarchical structure
 * @throws IOException If file reading fails
 */

public class CsvReader {

    public static List<Employe> readEmployees(String filePath) throws IOException {
  	    //System.out.println("    ******   File Reading Process Started  ******");
        List<Employe> employees = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip the header line assuming the first line contains column names
        br.readLine();

        // Read each line from the CSV file
        while ((line = br.readLine()) != null) {
            line = line.trim();

            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }

            // Split the line into fields using comma as delimiter
            String[] parts = line.split(",");

            // Clean each field: remove leading/trailing spaces and quotes
            for (int i = 0; i < parts.length; i++) {
                parts[i] = parts[i].trim().replace("\"", "");
            }

            try {
                // Parse employee attributes
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                double salary = Double.parseDouble(parts[3]);
                Integer managerId = (parts.length > 4 && !parts[4].isEmpty())? Integer.parseInt(parts[4]): 0; // Default to 0 if no manager ID

                // Create and add employee object to the list
                Employe employee = new Employe(id, firstName, lastName, salary, managerId);
                employees.add(employee);
            } catch (NumberFormatException e) {
                // Handle parsing errors gracefully and log the issue
                System.err.println("Error parsing line: " + line + ". Skipping this entry.");
            }
        }

        // Build a map for quick employee lookup by employee ID
        Map<Integer, Employe> employeeMap = employees.stream()
                                                     .collect(Collectors.toMap(Employe::getEmpId, e -> e));

        // Establish reporting relationships (manager to subordinates)
        for (Employe emp : employees) {
            if (emp.getManagerId() != 0) {
                Employe manager = employeeMap.get(emp.getManagerId());
                if (manager != null) {
                    manager.setSubordinates(emp); // Add this employee to manager's subordinates
                }
            }
        }

        br.close(); // Close the file reader
    	//System.out.println("    ******   File Reading Process Ended  ******");
        return employees; // Return the final list of employees
    }
}
