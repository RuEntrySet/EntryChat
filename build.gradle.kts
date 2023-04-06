plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/RuEntrySet/EntryAPI")
        credentials {
            username = System.getenv("GITHUB_USERNAME")
            password = System.getenv("GITHUB_PASSWORD")
        }
    }
}

dependencies {
    val library = "C:\\Users\\t9154\\Desktop\\Исходники\\Library/"

    //shadow(files(library + "spigot-1.12.2-NMS.jar"))
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    implementation("redis.clients:jedis:4.2.0")

    implementation("ru.entryset:api:3.5.3")

    compileOnly("me.clip:placeholderapi:2.10.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}