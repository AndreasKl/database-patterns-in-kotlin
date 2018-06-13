package net.andreaskluth.databasepatterns

import org.junit.Test
import java.sql.DriverManager

class ValidateJdbcConnectionTest {

    @Test
    fun happyPathSuccess() {
        DriverManager.getConnection("jdbc:tc:postgresql:10.4://test/test-db?TC_DAEMON=true").use { connection ->
            connection.createStatement().use { statement -> statement.execute("SELECT 1;") }
        }
    }

}