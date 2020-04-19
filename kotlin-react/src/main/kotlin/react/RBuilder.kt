package react

import kotlinext.js.*
import kotlin.reflect.*

@DslMarker
annotation class ReactDsl

@ReactDsl
open class RBuilder {
    val childList = mutableListOf<Any>()

    fun child(element: ReactElement): ReactElement {
        childList.add(element)
        return element
    }

    operator fun String.unaryPlus() {
        childList.add(this)
    }

    fun <P : RProps> child(type: Any, props: P, children: List<Any>) =
        child(createElement(type, props, *children.toTypedArray()))

    fun <P : RProps> child(type: Any, props: P, handler: RHandler<P>): ReactElement {
        val children = with(RElementBuilder(props)) {
            handler()
            childList
        }
        return child(type, props, children)
    }

    operator fun <P : RProps> RClass<P>.invoke(handler: RHandler<P>) =
        child(this, jsObject(), handler)

    operator fun <T> RProvider<T>.invoke(value: T, handler: RHandler<RProviderProps<T>>) =
        child(this, jsObject { this.value = value }, handler)

    operator fun <T> RConsumer<T>.invoke(handler: RBuilder.(T) -> Unit) =
        child(this, jsObject<RConsumerProps<T>> {
            this.children = { value ->
                buildElements { handler(value) }
            }
        }) {}

    fun <P : RProps> RClass<P>.node(
        props: P,
        children: List<Any> = emptyList()
    ) = child(this, clone(props), children)

    fun <P : RProps, C : Component<P, *>> child(
        klazz: KClass<C>,
        handler: RHandler<P>
    ): ReactElement =
        klazz.rClass.invoke(handler)

    inline fun <P : RProps, reified C : Component<P, *>> child(noinline handler: RHandler<P>) =
        child(C::class, handler)

    fun <P : RProps, C : Component<P, *>> node(
        klazz: KClass<C>,
        props: P,
        children: List<Any> = emptyList()
    ): ReactElement =
        klazz.rClass.node(props, children)

    inline fun <P : RProps, reified C : Component<P, *>> node(props: P, children: List<Any> = emptyList()) =
        node(C::class, props, children)

    fun RProps.children() {
        childList.addAll(Children.toArray(children))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> RProps.children(value: T) {
        childList.add((children as (T) -> Any).invoke(value))
    }
}

open class RBuilderMultiple : RBuilder()

fun buildElements(handler: RBuilder.() -> Unit): dynamic {
    val nodes = RBuilder().apply(handler).childList
    return when (nodes.size) {
        0 -> null
        1 -> nodes.first()
        else -> createElement(Fragment, js {}, *nodes.toTypedArray())
    }
}

open class RBuilderSingle : RBuilder()

inline fun buildElement(handler: RBuilder.() -> Unit): ReactElement =
    RBuilder().apply(handler)
        .childList.first()
        .unsafeCast<ReactElement>()

open class RElementBuilder<out P : RProps>(open val attrs: P) : RBuilder() {
    fun attrs(handler: P.() -> Unit) {
        attrs.handler()
    }

    var key: String
        @Deprecated(message = "Write-only property", level = DeprecationLevel.HIDDEN)
        get() = error("")
        set(value) {
            attrs.key = value
        }

    var ref: RRef
        @Deprecated(message = "Write-only property", level = DeprecationLevel.HIDDEN)
        get() = error("")
        set(value) {
            attrs.ref = value
        }

    fun ref(handler: (dynamic) -> Unit) {
        attrs.ref(handler)
    }
}

typealias RHandler<P> = RElementBuilder<P>.() -> Unit

fun <P : RProps> forwardRef(handler: RBuilder.(RProps, RRef) -> Unit): RClass<P> {
    return rawForwardRef { props, ref ->
        buildElements { handler(props, ref) }
    }
}

typealias FunctionalComponent<P> = (props: P) -> dynamic

/**
 * Get functional component from [func]
 */
fun <P : RProps> functionalComponent(
    func: RBuilder.(props: P) -> Unit
): FunctionalComponent<P> {
    return { props: P ->
        buildElements {
            func(props)
        }
    }
}

/**
 * Append functional component [functionalComponent] as child of current builder
 */
@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
fun <P : RProps> RBuilder.child(
    functionalComponent: FunctionalComponent<P>,
    props: P = jsObject(),
    handler: RHandler<P> = {}
): ReactElement {
    return child(functionalComponent, props, handler)
}
