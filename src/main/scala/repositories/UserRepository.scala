package repositories

import cats.effect.Concurrent
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.util.meta.Meta
import models.users.User

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime}


trait UserRepositoryAlgebra[F[_]] {

  def createUser(user: User): F[Int]

  def findByUsername(username: String): F[Option[User]]
}

class UserRepository[F[_] : Concurrent](transactor: Transactor[F]) extends UserRepositoryAlgebra[F] {

  // Meta instance to map between LocalDateTime and Timestamp
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].imap(_.toLocalDateTime)(Timestamp.valueOf)

  def createUser(user: User): F[Int] = {
    println(user)
    println(
      s"""
        INSERT INTO users (username, password_hash, first_name, last_name, contact_number, email, role, created_at)
        VALUES (${user.username}, ${user.password_hash}, ${user.first_name}, ${user.last_name}, ${user.contact_number}, ${user.email}, ${user.role}, ${user.created_at})
      """
    )
    sql"""
      INSERT INTO users (username, password_hash, first_name, last_name, contact_number, email, role, created_at)
      VALUES (${user.username}, ${user.password_hash}, ${user.first_name}, ${user.last_name}, ${user.contact_number}, ${user.email}, ${user.role}, ${user.created_at})
    """.update
      .run
      .transact(transactor)
  }

  def findByUsername(username: String): F[Option[User]] = {
    sql"SELECT * FROM users WHERE username = $username"
      .query[User]
      .option
      .transact(transactor)
  }
}
