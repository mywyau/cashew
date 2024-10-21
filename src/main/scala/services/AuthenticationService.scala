package services

import cats.NonEmptyParallel
import cats.data.{Validated, ValidatedNel}
import cats.effect.Concurrent
import cats.syntax.all._
import com.github.t3hnar.bcrypt._
import models.users.{User, UserLoginRequest, UserRegistrationRequest}
import repositories.UserRepositoryAlgebra

import java.time.LocalDateTime

trait AuthenticationService[F[_]] {

  def registerUser(request: UserRegistrationRequest): F[Validated[List[String], User]]

  def loginUser(request: UserLoginRequest): F[Either[String, User]]

}

class AuthenticationServiceImpl[F[_] : Concurrent : NonEmptyParallel](userRepository: UserRepositoryAlgebra[F]) extends AuthenticationService[F] {

  private val UsernameExistsError = "Username already exists"
  private val ContactNumberExistsError = "Contact number already exists"
  private val EmailExistsError = "Email already exists"
  private val UserCreationError = "Failed to create user due to an unknown error"

  def registerUser(request: UserRegistrationRequest): F[Validated[List[String], User]] = {
    val hashedPassword = request.password.boundedBcrypt

    // Create a new user instance from the request data with the hashed password
    val newUser = User(
      username = request.username,
      password_hash = hashedPassword,
      first_name = request.first_name,
      last_name = request.last_name,
      contact_number = request.contact_number,
      email = request.email,
      role = request.role,
      created_at = LocalDateTime.now()
    )

    // Concurrently check if the username or contact number already exists
    val checkUsername: F[ValidatedNel[String, Unit]] =
      userRepository.findByUsername(request.username).map {
        case Some(_) => Validated.invalidNel(UsernameExistsError)
        case None => Validated.valid(())
      }

    val checkContactNumber: F[ValidatedNel[String, Unit]] =
      userRepository.findByContactNumber(request.contact_number).map {
        case Some(_) => Validated.invalidNel(ContactNumberExistsError)
        case None => Validated.valid(())
      }

    val checkEmail: F[ValidatedNel[String, Unit]] =
      userRepository.findByEmail(request.email).map {
        case Some(_) => Validated.invalidNel(EmailExistsError)
        case None => Validated.valid(())
      }

    // Run both checks concurrently and combine the results to accumulate any validation errors
    val validationResult: F[ValidatedNel[String, Unit]] = (checkUsername, checkContactNumber, checkEmail).parMapN(_ |+| _ |+| _)

    // If validation passes, attempt to create the user
    validationResult.flatMap {
      case Validated.Valid(_) =>
        println("Yay")
        // Proceed to create the user if all validations succeed
        userRepository.createUser(newUser).map { rowsAffected =>
          if (rowsAffected > 0) {
            Validated.valid(newUser)
          } else {
            Validated.invalidNel(UserCreationError).leftMap(_.toList)
          }
        }

      case Validated.Invalid(errors) =>
        println("Oh NO")
        // If validation fails, return the accumulated errors as List[String]
        Concurrent[F].pure(Validated.invalid(errors.toList))
    }
  }


  def loginUser(request: UserLoginRequest): F[Either[String, User]] = {
    userRepository.findByUsername(request.username).flatMap {
      case Some(user) if request.password.isBcrypted(user.password_hash) =>
        Concurrent[F].pure(Right(user)) // Successfully authenticated
      case Some(_) =>
        Concurrent[F].pure(Left("Invalid password")) // Password did not match
      case None =>
        Concurrent[F].pure(Left("Username not found")) // Username not found
    }
  }
}

object AuthenticationService {
  def apply[F[_] : Concurrent : NonEmptyParallel](userRepository: UserRepositoryAlgebra[F]): AuthenticationService[F] =
    new AuthenticationServiceImpl[F](userRepository)
}
