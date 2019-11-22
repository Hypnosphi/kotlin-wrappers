import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.config.BundleConfig
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.util.nodePath
import org.jetbrains.kotlin.gradle.frontend.util.startWithRedirectOnFail
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

plugins {
    id("kotlin2js")
}

buildscript {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")

    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-frontend-plugin:0.0.44")
    }
}

apply {
    plugin("org.jetbrains.kotlin.frontend")
}

dependencies {
    compile(kotlin("stdlib-js"))
    compile(project(":kotlin-css"))
    compile(project(":kotlin-react"))
    compile(project(":kotlin-react-dom"))
    compile(project(":kotlin-react-redux"))
    compile(project(":kotlin-redux"))
    compile(project(":kotlin-react-router-dom"))
    compile(project(":kotlin-styled"))
}

extensions.configure<KotlinFrontendExtension>("kotlinFrontend") {
    downloadNodeJsVersion = Versions.node

    bundle("webpack", delegateClosureOf<WebPackExtension> {
        bundleName = "main"
        contentPath = file("src/main/resources/static")
        publicPath = "/"
        port = 8081
    })

    define("PRODUCTION", false)
}

extensions.configure<NpmExtension>("npm") {
    dependency("kotlin", Versions.kotlin)
    dependency("react", Versions.reactJs)
    dependency("react-dom", Versions.reactJs)
    dependency("react-router", Versions.reactRouter)
    dependency("react-router-dom", Versions.reactRouterDom)
    dependency("styled-components", Versions.styledComponents)
    dependency("inline-style-prefixer", Versions.inlineStylePrefixer)
    dependency("axios", Versions.axios)
    dependency("react-quill", Versions.reactQuill)
}

(tasks.getByPath("compileKotlin2Js") as Kotlin2JsCompile).apply {
    kotlinOptions {
        metaInfo = true
        outputFile = "${project.buildDir.path}/js/${project.name}.js"
        sourceMap = true
        moduleKind = "commonjs"
        main = "call"
    }
}

(tasks.getByPath("compileTestKotlin2Js") as Kotlin2JsCompile).apply {
    kotlinOptions {
        metaInfo = true
        outputFile = "${project.buildDir.path}/js-tests/${project.name}-tests.js"
        sourceMap = true
        moduleKind = "commonjs"
        main = "call"
    }
}

object Versions {
    val kotlin = "1.3.11"
    val node = "10.15.0"
    val reactJs = "16.6.0"
    val reactRouter = "4.3.1"
    val reactRouterDom = "4.3.1"
    val styledComponents = "3.4.10"
    val inlineStylePrefixer = "5.0.3"
    val axios = "0.18.0"
    val reactQuill = "1.3.3"
}

