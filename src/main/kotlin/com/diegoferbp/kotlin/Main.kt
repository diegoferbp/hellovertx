package com.diegoferbp.kotlin

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
class HelloWorldVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val router = Router.router(vertx)

        router.get("/").handler { context ->
            context.response()
                .putHeader("content-type", "text/html; charset=utf-8")
                .end("<h1>¡Hola Mundo desde Vert.x con Kotlin! 🚀</h1>")

        }
        router.get("/hello/:name").handler { context ->
            val name = context.pathParam("name")
            context.response()
                .putHeader("content-type", "application/json")
                .end("""{"message": "¡Hola $name!", "framework": "Vert.x", "language": "Kotlin"}""")
        }

        router.get("/info").handler { context ->
            val info = """
                {
                    "server": "Vert.x",
                    "language": "Kotlin 2.1",
                    "port": 8080,
                    "status": "running",
                    "endpoints": [
                        "GET /",
                        "GET /hello/:name",
                        "GET /info"
                    ]
                }
            """.trimIndent()

            context.response()
                .putHeader("content-type", "application/json")
                .end(info)
        }

        val server = vertx.createHttpServer()
        server.requestHandler(router).listen(8080) { result ->
            if (result.succeeded()) {
                println("🎉 Servidor iniciado en http://localhost:8080")
                println("📋 Endpoints disponibles:")
                println("   • GET / - Saludo básico")
                println("   • GET /hello/{nombre} - Saludo personalizado")
                println("   • GET /info - Información del servidor")
            } else {
                println("❌ Error al iniciar el servidor: ${result.cause()}")
            }
        }
    }
}

class CoroutineHelloWorldVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val router = createRouter()
        val server = vertx.createHttpServer()

        // Usar await para operaciones asíncronas
        server.requestHandler(router).listen(8080).coAwait()
        println("🚀 Servidor con corrutinas iniciado en puerto 8080")
    }

    private suspend fun createRouter(): Router {
        val router = Router.router(vertx)

        router.get("/async").handler { context ->
            // Lanzar corrutina para manejar request asíncrono
            vertx.dispatcher().let { dispatcher ->
                kotlinx.coroutines.GlobalScope.launch(dispatcher) {
                    val response = simulateAsyncWork()
                    context.response()
                        .putHeader("content-type", "application/json")
                        .end(response)
                }
            }
        }

        return router
    }

    private suspend fun simulateAsyncWork(): String {
        delay(1000) // Simular trabajo asíncrono
        return """{"message": "Trabajo asíncrono completado", "delay": "1 segundo"}"""
    }
}


//fun main() {
//    val vertx = Vertx.vertx()
//    vertx.deployVerticle(HelloWorldVerticle()) { result ->
//        if (result.succeeded()) {
//            println("✅ Verticle desplegado exitosamente")
//        } else {
//            println("❌ Error desplegando verticle: ${result.cause()}")
//        }
//    }
//}



fun main() {
    val vertx = Vertx.vertx()
    vertx.deployVerticle(CoroutineHelloWorldVerticle())
}