package com.fasetto.fragmentjava.kotlin_lib

class VehicleData(private val forzaApi: ForzaTelemetryApi) {
    val carName: String
        get() = forzaApi.getCarName()
    val carClass: String
        get() = forzaApi.getCarClass()
    val performanceIndex: Int
        get() = forzaApi.getPerformanceIndex()
    val drivetrain: String
        get() = forzaApi.getDrivetrain()
    val numOfCylinders: Int
        get() = forzaApi.numOfCylinders
    val carType: String
        get() = forzaApi.getCarType()
    val ordinal: Int
        get() = forzaApi.getOrdinal()
}