package com.fasetto.fragmentjava.kotlin_lib

import android.util.Log
import java.lang.Exception
import java.lang.RuntimeException
import java.net.*
import kotlin.Throws

interface ForzaInterface {
    fun startConnection(port: Int): Thread? {
        return Thread {
            try {
                DatagramSocket(port).use { datagramSocket ->
                    // Allocate the largest expected bytes - FM8
                    val receive = ByteArray(ForzaTelemetryApi.FM8_PACKET_LENGTH)
                    val datagramPacket = DatagramPacket(receive, receive.size)
                    var lastOrdinal = 0
                    var isPaused = false
                    var isConnected = false
                    while (true) {
                        try {
                            datagramSocket.receive(datagramPacket)
                            Log.d("ForzaInterface", "UDP got " + datagramPacket.length + " bytes")
                            if (!isConnected) {
                                var tempApi: ForzaTelemetryApi? =
                                    ForzaTelemetryApi(datagramPacket.length, datagramPacket.data)
                                if (tempApi!!.timeStampMS != 0L) {
                                    lastOrdinal = tempApi.getOrdinal()
                                    //Set ForzaApi to null if game is paused, as all values will return 0
                                    if (!tempApi.getIsRaceOn()) {
                                        tempApi = null
                                    }
                                    //Call onConnected when first data stream is received
                                    onConnected(tempApi, datagramPacket)
                                    isConnected = true
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //Send data to the ForzaApi parsing class
                        try {
                            val data = datagramPacket.data
                            val api = ForzaTelemetryApi(datagramPacket.length, data)
                            //Call onGamePaused when isRaceOn is false, call onGameUnpaused when true while game is paused
                            if (!api.getIsRaceOn() && !isPaused) {
                                onGamePaused()
                                isPaused = true
                            } else if (api.getIsRaceOn() && isPaused) {
                                onGameUnpaused()
                                isPaused = false
                            }
                            //Call onCarChanged when ordinal changes
                            if (api.getOrdinal() !== lastOrdinal && !isPaused) {
                                onCarChanged(api, VehicleData(api))
                                lastOrdinal = api.getOrdinal()
                            }
                            //Send datastream every single loop unless game is paused
                            if (!isPaused) onDataReceived(api)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: SocketException) {
                throw RuntimeException(e)
            }
        }
    }

    fun onDataReceived(api: ForzaTelemetryApi?)
    fun onConnected(api: ForzaTelemetryApi?, packet: DatagramPacket?)
    fun onGamePaused()
    fun onGameUnpaused()
    fun onCarChanged(api: ForzaTelemetryApi?, data: VehicleData?)

    companion object {
        @get:Throws(UnknownHostException::class)
        val deviceIp: String?
            get() = InetAddress.getLocalHost().hostAddress
    }
}