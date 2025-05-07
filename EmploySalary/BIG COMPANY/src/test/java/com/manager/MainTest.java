package com.manager;


import com.company.util.CsvReader;
import com.manager.DTO.Employe;
import com.manager.controller.EmployMain;
import com.manager.logic.FindSalaryDiff;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    	 @Test
    	    public void testMainMethodRunsSuccessfully() {
    	        // Arrange: Create dummy args (empty for now)
    	        String[] args = {};

    	        // Act & Assert: Ensure no exception is thrown during main() execution
    	        assertDoesNotThrow(() -> {
    	            EmployMain.main(args);
    	        });
    	    }
    }

 
