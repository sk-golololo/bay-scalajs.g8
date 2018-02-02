// scalafmt: { maxColumn = 160, align.tokens = ["="] }

import bay.driver.CustomizedPgDriver
import com.lightbend.lagom.internal.scaladsl.server.ScaladslServiceRouter
import com.softwaremill.macwire._
import controllers._
import controllers.swagger._
import play.api.ApplicationLoader.Context
import play.api.db.slick.{DbName, SlickComponents}
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.i18n.I18nComponents
import play.api.mvc.{BodyParsers, EssentialFilter}
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, LoggerConfigurator}
import play.filters.cors.{CORSConfig, CORSFilter}
import play.filters.gzip.{GzipFilter, GzipFilterConfig}
import services.Services
import services.dao.UserDao
import slick.basic.DatabaseConfig

import scala.concurrent.ExecutionContext

class ApplicationComponents(context: Context)
    extends BuiltInComponentsFromContext(context)
    with I18nComponents
    with DBComponents
    with HikariCPComponents
    with SlickComponents
    with AssetsComponents {

  /*
   * Engine
   */
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment)
  }
  val bodyParsers: BodyParsers.Default = wire[BodyParsers.Default]

  /*
   * Filter
   */
  lazy val gzipFilterConfig: GzipFilterConfig         = GzipFilterConfig.fromConfiguration(configuration)
  lazy val gzipFilter: GzipFilter                     = wire[GzipFilter]
  lazy val corsConfig: CORSConfig                     = CORSConfig.fromConfiguration(configuration)
  lazy val corsFilter: CORSFilter                     = new CORSFilter(corsConfig)
  override lazy val httpFilters: Seq[EssentialFilter] = wireSet[EssentialFilter].toSeq

  /*
   * SERVICES
   */
  lazy val dbConfig: DatabaseConfig[CustomizedPgDriver] = slickApi.dbConfig[CustomizedPgDriver](DbName("default"))
  lazy val userDao: UserDao                             = wire[UserDao]
  lazy val services: Services                           = wire[Services]

  /*
   * CONTROLLER
   */
  lazy val security: Security         = wire[Security]
  lazy val appController: Application = wire[Application]

  lazy val petService: PetService = wire[PetServiceImpl]
  lazy val storeService: StoreService = wire[StoreServiceImpl]
  lazy val userService: UserService = wire[UserServiceImpl]






  /*
   * ROUTES
   */
  lazy val router: Router = {
    // add the prefix string in local scope for the Routes constructor
    val prefix: String = "/"
    wire[Routes]
  }

    lazy val petController: controllers.PetRouter0 = wire[controllers.PetRouter0]
  //  lazy val userController: UserRou
  // ter = wire[UserRouter]
  //  lazy val storeController: StoreRouter = wire[StoreRouter]

//  lazy val petRouter: Router = new ScaladslServiceRouter(petController.descriptor, petService, httpConfiguration)
//  val storeRouter: Router = new ScaladslServiceRouter(storeController.descriptor, storeService, httpConfiguration)
//  val userRouter: Router = new ScaladslServiceRouter(userController.descriptor, userService, httpConfiguration)

}
