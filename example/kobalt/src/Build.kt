import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.java.*

// ./kobaltw run
// ./kobaltw puslishToMavenLocal

val bs = buildScript {
    //repos(file("K:/maven/repository"))
    plugins("net.thauvin.erik:kobalt-maven-local:")
}

val example = project {

    println("  >>> LOCAL MAVEN REPO: " + localMaven().removePrefix("file:/"))

    name = "example"
    version = "0.1"

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.Main"
    }
}