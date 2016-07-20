import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.java.*
import net.thauvin.erik.kobalt.plugin.maven.local.*

val repos = repos(localMaven())

val pl = plugins(file("../kobaltBuild/libs/kobalt-maven-local-0.4.0-beta.jar"))
//val pl = plugins("net.thauvin.erik:kobalt-maven-local:0.4.0-beta")

val example = project {

    name = "example"
    group = "com.example"
    artifactId = name
    version = "0.1"

    sourceDirectories {
        path("src/main/java")
    }

    sourceDirectoriesTest {
        path("src/test/java")
    }

    dependencies {
    }

    dependenciesTest {
    }

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.Main"
    }
}