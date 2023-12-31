plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version("6.1.0")
}

group = "ru.entryset"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    compileOnly(files("C:\\Users\\t9154\\Desktop\\sources\\Library\\EntryCore.jar"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks {
    jar {
        enabled = false
        dependsOn(shadowJar)
    }
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}