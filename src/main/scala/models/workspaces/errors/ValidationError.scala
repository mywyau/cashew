package models.workspaces.errors

sealed trait WorkValidationError

case object InvalidWorkspaceId extends WorkValidationError

case object InvalidTimeRange extends WorkValidationError

case object WorkspaceNotFound extends WorkValidationError


