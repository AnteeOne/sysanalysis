plugins {
    kotlin("jvm") version "1.5.10"
    java
}

group = "tech.antee"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("org.postgresql:postgresql:42.3.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.projectlombok:lombok:1.18.20")
    implementation("com.opencsv:opencsv:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}