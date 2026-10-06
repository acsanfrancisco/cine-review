package acsanfrancisco.cine_review;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DatabaseTest {

    static Connection connection;

    @BeforeAll
    static void setUpDatabase() throws Exception{
        connection = DriverManager.getConnection("jdbc:h2:mem:cine-review-test", "sa", "");
        connection.createStatement().execute("""
        CREATE TABLE tb_users(
        id BIGINT NOT NULL,
        full_name varchar(255) NOT NULL,
        email varchar(255) NOT NULL)
        """);
    }

    @AfterAll
    static void closeDatabase() throws Exception{
        connection.close();
    }

    @Test
    @BeforeEach
    void shouldInsertUser() throws Exception{
        connection.createStatement().execute("""
        INSERT INTO tb_users (id, full_name,email)
        VALUES (1, 'Little Zé da Silva', 'littlezedasilva@gmail.com')
        """);
    }

    @Test
    void shouldFindUserById() throws Exception{
        connection.createStatement().executeQuery("SELECT * FROM tb_users WHERE id = 1");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM tb_users WHERE id = 1");
        Assertions.assertTrue(resultSet.next());
    }

}
