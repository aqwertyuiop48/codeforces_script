/*
@Test → Marks a test method.
@BeforeAll → Runs once before all tests in the class.
@AfterAll → Runs once after all tests in the class.
@BeforeEach → Runs before each test.
@AfterEach → Runs after each test.
@Disabled → Marks a test as skipped (Jenkins will report it as skipped).
@DisplayName → Provides a human-readable test name in reports.
@Tag → Categorizes tests (Jenkins can filter by tags).
@RepeatedTest → Repeats a test multiple times.
@ParameterizedTest (with @ValueSource, @CsvSource, etc.) → Runs the same logic with different inputs.
@Timeout → Fails if execution takes too long.
 */


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Jenkins will respect order in reports
public class CalculatorTest {

    @Test
    public void testAddition_MixedNumbers() {
        double result = Calculator.add(-10, 5);
        assertEquals(-5, result, "-10 + 5 should equal -5");
    }
    
    @Test
    public void testSubtraction_PositiveNumbers() {
        double result = Calculator.subtract(10, 5);
        assertEquals(5, result, "10 - 5 should equal 5");
    }

    @Test
    public void testSubtraction_NegativeNumbers() {
        double result = Calculator.subtract(-10, -5);
        assertEquals(-5, result, "-10 - (-5) should equal -5");
    }
    
    @Test
    public void testMultiplication() {
        double result = Calculator.multiply(10, 5);
        assertEquals(50, result, "10 * 5 should equal 50");
    }

    @Test
    public void testMultiplication_WithZero() {
        double result = Calculator.multiply(10, 0);
        assertEquals(0, result, "10 * 0 should equal 0");
    }

    @Test
    public void testDivision() {
        double result = Calculator.divide(10, 2);
        assertEquals(5, result, "10 / 2 should equal 5");
    }

    @Test
    public void testDivision_NegativeResult() {
        double result = Calculator.divide(-10, 2);
        assertEquals(-5, result, "-10 / 2 should equal -5");
    }

    @Test
    public void testDivision_Decimals() {
        double result = Calculator.divide(7, 2);
        assertEquals(3.5, result, "7 / 2 should equal 3.5");
    }

    @Test
    public void testDivision_DivisionByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Calculator.divide(10, 0);
        });
        String expectedMessage = "Division by zero is not allowed.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // New tests added to cover more cases

    @Test
    public void testAddition_WithZero() {
        double result1 = Calculator.add(0, 5);
        double result2 = Calculator.add(5, 0);
        assertEquals(5, result1, "0 + 5 should equal 5");
        assertEquals(5, result2, "5 + 0 should equal 5");
    }
    
    @Test
    public void testSubtraction_WithZero() {
        double result1 = Calculator.subtract(0, 5);
        double result2 = Calculator.subtract(5, 0);
        assertEquals(-5, result1, "0 - 5 should equal -5");
        assertEquals(5, result2, "5 - 0 should equal 5");
    }
    
    @Test
    public void testMultiplication_NegativeAndPositive() {
        double result = Calculator.multiply(-10, 5);
        assertEquals(-50, result, "-10 * 5 should equal -50");
    }
    
    @Test
    public void testMultiplication_BothNegative() {
        double result = Calculator.multiply(-10, -5);
        assertEquals(50, result, "-10 * -5 should equal 50");
    }
    
    @Test
    public void testDivision_WithNegativeDivisor() {
        double result = Calculator.divide(10, -2);
        assertEquals(-5, result, "10 / -2 should equal -5");
    }
    
    @Test
    public void testAddition_BoundaryValues() {
        // Using some extreme values
        double a = Double.MAX_VALUE / 2;
        double b = Double.MAX_VALUE / 2;
        double expected = Double.MAX_VALUE;
        double result = Calculator.add(a, b);
        assertEquals(expected, result, "Summing two halves of Double.MAX_VALUE should be Double.MAX_VALUE");
    }
    
    @Test
    public void testSubtraction_BoundaryValues() {
        // Subtracting equal numbers should yield zero even with extreme values
        double a = Double.MAX_VALUE;
        double result = Calculator.subtract(a, a);
        assertEquals(0, result, "Subtracting a number from itself should equal 0");
    }
    
    @Test
    public void testDivision_FractionalResultHighPrecision() {
        double result = Calculator.divide(1, 3);
        // expected roughly 0.33333
        assertEquals(0.33333, result, 0.00001, "1 / 3 should be approximately 0.33333");
    }

        @BeforeAll
        public static void setupAll() {
            System.out.println("Running Calculator tests...");
        }

        @BeforeEach
        public void setup() {
            System.out.println("Starting a test...");
        }

        @AfterEach
        public void teardown() {
            System.out.println("Finished a test.");
        }

        @AfterAll
        public static void teardownAll() {
            System.out.println("All tests completed.");
        }

        @Test
        @Order(1)
        @DisplayName("Addition with positive numbers")
        @Tag("Addition")
        public void testAddition_PositiveNumbers() {
            double result = Calculator.add(10, 5);
            assertEquals(15, result, "10 + 5 should equal 15");
        }

        @Test
        @Order(2)
        @DisplayName("Addition with negative numbers")
        @Tag("Addition")
        public void testAddition_NegativeNumbers() {
            double result = Calculator.add(-10, -5);
            assertEquals(-15, result, "-10 + (-5) should equal -15");
        }

        @RepeatedTest(3)
        @Order(3)
        @DisplayName("Repeated addition test")
        public void testAddition_Repeated() {
            double result = Calculator.add(1, 2);
            assertEquals(3, result, "1 + 2 should equal 3");
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, -1, 100, -100})
        @Order(4)
        @DisplayName("Parameterized addition with zero and boundaries")
        public void testAddition_Parameterized(int value) {
            double result = Calculator.add(value, 0);
            assertEquals(value, result, value + " + 0 should equal " + value);
        }

        @ParameterizedTest
        @CsvSource({
                "10, 5, 2",
                "9, 3, 3",
                "-10, -2, 5"
        })
        @Order(5)
        @DisplayName("Division with CSV parameters")
        public void testDivision_CsvParameterized(double a, double b, double expected) {
            double result = Calculator.divide(a, b);
            assertEquals(expected, result, a + " / " + b + " should equal " + expected);
        }

        @Test
        @Order(6)
        @Timeout(1)  // Jenkins will fail this test if it runs >1s
        public void testMultiplication_Performance() {
            double result = Calculator.multiply(1000, 2000);
            assertEquals(2_000_000, result);
        }

        @Test
        @Order(7)
        @Disabled("Not implemented yet")
        public void testFutureFeature() {
            // Jenkins will mark this test as skipped
        }

}