# Library Publication

## Registering a Sonatype account

see https://getstream.io/blog/publishing-libraries-to-mavencentral-2021/#overview

## Prepare signature

1. to install gpg tool for your system: https://gpgtools.org/
2. to generate a new key: gpg --full-gen-key
3. to publish your key signature gpg --keyserver keyserver.ubuntu.com --send-keys [you key id]
4. to export your key as file to project root: gpg --output private.pgp --export-secret-keys [you key id]

## Publish The Artifact

1. add follow lines to `.gitignore`

```
secret.properties
*.pgp
```

2. Copy this whole module (`gradle-publication`) to your project
3. At `settings.gradle` at root of your project. add following line

```
includeBuild("gradle-publication")
```

4. create `secret.properties`  file in project root with your secrets

```
ossrhUsername=...
ossrhPassword=...
signing.keyId=...
signing.secretKeyRingFile=../private.pgp
signing.password=...
```

5. apply publication plug-in to each of your library module

<details>
<summary>JVM</summary>

```groovy
plugins {
    id "org.jetbrains.kotlin.jvm" // or "java"
    id "jvm.publication"
}
```

</details>
<details>
<summary>Kotlin Multi-Platform</summary>

```groovy
plugins {
    id "org.jetbrains.kotlin.multiplatform"
    id "kmm.publication"
}
```

</details>

6. edit POM in `src/main/kotlin/publication.gradle.kts` and/or `src/main/kotlin/kmm.publication.gradle.kts`
7. set artifact `group` and `version` for your module. such as set at root `build.gradle` like the following

```groovy
subprojects {
    group = "io.github.piruin"
    version = "1.0.0"
}
```

8. run  `./gradlew publishMavenPublicationToSonatypeRepository`
9. after that go to the [Sonatype](https://s01.oss.sonatype.org/#welcome) site, Login -> menu `Staging Repositories`
   -> `close` then `release`! 😄

## Reference

- [Publishing your Kotlin Multiplatform library to Maven Central](https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k)
