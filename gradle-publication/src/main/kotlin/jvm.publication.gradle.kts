import java.util.Properties

plugins {
    java
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["signing.key"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("secret.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["signing.key"] = System.getenv("SIGNING_KEY")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

fun getExtraString(name: String) = ext[name]?.toString()

java {
    withJavadocJar()
    withSourcesJar()
}

fun safeProperty(propertyName: String): String? {
    return try {
        property(propertyName).toString()
    } catch (missingProperties: Exception) {
        null
    }
}

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            from(components["java"])
            // Provide artifacts information requited by Maven Central
            pom {
                name.set(property("pom.name")?.toString())
                description.set(property("pom.description").toString())
                url.set(property("pom.url").toString())

                licenses {
                    license {
                        name.set(property("pom.license.name").toString())
                        url.set(property("pom.license.url").toString())
                    }
                }

                developers {
                    developer {
                        id.set(property("pom.developer.id").toString())
                        name.set(property("pom.developer.name").toString())
                        email.set(property("pom.developer.email").toString())
                    }
                }
                scm {
                    url.set(safeProperty("pom.smc.url") ?: "${property("pom.url")}")
                    connection.set(safeProperty("pom.smc.connection") ?: "${property("pom.url")}.git")
                    developerConnection.set(safeProperty("pom.smc.developerConnection") ?: "${property("pom.url")}.git")
                }
            }
        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used
signing {
    getExtraString("signing.keyId")?.let { keyId ->
        getExtraString("signing.key")?.let { key ->
            useInMemoryPgpKeys(keyId, key, getExtraString("signing.password"))
        }
        sign(publishing.publications)
    }
}
