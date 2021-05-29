@file:Suppress("NOTHING_TO_INLINE")

package react

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

typealias RDependenciesArray = Array<dynamic>
typealias RDependenciesList = List<dynamic>

typealias RSetState<T> = (value: T) -> Unit

/**
 * Only works inside [functionalComponent]
 * @see https://reactjs.org/docs/hooks-state.html
 */
inline fun <T> useState(initValue: T): RStateDelegate<T> {
    val (state, setState) = rawUseState(initValue)
    return RStateDelegate(state as T, setState as RSetState<T>)
}

/**
 * Only works inside [functionalComponent]
 * @see https://reactjs.org/docs/hooks-state.html
 */
inline fun <T> useState(noinline valueInitializer: () -> T): RStateDelegate<T> {
    val (state, setState) = rawUseState(valueInitializer)
    return RStateDelegate(state as T, setState as RSetState<T>)
}

/**
 * Only works inside [functionalComponent]
 * @see https://reactjs.org/docs/hooks-state.html
 */
class RStateDelegate<T>(
    private val state: T,
    private val setState: RSetState<T>
) : ReadWriteProperty<Nothing?, T> {
    operator fun component1(): T = state
    operator fun component2(): RSetState<T> = setState

    override operator fun getValue(
        thisRef: Nothing?,
        property: KProperty<*>
    ): T =
        state

    override operator fun setValue(
        thisRef: Nothing?,
        property: KProperty<*>,
        value: T
    ) {
        setState(value)
    }
}

typealias RReducer<S, A> = (state: S, action: A) -> S
typealias RDispatch<A> = (action: A) -> Unit

fun <S, A> useReducer(reducer: RReducer<S, A>, initState: S, initialAction: A? = null): Pair<S, RDispatch<A>> {
    val jsTuple = if (initialAction != null) {
        rawUseReducer(reducer, initState, initialAction)
    } else {
        rawUseReducer(reducer, initState)
    }
    val currentState = jsTuple[0] as S
    val dispatch = jsTuple[1] as RDispatch<A>
    return currentState to dispatch
}

fun <S, A> useReducer(reducer: RReducer<S?, A>): Pair<S?, RDispatch<A>> {
    return useReducer(reducer, null)
}

typealias RCleanup = () -> Unit

fun useEffectWithCleanup(dependencies: RDependenciesList? = null, effect: () -> RCleanup) {
    if (dependencies != null) {
        rawUseEffect(effect, dependencies.toTypedArray())
    } else {
        rawUseEffect(effect)
    }
}

fun useEffect(dependencies: RDependenciesList? = null, effect: () -> Unit) {
    val rawEffect = {
        effect()
        undefined
    }
    if (dependencies != null) {
        rawUseEffect(rawEffect, dependencies.toTypedArray())
    } else {
        rawUseEffect(rawEffect)
    }
}

fun useLayoutEffectWithCleanup(dependencies: RDependenciesList? = null, effect: () -> RCleanup) {
    if (dependencies != null) {
        rawUseLayoutEffect(effect, dependencies.toTypedArray())
    } else {
        rawUseLayoutEffect(effect)
    }
}

fun useLayoutEffect(dependencies: RDependenciesList? = null, effect: () -> Unit) {
    val rawEffect = {
        effect()
        undefined
    }
    if (dependencies != null) {
        rawUseLayoutEffect(rawEffect, dependencies.toTypedArray())
    } else {
        rawUseLayoutEffect(rawEffect)
    }
}

inline fun <T : Function<*>> useCallback(
    vararg dependencies: dynamic,
    callback: T,
): T =
    useCallback(callback, dependencies)

inline fun <T> useMemo(
    vararg dependencies: dynamic,
    noinline callback: () -> T,
): T =
    useMemo(callback, dependencies)
