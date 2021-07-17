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
  //implementation("io.vertx:vertx-web-client")
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-config")
	implementation("io.vertx:vertx-config-yaml")
	implementation("io.vertx:vertx-web")
  //implementation("io.vertx:vertx-web-openapi")
  //implementation("io.vertx:vertx-pg-client")
  implementation("io.vertx:vertx-rx-java3")
	//implementation("io.vertx:vertx-auth-oauth2")
	implementation("com.google.dagger:dagger-compiler:2.25.2")
	implementation("com.google.dagger:dagger:2.25.2")
	annotationProcessor("com.google.dagger:dagger-compiler:2.25.2")
  testImplementation("io.vertx:vertx-junit5")
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
