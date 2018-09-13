package styled

import kotlinext.js.asJsObject
import kotlinx.css.Color
import kotlinx.css.Display
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import kotlin.test.asserter


class TestNested {
    @kotlin.test.Test
    fun testNested() {
        class Element : RComponent<RProps, RState>() {
            override fun RBuilder.render() {
                styledDiv {
                    css {
                        display = Display.block

                        firstChild {
                            color = Color.black
                        }
                    }
                }
            }
        }

        val element = Element()

        asserter.assertEquals("", "foo", element.asJsObject().toString())
    }
}
