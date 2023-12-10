package com.home.prac.codingtest.hsbc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FindAverageSalaryApp {
    public void findAverageSalary(List<Employee> employees){

        Map<String, Map<String, Double>> result = new ConcurrentHashMap<>();

        /*result = employees.parallelStream()
                .collect(Collectors.groupingBy(Employee::getDesignation,
                        Collectors.groupingBy(Employee::getOfficeLocation,
                                Collectors.averagingDouble(Employee::getSalary))));

        result.forEach((designation, locationWiseAvgSalMap) -> {
            locationWiseAvgSalMap.forEach((location, avgSalary) ->
                    System.out.println(location + " --> " + designation + " --> " + avgSalary)
                    );
        });*/

        // parallel processing
        employees.parallelStream()
                .collect(Collectors.groupingByConcurrent(Employee::getOfficeLocation,
                        Collectors.groupingByConcurrent(Employee::getDesignation,
                                Collectors.averagingDouble(Employee::getSalary))))
                .forEach((ofcLocation, designationWiseAvgSal) ->
                        designationWiseAvgSal.forEach((designation, avgSal) ->
                                result.computeIfAbsent(ofcLocation, k -> new ConcurrentHashMap<>())
                                        .put(designation, avgSal)));

        result.forEach((ofcLocation, designationWiseAvgSal) ->
                        designationWiseAvgSal.forEach((designation, avgSalary) ->
                                System.out.println(ofcLocation + " --> " +
                                        designation + " --> " +
                                        BigDecimal.valueOf(avgSalary).setScale(2, BigDecimal.ROUND_HALF_UP))));
    }
}
