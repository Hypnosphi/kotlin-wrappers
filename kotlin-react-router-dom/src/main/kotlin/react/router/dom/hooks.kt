package react.router.dom

import kotlinext.js.Object
import kotlinext.js.jsObject
import react.RProps

fun useHistory() = ReactRouterDomModule.rawUseHistory()

fun useLocation() = ReactRouterDomModule.rawUseLocation()

fun <T : RProps> useParams(): T? {
    val rawParams = ReactRouterDomModule.rawUseParams()

    return if (Object.keys(rawParams as Any).isEmpty()) null else rawParams as T
}

fun <T : RProps> useRouteMatch(
    vararg path: String,
    exact: Boolean = false,
    strict: Boolean = false,
    sensitive: Boolean = false
): RouteResultMatch<T>? {
    if (path.isEmpty()) {
        return ReactRouterDomModule.rawUseRouteMatch(null)
    }

    val options: RouteMatchOptions = jsObject {
        this.path = path
        this.exact = exact
        this.strict = strict
        this.sensitive = sensitive
    }

    return ReactRouterDomModule.rawUseRouteMatch(options)
}

external interface RouteMatchOptions {
    var path: Array<out String>
    var exact: Boolean
    var strict: Boolean
    var sensitive: Boolean
}
