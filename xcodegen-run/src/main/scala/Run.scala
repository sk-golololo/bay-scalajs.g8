package com.xiris

import io.swagger.codegen.SwaggerCodegen

object Run {
  def main(args: Array[String]): Unit = {
    SwaggerCodegen.main(
      Array("generate",
        "-l", "XClientCodegen",
        "-i", "http://petstore.swagger.io/v2/swagger.json"))
  }
}
