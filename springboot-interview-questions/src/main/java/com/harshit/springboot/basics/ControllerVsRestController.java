package com.harshit.springboot.basics;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Controller vs @RestController - Complete Comparison
 * 
 * Understanding the difference between @Controller and @RestController is fundamental
 * for Spring Boot interviews. Both annotations mark a class as a Spring-managed component,
 * but they serve different purposes and are used in different types of applications.
 * 
 * @Controller is used in MVC applications that return views (HTML, JSP, Thymeleaf, etc.).
 * It uses ViewResolver to render UI pages. When you return a String from a @Controller
 * method, Spring interprets it as a view name and looks for a corresponding view template
 * to render. If you want to return JSON from a @Controller, you need to use @ResponseBody
 * annotation on the method.
 * 
 * @RestController is a combination of @Controller and @ResponseBody. It's used in REST
 * APIs where you want to return JSON or XML directly. When you return an object from a
 * @RestController method, Spring automatically serializes it to JSON using HttpMessageConverters
 * like Jackson. You don't need @ResponseBody on each method because @RestController
 * applies it at the class level.
 */
@Controller
@RequestMapping("/web")
class WebController {
    
    /**
     * @Controller EXAMPLE - Returns View Name
     * 
     * This method returns a String, which Spring interprets as a view name.
     * Spring uses ViewResolver to find and render the corresponding view template
     * (like home.html or home.jsp). This is the typical pattern for web applications
     * that serve HTML pages.
     */
    @GetMapping("/home")
    public String home() {
        // Interview Point: Returns view name, not JSON
        // Spring looks for home.html or home.jsp to render
        return "home";  // View name, not JSON
    }
    
    /**
     * @Controller EXAMPLE - Returns ModelAndView
     * 
     * You can also return ModelAndView to pass data to the view along with the view name.
     * This is useful when you need to pass model attributes to the view template.
     */
    @GetMapping("/products")
    public ModelAndView getProducts() {
        ModelAndView modelAndView = new ModelAndView("products");
        modelAndView.addObject("products", java.util.Arrays.asList("Product1", "Product2"));
        return modelAndView;
    }
    
    /**
     * @Controller EXAMPLE - Returns JSON with @ResponseBody
     * 
     * If you want to return JSON from a @Controller, you need to use @ResponseBody
     * annotation. This tells Spring to serialize the return value to JSON instead of
     * treating it as a view name.
     */
    @GetMapping("/api/data")
    @ResponseBody  // Required to return JSON from @Controller
    public ResponseEntity<Map<String, String>> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Data from @Controller with @ResponseBody");
        return ResponseEntity.ok(data);
    }
}

/**
 * @RestController EXAMPLE - Automatically Returns JSON
 * 
 * @RestController is a convenience annotation that combines @Controller and @ResponseBody.
 * All methods in a @RestController automatically return JSON (or XML) without needing
 * @ResponseBody on each method. This makes it perfect for REST APIs.
 */
@RestController
@RequestMapping("/api")
class RestApiController {
    
    /**
     * REST API EXAMPLE
     * 
     * This method automatically returns JSON. Spring uses HttpMessageConverters (like
     * Jackson) to serialize the ResponseEntity to JSON. No @ResponseBody needed because
     * @RestController applies it at the class level.
     */
    @PostMapping("/products")
    public ResponseEntity<Void> addProduct(@RequestBody ProductDTO productDTO) {
        // Interview Point: Automatically returns JSON
        // @ResponseBody is not needed - @RestController handles it
        // @RequestBody deserializes JSON request body to ProductDTO
        productService.addProduct(productDTO);
        return ResponseEntity.status(201).build();
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
}

// Helper classes
class ProductService {
    public void addProduct(ProductDTO product) {
        // Add product logic
    }
    
    public ProductDTO getProductById(Long id) {
        return new ProductDTO(id, "Product " + id);
    }
}

class ProductDTO {
    private Long id;
    private String name;
    
    public ProductDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

