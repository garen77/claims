plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
}

group = "com.saturn"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	
	// Flowable dependencies
	implementation("org.flowable:flowable-spring-boot-starter:7.1.0")
	implementation("org.flowable:flowable-engine-spring:7.1.0")
	implementation("org.flowable:flowable-ui-admin-spring:7.1.0")
	implementation("org.flowable:flowable-ui-modeler-spring:7.1.0")
	implementation("org.flowable:flowable-ui-task-spring:7.1.0")
	
	// Database
	runtimeOnly("com.h2database:h2")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Configurazione per il run con classpath corretto
tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	classpath = sourceSets["main"].runtimeClasspath
	jvmArgs = listOf(
		"-Dspring.profiles.active=dev",
		"-Djava.awt.headless=true",
		"-Xmx1024m"
	)
}
