@file:JsModule("react-table")
@file:JsNonModule

@file:Suppress("NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE")

package react.table

import kotlinext.js.Record

external val useRowSelect: PluginHook<Any>

external interface TableToggleAllRowsSelectedProps : TableToggleCommonProps

external interface TableToggleRowsSelectedProps : TableToggleCommonProps

external interface UseRowSelectOptions<D : Any> {
    var manualRowSelectedKey: IdType<D>
        @Deprecated(message = "Write-only property", level = DeprecationLevel.HIDDEN)
        get

    var autoResetSelectedRows: Boolean
        @Deprecated(message = "Write-only property", level = DeprecationLevel.HIDDEN)
        get

    var selectSubRows: Boolean
        @Deprecated(message = "Write-only property", level = DeprecationLevel.HIDDEN)
        get
}

external interface UseRowSelectHooks<D : Any> {
    val getToggleRowSelectedProps: Array<out PropGetter<D, TableToggleRowsSelectedProps>>
    val getToggleAllRowsSelectedProps: Array<out PropGetter<D, TableToggleAllRowsSelectedProps>>
    val getToggleAllPageRowsSelectedProps: Array<out PropGetter<D, TableToggleAllRowsSelectedProps>>
}

external interface UseRowSelectState<D : Any> {
    var selectedRowIds: Record<IdType<D>, Boolean>
}

external interface UseRowSelectInstanceProps<D : Any> {
    fun toggleRowSelected(rowId: IdType<D>, set: Boolean = definedExternally)
    fun toggleAllRowsSelected(value: Boolean = definedExternally)
    fun getToggleAllRowsSelectedProps(
        props: TableToggleAllRowsSelectedProps = definedExternally,
    ): TableToggleAllRowsSelectedProps

    fun getToggleAllPageRowsSelectedProps(
        props: TableToggleAllRowsSelectedProps = definedExternally,
    ): TableToggleAllRowsSelectedProps

    val isAllRowsSelected: Boolean
    val selectedFlatRows: Array<out Row<D>>
}

external interface UseRowSelectRowProps<D : Any> {
    val isSelected: Boolean
    val isSomeSelected: Boolean
    fun toggleRowSelected(set: Boolean = definedExternally)
    fun getToggleRowSelectedProps(
        props: TableToggleRowsSelectedProps = definedExternally,
    ): TableToggleRowsSelectedProps
}
