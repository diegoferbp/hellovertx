plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "com.diegoferbp.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.vertx:vertx-core:4.5.1")
    implementation("io.vertx:vertx-lang-kotlin:4.5.1")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:4.5.1")
    implementation("io.vertx:vertx-web:4.5.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.diegoferbp.kotlin.MainKt")
}