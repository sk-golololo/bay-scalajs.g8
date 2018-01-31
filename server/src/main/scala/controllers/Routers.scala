package controllers

import controllers.swagger.v1.petstore.{PetRouter, StoreRouter, UserRouter}
import play.api.libs.Files
import play.api.libs.circe.Circe
import play.api.mvc.{MultipartFormData, Request, Result}
import play.api.routing.SimpleRouter
import services.Services
import shared.models.swagger.petstore.v1.{Order, Pet, User}

abstract class ApiRouterImpl(application: controllers.Application, assets: controllers.Assets) {
  this: ExtendedController with SimpleRouter with Circe =>
  protected def controllerComponents: play.api.mvc.ControllerComponents = application.controllerComponents

  implicit val ec: scala.concurrent.ExecutionContext = application.defaultExecutionContext
  val services: Services = application.services

}

class PetRouterImpl(application: controllers.Application, assets: controllers.Assets) extends ApiRouterImpl(application, assets) with PetRouter {
  override def updatePet()(implicit request: Request[Pet]): HttpResult[Result] = ???
  override def addPet()(implicit request: Request[Pet]): HttpResult[Result] = HttpResult.point(Ok)
  override def findPetsByStatus(status: Option[String])(implicit request: Request[_]): HttpResult[List[Pet]] = ???
  override def findPetsByTags(tags: Option[String])(implicit request: Request[_]): HttpResult[List[Pet]] = ???
  override def getPetById(petId: Int, apiKey: String)(implicit request: Request[_]): HttpResult[Pet] = ???
  override def updatePetWithForm(petId: Int)(implicit request: Request[MultipartFormData[Files.TemporaryFile]]): HttpResult[Result] = ???
  override def deletePet(petId: Int)(implicit request: Request[_]): HttpResult[Result] = ???
}

class StoreRouterImpl(application: controllers.Application, assets: controllers.Assets) extends ApiRouterImpl(application, assets) with StoreRouter {
  override def placeOrder()(implicit request: Request[Order]): HttpResult[Order] = ???
  override def getOrderById(orderId: String)(implicit request: Request[_]): HttpResult[Order] = ???
  override def deleteOrder(orderId: String)(implicit request: Request[_]): HttpResult[Result] = ???
}

class UserRouterImpl(application: controllers.Application, assets: controllers.Assets) extends ApiRouterImpl(application, assets) with UserRouter {
  override def createUser()(implicit request: Request[User]): HttpResult[Result] = ???
  override def createUsersWithArrayInput()(implicit request: Request[_]): HttpResult[Result] = ???
  override def createUsersWithListInput()(implicit request: Request[_]): HttpResult[Result] = ???
  override def loginUser(username: Option[String], password: Option[String])(implicit request: Request[_]): HttpResult[String] = ???
  override def logoutUser()(implicit request: Request[_]): HttpResult[Result] = ???
  override def getUserByName(username: String)(implicit request: Request[_]): HttpResult[User] = ???
  override def updateUser(username: String)(implicit request: Request[User]): HttpResult[Result] = ???
  override def deleteUser(username: String)(implicit request: Request[_]): HttpResult[Result] = ???
}
