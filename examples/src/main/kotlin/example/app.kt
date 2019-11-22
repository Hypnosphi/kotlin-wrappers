package example

import kotlinext.js.*
import kotlinx.html.*
import react.*
import react.dom.*

enum class Theme(val backgroundColor: String, val color: String) {
    LIGHT("white", "black"),
    DARK("black", "white")
}

val ThemeContext = createContext(Theme.LIGHT)

fun RBuilder.app(props: RProps) {
    ThemeContext.Provider(Theme.DARK) {
        div {
            ThemeContext.Consumer { theme ->
                div {
                    attrs.jsStyle {
                        color = theme.color
                        backgroundColor = theme.backgroundColor
                    }
                    +"Hello from React 16.3"
                }
            }

            // Three different ways to define style properties are listed below
            attrs.jsStyle = js {
                width = "100px"
            }

            attrs {
                jsStyle {
                    height = "100px"
                }
            }

            attrs.jsStyle.backgroundColor = "red"

            // Setting an attribute
            attrs.attributes["title"] = "My title"

            // Setting a custom attribute
            attrs["my-attribute"] = 100
            attrs["class"] = "class"

            // Appending children from props
            props.children()

            // Unescaped content
            // TODO: doesnt work with live examples
//            attrs.unsafe { +"&nbsp;" }


            // Form elements https://facebook.github.io/react/docs/forms.html

            input {
                attrs.defaultValue = "foo"
            }

            input {
                attrs {
                    type = InputType.checkBox
                    defaultChecked = true
                }
            }

            textArea {
                attrs {
                    value = "bar"
                    defaultValue = "foobar"
                }
            }

            val options = listOf("foo", "bar", "baz")

            select {
                attrs.value = "foo"

                options.forEach {
                    option {
                        key = it
                        attrs.value = it
                        +it
                    }
                }
            }

            select {
                attrs.multiple = true
                attrs.values = setOf("foo", "bar")

                options.forEach {
                    option {
                        key = it
                        attrs.value = it
                        +it
                    }
                }
            }

            select {
                attrs.multiple = true
                attrs.values += "foo"
                attrs.values += "bar"

                options.forEach {
                    option {
                        key = it
                        attrs.value = it
                        +it
                    }
                }
            }
        }
    }
}
