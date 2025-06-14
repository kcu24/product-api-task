tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))

    implementation("org.liquibase:liquibase-core:4.24.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
}