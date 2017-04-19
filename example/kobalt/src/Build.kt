import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.java.*

// ./kobaltw run --log 2

val bs = buildScript {
    //plugins(file("../libs/kobalt-maven-local-0.5.2.jar"))
    plugins("net.thauvin.erik:kobalt-maven-local:")
    repos(localMaven())
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
