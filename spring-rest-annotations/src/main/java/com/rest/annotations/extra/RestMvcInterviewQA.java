package com.rest.annotations.extra;

/**
 * Spring MVC / REST interview Q&A — under spring-rest-annotations project.
 *
 * <p><b>Q. What is REST?</b> Architectural style: resources identified by URIs, manipulated with HTTP verbs,
 * stateless, representations (often JSON). Real life: GET /orders/123 returns order JSON.
 *
 * <p><b>Q. HTTP methods?</b> GET safe/read, POST create, PUT replace, PATCH partial update, DELETE remove.
 * OPTIONS/HEAD for metadata. Real life: POST /payments initiates payment; PUT replaces full profile.
 *
 * <p><b>Q. PUT vs POST?</b> PUT idempotent full replace at known URI; POST often creates with server-assigned id
 * and is not required to be idempotent. Real life: PUT /users/1 replaces user 1; POST /users creates new.
 *
 * <p><b>Q. @RestController?</b> @Controller + @ResponseBody on class — return types serialize to HTTP body.
 *
 * <p><b>Q. @RequestMapping?</b> Maps URL + HTTP method to handler; specialized shortcuts @GetMapping etc.
 *
 * <p><b>Q. @PathVariable vs @RequestParam?</b> PathVariable binds /orders/{id}; RequestParam binds ?status=OPEN.
 * Real life: /orders/42 → PathVariable; filter query → RequestParam.
 *
 * <p><b>Q. Exception handling in Spring?</b> @ControllerAdvice + @ExceptionHandler for global mapping of
 * exceptions to HTTP status/problem details. Real life: map ConstraintViolationException → 400 JSON error.
 *
 * <p>See {@code com.rest.annotations.controller.EmployeeController} in this project for concrete mappings.
 */
public final class RestMvcInterviewQA {

    private RestMvcInterviewQA() {
    }

    public static void main(String[] args) {
        System.out.println("REST/MVC Q&A — open EmployeeController in this module for runnable Spring examples.");
    }
}
