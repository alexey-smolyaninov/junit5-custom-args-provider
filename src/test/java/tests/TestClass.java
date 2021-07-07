package tests;

import dataProvider.DataFromDB;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import java.util.logging.Logger;

@DisplayName("Some class with tests")
public class TestClass {
    Logger logger = Logger.getGlobal();

    @DisplayName("Test 1. Positive test")
    @ParameterizedTest(name = "#{index}. Args: {0}, {1}, {2}")
    @DataFromDB(table = "employees", columns = {"firstname", "lastname", "phone"})
    void test1(String firstname, String lastname, String phone){
        firstname = Objects.requireNonNull(firstname, "The firstname must not be null");
        lastname = Objects.requireNonNull(lastname, "The lastname must not be null");
        phone = Objects.requireNonNull(phone, "The phone must not be null");
        //
        // do something
        //
    }


    @DisplayName("Test 2. Test with a non existing column")
    @ParameterizedTest(name = "#{index}. Args: {0}, {1}, {2}")
    @DataFromDB(table = "employees", columns = {"firstname", "lastname", "invalidColumnName"})
    void test2(String firstname, String lastname, String phone) {
        //
        // the test should fail due to the data provider will not be able to provide arguments
        //
    }

}
