plugins {
	id("java")
	id("io.spring.dependency-management") version "1.1.7"
	id("org.springframework.boot") version "3.5.0"
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

allprojects {
	group = "com.ingemark"
	version = "1.0.0"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	dependencies {
		annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
		implementation("org.mapstruct:mapstruct:1.5.5.Final")

		testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
		testImplementation("org.mockito:mockito-core:5.11.0")
		testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

	tasks.test {
		useJUnitPlatform()
	}
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	enabled = false
}

tasks.getByName<Jar>("jar") {
	enabled = true
}