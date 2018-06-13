package net.andreaskluth.databasepatterns

import java.sql.Connection
import java.util.UUID

class OptimisticLockingDemo {

    fun createEntity(name: String): (conn: Connection) -> String {
        return { conn ->
            val id = UUID.randomUUID().toString()
            val statement = conn.prepareStatement("INSERT INTO public.demo (id, name, version) VALUES (?, ?, ?);")
            statement.setString(1, id)
            statement.setString(2, name)
            statement.setInt(3, 0)
            statement.execute()
            id
        }
    }

    fun updateEntity(id: String, name: String, version: Int): (conn: Connection) -> String {
        return { conn ->
            val statement = conn.prepareStatement("UPDATE public.demo SET name  = ?, version = ? WHERE id = ? AND version = ?;")
            statement.setString(1, name)
            statement.setInt(2, version + 1)
            statement.setString(3, id)
            statement.setInt(4, version)
            if (statement.executeUpdate() == 0) {
                throw IllegalStateException("Optimistic locking failed.")
            }
            id
        }
    }

}