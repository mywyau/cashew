package repositories.workspaces

import cats.effect.Concurrent
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.util.meta.Meta
import models.workspaces.Workspace

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime}

trait WorkspaceRepositoryAlgebra[F[_]] {

  def findWorkspaceById(workspaceId: String): F[Option[Workspace]]

  def findWorkspaceByName(workspaceId: String): F[Option[Workspace]]

  def getAllWorkspace: F[List[Workspace]]

  def setWorkspace(workspace: Workspace): F[Int]

  def updateWorkspace(workspaceId: String, updatedWorkspace: Workspace): F[Int]

  def deleteWorkspace(workspaceId: String): F[Int]
}

class WorkspaceRepository[F[_] : Concurrent](transactor: Transactor[F]) extends WorkspaceRepositoryAlgebra[F] {

  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].imap(_.toLocalDateTime)(Timestamp.valueOf)

  implicit val localDateMeta: Meta[LocalDate] =
    Meta[Date].imap(_.toLocalDate)(Date.valueOf)

  def findWorkspaceById(workspaceId: String): F[Option[Workspace]] = {
    sql"SELECT * FROM workspace WHERE workspace_id = $workspaceId"
      .query[Workspace]
      .option
      .transact(transactor)
  }

  def findWorkspaceByName(workspaceName: String): F[Option[Workspace]] = {
    sql"SELECT * FROM workspace WHERE name = $workspaceName"
      .query[Workspace]
      .option
      .transact(transactor)
  }


  def getAllWorkspace: F[List[Workspace]] = {
    sql"SELECT * FROM workspace"
      .query[Workspace]
      .to[List]
      .transact(transactor)
  }

  def setWorkspace(workspace: Workspace): F[Int] = {
    sql"""
      INSERT INTO workspace (
        business_id,
        name,
        description,
        address,
        city,
        country,
        postcode,
        price_per_day,
        latitude,
        longitude
      )
      VALUES (
        ${workspace.business_id},
        ${workspace.name},
        ${workspace.description},
        ${workspace.address},
        ${workspace.city},
        ${workspace.country},
        ${workspace.postcode},
        ${workspace.price_per_day},
        ${workspace.latitude},
        ${workspace.longitude}
      )
    """.update
      .run
      .transact(transactor)
  }


  def updateWorkspace(business_id: String, updatedWorkspace: Workspace): F[Int] = {
    sql"""
      UPDATE workspace
      SET name = ${updatedWorkspace.name},
          description = ${updatedWorkspace.description},
          address = ${updatedWorkspace.address},
          city = ${updatedWorkspace.city},
          country = ${updatedWorkspace.country},
          postcode = ${updatedWorkspace.postcode},
          price_per_day = ${updatedWorkspace.price_per_day},
          latitude = ${updatedWorkspace.latitude},
          longitude = ${updatedWorkspace.longitude}
      WHERE business_id = $business_id
    """
      .update
      .run
      .transact(transactor)
  }

  def deleteWorkspace(workspaceId: String): F[Int] = {
    sql"""
      DELETE FROM workspace WHERE workspace_id = $workspaceId
    """.update
      .run
      .transact(transactor)
  }

}
