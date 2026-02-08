

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeCont
{
    Map<Long,Employee> employeeMap= new HashMap<>();

    EmployeeCont()
    {
        employeeMap.put(1,new Employee(1,"IT"));
        employeeMap.put(1,new Employee(2,"Engineerinng"));
    }

    @GetMapping(/{id})
    public ResponseEntity<Employee>> getAllEmployee(@PathVariable int id)
    {
       Employee emp=employeeMap.get(id);
       if(emp!=null)
           return responseEntitiy.ok(emp);
       else
           return responseentity.notFound().build();

    }
    @PostMapping
    public ResponseEntity<Employee> CreateEmployee(@Valid @RequestBody Employee emp)
    {
        if(emp.getId()==null)
        {
            emp.setId("1");
        }
        employeeMap.put(emp.getId(),emp);
        return ResponseEntity.status(HTTPStatus.Created).body(emp);
    }
    private static class Employee
    {
        @NotNull("employee id shoud not be null")
        private int id;
        @NotNull("")
        private int depaartment;
        Employee(int id,int department)
        {
            this.id=id;
            this.department=department;
        }

        public int getDepaartment() {
            return depaartment;
        }

        public int getId() {
            return id;
        }

        public void setDepaartment(int depaartment) {
            this.depaartment = depaartment;
        }

        public void setId(int id) {
            this.id = id;
        }
        @override
        public String toString()
        {
            return "id= " + id  + ", department= "+ department;
        }
    }
}
