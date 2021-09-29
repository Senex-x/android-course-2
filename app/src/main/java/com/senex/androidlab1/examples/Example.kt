package com.senex.androidlab1.examples

internal interface ExampleInterfaceFoo {
    fun foo(string: String): Int
}

internal interface ExampleInterfaceBar {
    fun bar(string: String): String
}

internal open class ExampleObject(
    val data: String,
    var optional: String? = null
)

internal class ExampleChildFooImpl(
    data: String,
    optional: String? = null
) : ExampleObject(
    data,
    optional
), ExampleInterfaceFoo {
    override fun foo(string: String) =
        (string + data + optional).chars().sum()
}

internal class ExampleChildBarImpl(
    data: String,
    optional: String? = null
) : ExampleObject(
    data,
    optional
), ExampleInterfaceBar {
    // I just wanted to use streams for no reason.
    // Proper solution: (string + data + optional).uppercase()
    override fun bar(string: String) =
        buildString {
            (string + data + (optional ?: "empty")).chars()
                .map { index -> index.toChar().uppercaseChar().code }
                .forEach { index -> append(index.toChar()) }

        }
}