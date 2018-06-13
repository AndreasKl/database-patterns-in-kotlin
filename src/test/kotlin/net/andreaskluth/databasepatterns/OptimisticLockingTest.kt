package net.andreaskluth.databasepatterns

import org.junit.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class OptimisticLockingTest {

    @Test
    fun insertAndUpdateEntity() {
        prepareDatabase()
        val demo = OptimisticLockingDemo()

        val id = runInConnection(demo.createEntity("demo"))
        runInConnection(demo.updateEntity(id, "demo", 0))
    }

    private fun prepareDatabase() {
        runInStatement({ statement ->
            statement.execute("CREATE TABLE public.demo (id text NOT NULL PRIMARY KEY, name text, version bigint);")
        })
    }

    private fun <T> runInStatement(consumer: (statement: Statement) -> T): T {
        return runInConnection { connection ->
            connection.createStatement().use { statement ->
                consumer(statement)
            }
        }
    }

    private fun <T> runInConnection(consumer: (statement: Connection) -> T): T {
        return DriverManager.getConnection("jdbc:tc:postgresql:10.4://test/test-db?TC_DAEMON=true").use { connection ->
            consumer(connection)
        }
    }

}