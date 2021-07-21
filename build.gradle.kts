import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.monkey"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.1.1"
val junitJupiterVersion = "5.7.0"

val mainClassName = "com.monkey.monkeyshop.Main"

application {
  mainClass.set("com.monkey.monkeyshop.Main")
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-config:$vertxVersion")
	implementation("io.vertx:vertx-config-yaml:$vertxVersion")
	implementation("io.vertx:vertx-web:$vertxVersion")
	implementation("io.vertx:vertx-web-client:$vertxVersion")
  implementation("io.vertx:vertx-pg-client")
  implementation("io.vertx:vertx-rx-java3:$vertxVersion")
	implementation("io.vertx:vertx-auth-jwt:$vertxVersion")
	implementation("org.flywaydb:flyway-core:7.11.3")
	implementation("org.postgresql:postgresql:42.2.23")
	implementation("org.mindrot:jbcrypt:0.4")
	implementation("com.google.dagger:dagger-compiler:2.37")
	implementation("com.google.dagger:dagger:2.37")
	annotationProcessor("com.google.dagger:dagger-compiler:2.37")
  testImplementation("io.vertx:vertx-junit5:$vertxVersion")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main" to mainClassName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}
