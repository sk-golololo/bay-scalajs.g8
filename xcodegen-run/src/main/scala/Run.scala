package com.xiris

import io.swagger.codegen.SwaggerCodegen

object Run {
  def main(args: Array[String]): Unit = {
    print("")
    SwaggerCodegen.main(
      Array("generate",
        "-l", "XCodegen",
//        "-supporting-files",
        "--api-package", "controllers.swagger",
        "-i", "http://petstore.swagger.io/v2/swagger.json"
      ))

    /*SwaggerCodegen.main(
      Array("generate",
        "-l", "XCodegen",
        "-i", "http://petstore.swagger.io/v2/swagger.json"
      ))*/
  }
}
