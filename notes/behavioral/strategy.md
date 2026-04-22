
### A. Factory (The "Creation" Phase)

-   **Focus:** How do I get an instance?
    
-   **Role:** It hides the `new` keyword. Instead of your main code having a messy `if-else` block to decide which sorter to create, you delegate that "mess" to the Factory.
    
-   **Ex:** `SortFactory.getComparator("name")`.

```java
class SortFactory {
    public static Comparator<Employee> getComparator(String type) {
        if (type.equalsIgnoreCase("name")) return new NameComparator();
        if (type.equalsIgnoreCase("sal")) return new SalaryAdapter(); // Returns Adapter
        throw new IllegalArgumentException("Unknown type");
    }
}
```

### B. Strategy (The "Action" Phase)

-   **Focus:** How do I do the work?
    
-   **Role:** It defines the **Common Interface** (`Comparator`). The `List.sort()` method is the "Context." It doesn't care if it's sorting by name or salary; it just calls `.compare()`. The "Strategy" is the specific logic inside that method.
    
-   **Ex:** `employees.sort(nameComparator)`.
```java
// 1. STRATEGIES (Standard behaviors)
class NameComparator implements Comparator<Employee> {
    public int compare(Employee e1, Employee e2) {
        return e1.name.compareTo(e2.name);
    }
}
```
    

### C. Adapter (The "Conversion" Phase)

-   **Focus:** How do I make this work with my interface?
    
-   **Role:** It acts as a wrapper. You use an Adapter when you have a piece of code (like `LegacyScientificSorter`) that is useful but doesn't implement the `Comparator` interface. The Adapter "translates" your request.
    
-   **Ex:** The `SalaryAdapter` class.
```java
//  THE ADAPTEE (A legacy class that doesn't "fit")
class LegacyScientificSorter {
    public int doComplexSalSort(double a, double b) {
        return Double.compare(a, b);
    }
}

//  THE ADAPTER (Makes LegacyScientificSorter look like a Comparator)
class SalaryAdapter implements Comparator<Employee> {
    private LegacyScientificSorter legacy = new LegacyScientificSorter();
    public int compare(Employee e1, Employee e2) {
        return legacy.doComplexSalSort(e1.sal, e2.sal);
    }
}
```