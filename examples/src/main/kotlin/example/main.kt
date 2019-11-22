package example

import react.Component
import react.RProps
import react.dom.div
import react.dom.li
import react.dom.render
import react.dom.ul
import react.router.dom.hashRouter
import react.router.dom.route
import react.router.dom.routeLink
import react.router.dom.switch
import kotlin.browser.document
import kotlin.browser.window
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
val examples = mapOf(
    "todo" to Todo::class,
//    "product" to Product::class, TODO
    "TicTacToe" to TicTacToe::class,
    "axios" to AxiosSearch::class
) as Map<String, KClass<Component<*, *>>>

fun main(args: Array<String>) {
    window.onload = {
        render(document.getElementById("app")) {
            hashRouter {
                switch {
                    for ((name, component) in examples) {
                        route("/$name", component = component)
                    }

                    route("/") {
                        div {
                            ul {
                                for ((name, _) in examples) {
                                    li {
                                        routeLink(to = name) {
                                            +name
                                        }
                                    }
                                }
                            }
                            app(object : RProps {})
                        }
                    }
                }
            }
        }
    }
}
