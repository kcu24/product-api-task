tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
}

tasks.getByName<Jar>("jar") {
    enabled = true
}


tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.ingemark.productsmgmtapp.ProductsmgmtApplication")
}

dependencies {
    implementation(project(":api-rest-implementation"))
    implementation(project(":infrastructure-exchange"))
    implementation(project(":infrastructure-persistence"))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.retry:spring-retry")
}