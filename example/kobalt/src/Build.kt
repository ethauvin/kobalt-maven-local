import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.java.*

// ./kobaltw run --log 2

val bs = buildScript {
    plugins("net.thauvin.erik:kobalt-maven-local:")
    repos(localMaven())
}

val example = project {

    // println("  >>> LOCAL MAVEN REPO: " + localMaven())

    name = "example"
    group = "com.example"
    artifactId = name
    version = "0.1"

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.Main"
    }
}
