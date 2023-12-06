package com.fasetto.fragmentjava.kotlin_lib

class ForzaTelemetryBuilder {
    var listener: ForzaInterface? = null
    var port = 5300

    constructor() {} //Used for default
    constructor(port: Int) {
        this.port = port
    }

    fun addListener(listener: ForzaInterface?): ForzaTelemetryBuilder {
        this.listener = listener
        return this
    }

    val thread: Thread?
        get() = listener!!.startConnection(port)
}