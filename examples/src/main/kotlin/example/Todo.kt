package example

import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import react.*
import react.dom.*

/**
 * A to-do list implementation by Scott_Huang@qq.com
 */

interface TodoProps : RProps {
    var initialItems: List<String?>?
}

interface TodoState : RState {
    var items: List<String?>
    var text: String
}

class Todo(props: TodoProps) : RComponent<TodoProps, TodoState>(props) {
    override fun TodoState.init(props: TodoProps) {
        items = props.initialItems ?: emptyList()
        text = ""
    }

    override fun RBuilder.render() {
        div {
            input(type = InputType.text, name = "itemText") {
                key = "itemText"

                attrs {
                    value = state.text
                    placeholder = "Add a to-do item"
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        setState {
                            text = target.value
                        }
                    }
                }
            }

            button {
                +"Add"
                attrs {
                    onClickFunction = {
                        if (state.text.isNotEmpty()) {
                            setState {
                                items += text
                                text = ""
                            }
                        }
                    }
                }
            }

            h3 {
                ul {
                    for ((index, item) in state.items.withIndex()) {
                        li {
                            key = index.toString()
                            +item.toString()
                            button {
                                +"×"
                                attrs {
                                    onClickFunction = {
                                        setState {
                                            items = items.filterIndexed { i, _ -> i != index }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.todo(items: List<String?> = listOf("Hello", "World")) = child(Todo::class) {
    attrs.initialItems = items
}
