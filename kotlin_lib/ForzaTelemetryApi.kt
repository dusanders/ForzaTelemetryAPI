package com.fasetto.fragmentjava.kotlin_lib

import android.util.Log
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.DecimalFormat
import kotlin.Throws
import kotlin.experimental.and

class ForzaTelemetryApi(private val packetLength: Int, bytes: ByteArray) {
    /*
     * GETTERS FOR FORZA HORIZON 5 UDP STREAM DATA OUT.
     */  val isRaceOn: Boolean
    val timeStampMS: Long
    private val engineMaxRpm: Float
    private val engineIdleRpm: Float
    private val currentEngineRpm: Float
    private val accelerationX: Float
    private val accelerationY: Float
    private val accelerationZ: Float
    private val velocityX: Float
    private val velocityY: Float
    private val velocityZ: Float
    private val angularVelocityX: Float
    private val angularVelocityY: Float
    private val angularVelocityZ: Float
    private val yaw: Float
    private val pitch: Float
    private val roll: Float
    private val normalizedSuspensionTravelFrontLeft: Float
    private val normalizedSuspensionTravelFrontRight: Float
    private val normalizedSuspensionTravelRearLeft: Float
    private val normalizedSuspensionTravelRearRight: Float
    private val tireSlipRatioFrontLeft: Float
    private val tireSlipRatioFrontRight: Float
    private val tireSlipRatioRearLeft: Float
    private val tireSlipRatioRearRight: Float
    private val wheelRotationSpeedFrontLeft: Float
    private val wheelRotationSpeedFrontRight: Float
    private val wheelRotationSpeedRearLeft: Float
    private val wheelRotationSpeedRearRight: Float
    val wheelOnRumbleStripFrontLeft: Int
    val wheelOnRumbleStripFrontRight: Int
    val wheelOnRumbleStripRearLeft: Int
    val wheelOnRumbleStripRearRight: Int
    val wheelInPuddleDepthFrontLeft: Float
    val wheelInPuddleDepthFrontRight: Float
    val wheelInPuddleDepthRearLeft: Float
    val wheelInPuddleDepthRearRight: Float
    val surfaceRumbleFrontLeft: Float
    val surfaceRumbleFrontRight: Float
    val surfaceRumbleRearLeft: Float
    val surfaceRumbleRearRight: Float
    private val tireSlipAngleFrontLeft: Float
    private val tireSlipAngleFrontRight: Float
    private val tireSlipAngleRearLeft: Float
    private val tireSlipAngleRearRight: Float
    private val tireCombinedSlipFrontLeft: Float
    private val tireCombinedSlipFrontRight: Float
    private val tireCombinedSlipRearLeft: Float
    private val tireCombinedSlipRearRight: Float
    private val suspensionTravelMetersFrontLeft: Float
    private val suspensionTravelMetersFrontRight: Float
    private val suspensionTravelMetersRearLeft: Float
    private val suspensionTravelMetersRearRight: Float
    private val carClass: Int
    private val carPerformanceIndex: Int
    private val drivetrainType: Int
    val numOfCylinders: Int
    private var carType: Int? = null
    var objectHit: Long? = null

    //    public Byte getUnknown3() {
    //        return unknown3;
    //    }
    //
    //    public Byte getUnknown4() {
    //        return unknown4;
    //    }
    //
    //    public Byte getUnknown5() {
    //        return unknown5;
    //    }
    //
    //    public Byte getUnknown6() {
    //        return unknown6;
    //    }
    //
    //    public Byte getUnknown7() {
    //        return unknown7;
    //    }
    //
    //    public Byte getUnknown8() {
    //        return unknown8;
    //    }
    //private final Byte unknown3;
    //private final Byte unknown4;
    //private final Byte unknown5;
    //private final Byte unknown6;
    //private final Byte unknown7;
    //private final Byte unknown8;
    private val ordinal: Int
    private val positionX: Float
    private val positionY: Float
    private val positionZ: Float
    private val speed: Float
    private val power: Float
    private val torque: Float
    private val tireTempFrontLeft: Float
    private val tireTempFrontRight: Float
    private val tireTempRearLeft: Float
    private val tireTempRearRight: Float
    private val boost: Float
    private val fuel: Float
    private val distanceTraveled: Float
    private val bestLap: Float
    private val lastLap: Float
    private val currentLap: Float
    private val currentRaceTime: Float
    private val lapNumber: Short
    private val racePosition: Byte
    private val throttle: Byte
    private val brake: Byte
    private val clutch: Byte
    private val handbrake: Byte
    private val gear: Byte
    private val steer: Byte
    private val normalizedDrivingLine: Byte
    private val normalizedAIBrakeDifference: Byte
    private var tireWearFrontLeft: Float? = null
    private var tireWearFrontRight: Float? = null
    private var tireWearRearLeft: Float? = null
    private var tireWearRearRight: Float? = null
    private var trackID = 0
    var df: DecimalFormat

    /**
     * Create API instance. Parses the byte[] into telemetry values.
     * @param recvdLen Actual byte length from Java DatagramPacket
     * @param bytes Allocated byte array - length will not be actual received.
     * @throws Exception Thrown if failed to parse byte array
     */
    init {
        //Check we got a Forza packet
        if (!isFHPacket() || !isFM7Packet() || !isFM8Packet()) {
            Log.e(TAG, "Invalid byte length: " + packetLength + " allocated: " + bytes.size)
        }
        if (isFHPacket()) {
            Log.d(TAG, "Using Horizon parse...")
        } else if (isFM7Packet()) {
            Log.d(TAG, "Using FM7 parse...")
        } else if (isFM8Packet()) {
            Log.d(TAG, "Using FM8 parse...")
        }
        //Set decimal formatting
        df = DecimalFormat("###.##")
        df.roundingMode = RoundingMode.DOWN
        //Wrap bytes in a ByteBuffer for faster parsing. The encoding is in Little Endian
        val bb = ByteBuffer.wrap(bytes)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        //Set data by going through the bytebuffer
        isRaceOn = getFromBuffer(bb, Int::class.javaPrimitiveType) == 1
        timeStampMS = getFromBuffer(bb, Long::class.javaPrimitiveType)
        engineMaxRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
        engineIdleRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
        currentEngineRpm = getFromBuffer(bb, Float::class.javaPrimitiveType)
        accelerationX = getFromBuffer(bb, Float::class.javaPrimitiveType)
        accelerationY = getFromBuffer(bb, Float::class.javaPrimitiveType)
        accelerationZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
        velocityX = getFromBuffer(bb, Float::class.javaPrimitiveType)
        velocityY = getFromBuffer(bb, Float::class.javaPrimitiveType)
        velocityZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
        angularVelocityX = getFromBuffer(bb, Float::class.javaPrimitiveType)
        angularVelocityY = getFromBuffer(bb, Float::class.javaPrimitiveType)
        angularVelocityZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
        yaw = getFromBuffer(bb, Float::class.javaPrimitiveType)
        pitch = getFromBuffer(bb, Float::class.javaPrimitiveType)
        roll = getFromBuffer(bb, Float::class.javaPrimitiveType)
        normalizedSuspensionTravelFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        normalizedSuspensionTravelFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        normalizedSuspensionTravelRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        normalizedSuspensionTravelRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipRatioFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipRatioFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipRatioRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipRatioRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelRotationSpeedFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelRotationSpeedFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelRotationSpeedRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelRotationSpeedRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelOnRumbleStripFrontLeft = getFromBuffer(bb, Int::class.javaPrimitiveType)
        wheelOnRumbleStripFrontRight = getFromBuffer(bb, Int::class.javaPrimitiveType)
        wheelOnRumbleStripRearLeft = getFromBuffer(bb, Int::class.javaPrimitiveType)
        wheelOnRumbleStripRearRight = getFromBuffer(bb, Int::class.javaPrimitiveType)
        wheelInPuddleDepthFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelInPuddleDepthFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelInPuddleDepthRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        wheelInPuddleDepthRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        surfaceRumbleFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        surfaceRumbleFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        surfaceRumbleRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        surfaceRumbleRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipAngleFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipAngleFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipAngleRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireSlipAngleRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireCombinedSlipFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireCombinedSlipFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireCombinedSlipRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireCombinedSlipRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        suspensionTravelMetersFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        suspensionTravelMetersFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        suspensionTravelMetersRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        suspensionTravelMetersRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        ordinal = getFromBuffer(bb, Int::class.javaPrimitiveType)
        carClass = getFromBuffer(bb, Int::class.javaPrimitiveType)
        carPerformanceIndex = getFromBuffer(bb, Int::class.javaPrimitiveType)
        drivetrainType = getFromBuffer(bb, Int::class.javaPrimitiveType)
        numOfCylinders = getFromBuffer(bb, Int::class.javaPrimitiveType)
        // Only Horizon gets these values
        if (isFHPacket()) {
            carType = getFromBuffer(bb, Int::class.javaPrimitiveType)
            objectHit = getFromBuffer<Long>(bb)
        } else {
            // Clear for Motorsport
            carType = 0
            objectHit = 0L
        }
        positionX = getFromBuffer(bb, Float::class.javaPrimitiveType)
        positionY = getFromBuffer(bb, Float::class.javaPrimitiveType)
        positionZ = getFromBuffer(bb, Float::class.javaPrimitiveType)
        speed = getFromBuffer(bb, Float::class.javaPrimitiveType)
        power = getFromBuffer(bb, Float::class.javaPrimitiveType)
        torque = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireTempFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireTempFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireTempRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
        tireTempRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
        boost = getFromBuffer(bb, Float::class.javaPrimitiveType)
        fuel = getFromBuffer(bb, Float::class.javaPrimitiveType)
        distanceTraveled = getFromBuffer(bb, Float::class.javaPrimitiveType)
        bestLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
        lastLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
        currentLap = getFromBuffer(bb, Float::class.javaPrimitiveType)
        currentRaceTime = getFromBuffer(bb, Float::class.javaPrimitiveType)
        lapNumber = getFromBuffer(bb, Short::class.javaPrimitiveType)
        racePosition = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        throttle = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        brake = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        clutch = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        handbrake = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        gear = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        steer = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        normalizedDrivingLine = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        normalizedAIBrakeDifference = getFromBuffer(bb, Byte::class.javaPrimitiveType)
        // Horizon doesn't get these values - only for Motorsport
        if (isFM8Packet() || isFM7Packet()) {
            // Set for Motorsport
            tireWearFrontLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireWearFrontRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireWearRearLeft = getFromBuffer(bb, Float::class.javaPrimitiveType)
            tireWearRearRight = getFromBuffer(bb, Float::class.javaPrimitiveType)
            trackID = getFromBuffer(bb, Int::class.javaPrimitiveType)
        } else {
            // These are not available in Horizon - only FM7/8
            tireWearFrontLeft = 0f
            tireWearFrontRight = 0f
            tireWearRearLeft = 0f
            tireWearRearRight = 0f
            trackID = 0
        }
    }

    fun isFM7Packet(): Boolean {
        return packetLength == DASH_PACKET_LENGTH
    }

    fun isFHPacket(): Boolean {
        return packetLength == FH4_PACKET_LENGTH
    }

    fun isFM8Packet(): Boolean {
        return packetLength == FM8_PACKET_LENGTH
    }

    fun getTireWearFrontLeft(): Int {
        return Math.round(tireWearFrontLeft!! * 100)
    }

    fun getTireWearFrontRight(): Int {
        return Math.round(tireWearFrontRight!! * 100)
    }

    fun getTireWearRearLeft(): Int {
        return Math.round(tireWearRearLeft!! * 100)
    }

    fun getTireWearRearRight(): Int {
        return Math.round(tireWearRearRight!! * 100)
    }

    fun getTrackId(): Int {
        return trackID
    }

    fun getEngineMaxRpm(): Int {
        return Math.round(engineMaxRpm)
    }

    fun getEngineIdleRpm(): Int {
        return Math.round(engineIdleRpm)
    }

    fun getCurrentEngineRpm(): Int {
        return Math.round(currentEngineRpm)
    }

    fun getAccelerationX(): Int {
        return Math.round(accelerationX * 100)
    }

    fun getAccelerationY(): Int {
        return Math.round(accelerationY * 100)
    }

    fun getAccelerationZ(): Int {
        return Math.round(accelerationZ * 100)
    }

    fun getVelocityX(): Int {
        return Math.round(velocityX * 100)
    }

    fun getVelocityY(): Int {
        return Math.round(velocityY * 100)
    }

    fun getVelocityZ(): Int {
        return Math.round(velocityZ * 100)
    }

    fun getAngularVelocityX(): Int {
        return Math.round(angularVelocityX * 100)
    }

    fun getAngularVelocityY(): Int {
        return Math.round(angularVelocityY * 100)
    }

    fun getAngularVelocityZ(): Int {
        return Math.round(angularVelocityZ * 100)
    }

    fun getYaw(): Int {
        return Math.round(yaw * 100)
    }

    fun getPitch(): Int {
        return Math.round(pitch * 100)
    }

    fun getRoll(): Int {
        return Math.round(roll * 100)
    }

    fun getNormalizedSuspensionTravelFrontLeft(): Int {
        return Math.round(normalizedSuspensionTravelFrontLeft * 100)
    }

    fun getNormalizedSuspensionTravelFrontRight(): Int {
        return Math.round(normalizedSuspensionTravelFrontRight * 100)
    }

    fun getNormalizedSuspensionTravelRearLeft(): Int {
        return Math.round(normalizedSuspensionTravelRearLeft * 100)
    }

    fun getNormalizedSuspensionTravelRearRight(): Int {
        return Math.round(normalizedSuspensionTravelRearRight * 100)
    }

    fun getTireSlipRatioFrontLeft(): Int {
        return Math.round(tireSlipRatioFrontLeft * 100)
    }

    fun getTireSlipRatioFrontRight(): Int {
        return Math.round(tireSlipRatioFrontRight * 100)
    }

    fun getTireSlipRatioRearLeft(): Int {
        return Math.round(tireSlipRatioRearLeft * 100)
    }

    fun getTireSlipRatioRearRight(): Int {
        return Math.round(tireSlipRatioRearRight * 100)
    }

    fun getWheelRotationSpeedFrontLeft(): Int {
        return Math.round(wheelRotationSpeedFrontLeft * 100)
    }

    fun getWheelRotationSpeedFrontRight(): Int {
        return Math.round(wheelRotationSpeedFrontRight * 100)
    }

    fun getWheelRotationSpeedRearLeft(): Int {
        return Math.round(wheelRotationSpeedRearLeft * 100)
    }

    fun getWheelRotationSpeedRearRight(): Int {
        return Math.round(wheelRotationSpeedRearRight * 100)
    }

    fun getTireSlipAngleFrontLeft(): Long {
        return angle(tireSlipAngleFrontLeft)
    }

    fun getTireSlipAngleFrontRight(): Long {
        return angle(tireSlipAngleFrontRight)
    }

    fun getTireSlipAngleRearLeft(): Long {
        return angle(tireSlipAngleRearLeft)
    }

    fun getTireSlipAngleRearRight(): Long {
        return angle(tireSlipAngleRearRight)
    }

    fun getTireCombinedSlipFrontLeft(): Int {
        return Math.round(tireCombinedSlipFrontLeft * 100)
    }

    fun getTireCombinedSlipFrontRight(): Int {
        return Math.round(tireCombinedSlipFrontRight * 100)
    }

    fun getTireCombinedSlipRearLeft(): Int {
        return Math.round(tireCombinedSlipRearLeft * 100)
    }

    fun getTireCombinedSlipRearRight(): Int {
        return Math.round(tireCombinedSlipRearRight * 100)
    }

    fun getSuspensionTravelMetersFrontLeft(): Int {
        return Math.round(suspensionTravelMetersFrontLeft * 100)
    }

    fun getSuspensionTravelMetersFrontRight(): Int {
        return Math.round(suspensionTravelMetersFrontRight * 100)
    }

    fun getSuspensionTravelMetersRearLeft(): Int {
        return Math.round(suspensionTravelMetersRearLeft * 100)
    }

    fun getSuspensionTravelMetersRearRight(): Int {
        return Math.round(suspensionTravelMetersRearRight * 100)
    }

    fun getCarClass(): String {
        var result = "-"
        when (carClass) {
            0 -> result = "D"
            1 -> result = "C"
            2 -> result = "B"
            3 -> result = "A"
            4 -> result = "S1"
            5 -> result = "S2"
            6 -> result = "X"
        }
        return result
    }

    fun getPerformanceIndex(): Int {
        return carPerformanceIndex
    }

    fun getDrivetrain(): String {
        var result = "-"
        when (drivetrainType) {
            0 -> result = "FWD"
            1 -> result = "RWD"
            2 -> result = "AWD"
        }
        return result
    }

    fun getCarType(): String {
        var result = "Unknown (" + getCarType() + ")"
        when (carType) {
            11 -> result = "Modern Super Cars"
            12 -> result = "Retro Super Cars"
            13 -> result = "Hyper Cars"
            14 -> result = "Retro Saloons"
            16 -> result = "Vans & Utility"
            17 -> result = "Retro Sports Cars"
            18 -> result = "Modern Sports Cars"
            19 -> result = "Super Saloons"
            20 -> result = "Classic Racers"
            21 -> result = "Cult Cars"
            22 -> result = "Rare Classics"
            25 -> result = "Super Hot Hatch"
            29 -> result = "Rods & Customs"
            30 -> result = "Retro Muscle"
            31 -> result = "Modern Muscle"
            32 -> result = "Retro Rally"
            33 -> result = "Classic Rally"
            34 -> result = "Rally Monsters"
            35 -> result = "Modern Rally"
            36 -> result = "GT Cars"
            37 -> result = "Super GT"
            38 -> result = "Extreme Offroad"
            39 -> result = "Sports Utility Heroes"
            40 -> result = "Offroad"
            41 -> result = "Offroad Buggies"
            42 -> result = "Classic Sports Cars"
            43 -> result = "Track Toys"
            44 -> result = "Vintage Racers"
            45 -> result = "Trucks"
        }
        return result
    }

    fun getPositionX(): Int {
        return Math.round(positionX * 1000)
    }

    fun getPositionY(): Int {
        return Math.round(positionY * 1000)
    }

    fun getPositionZ(): Int {
        return Math.round(positionZ * 1000)
    }

    fun getSpeedMps(): Int {
        return Math.round(speed)
    }

    fun getSpeedKph(): Int {
        return Math.round(getSpeedMps() * 3.6f)
    }

    fun getSpeedMph(): Int {
        return Math.round(getSpeedMps() * 2.23694f)
    }

    fun getPower(): Int {
        return Math.round(power)
    }

    fun getHorsePower(): Int {
        return Math.round(getPower() * 0.00134102f)
    }

    fun getTorque(): Int {
        return Math.round(torque)
    }

    fun getTireTempFrontLeft(): Int {
        return Math.round(tireTempFrontLeft)
    }

    fun getTireTempFrontRight(): Int {
        return Math.round(tireTempFrontRight)
    }

    fun getTireTempRearLeft(): Int {
        return Math.round(tireTempRearLeft)
    }

    fun getTireTempRearRight(): Int {
        return Math.round(tireTempRearRight)
    }

    fun getTireTempAverageFront(): Int {
        return Math.round(
            getAverage(
                getTireTempFrontLeft().toFloat(),
                getTireTempFrontRight().toFloat()
            )
        )
    }

    fun getTireTempAverageRear(): Int {
        return Math.round(
            getAverage(
                getTireTempRearLeft().toFloat(),
                getTireTempRearRight().toFloat()
            )
        )
    }

    fun getTireTempAverageLeft(): Int {
        return Math.round(
            getAverage(
                getTireTempFrontLeft().toFloat(),
                getTireTempRearLeft().toFloat()
            )
        )
    }

    fun getTireTempAverageRight(): Int {
        return Math.round(
            getAverage(
                getTireTempFrontRight().toFloat(),
                getTireTempRearRight().toFloat()
            )
        )
    }

    fun getTireTempAverageTotal(): Int {
        return Math.round(
            getAverage(
                getTireTempFrontLeft().toFloat(),
                getTireTempFrontRight().toFloat(),
                getTireTempRearLeft().toFloat(),
                getTireTempRearRight()
                    .toFloat()
            )
        )
    }

    fun getTireTempFrontLeft(isCelsius: Boolean): Int {
        return if (isCelsius) {
            Math.round((tireTempFrontLeft - 32) * 5 / 9)
        } else Math.round(tireTempFrontLeft)
    }

    fun getTireTempFrontRight(isCelsius: Boolean): Int {
        return if (isCelsius) {
            Math.round((tireTempFrontRight - 32) * 5 / 9)
        } else Math.round(tireTempFrontRight)
    }

    fun getTireTempRearLeft(isCelsius: Boolean): Int {
        return if (isCelsius) {
            Math.round((tireTempRearLeft - 32) * 5 / 9)
        } else Math.round(tireTempRearLeft)
    }

    fun getTireTempRearRight(isCelsius: Boolean): Int {
        return if (isCelsius) {
            Math.round((tireTempRearRight - 32) * 5 / 9)
        } else Math.round(tireTempRearRight)
    }

    fun getTireTempAverageFront(isCelsius: Boolean): Int {
        val avg = getAverage(getTireTempFrontLeft().toFloat(), getTireTempFrontRight().toFloat())
        return if (isCelsius) {
            Math.round((avg - 32) * 5 / 9)
        } else Math.round(avg)
    }

    fun getTireTempAverageRear(isCelsius: Boolean): Int {
        val avg = getAverage(getTireTempRearLeft().toFloat(), getTireTempRearRight().toFloat())
        return if (isCelsius) {
            Math.round((avg - 32) * 5 / 9)
        } else Math.round(avg)
    }

    fun getTireTempAverageLeft(isCelsius: Boolean): Int {
        val avg = getAverage(getTireTempFrontLeft().toFloat(), getTireTempRearLeft().toFloat())
        return if (isCelsius) {
            Math.round((avg - 32) * 5 / 9)
        } else Math.round(avg)
    }

    fun getTireTempAverageRight(isCelsius: Boolean): Int {
        val avg = getAverage(getTireTempFrontRight().toFloat(), getTireTempRearRight().toFloat())
        return if (isCelsius) {
            Math.round((avg - 32) * 5 / 9)
        } else Math.round(avg)
    }

    fun getTireTempAverageTotal(isCelsius: Boolean): Int {
        val avg = getAverage(
            getTireTempFrontLeft().toFloat(),
            getTireTempFrontRight().toFloat(),
            getTireTempRearLeft().toFloat(),
            getTireTempRearRight()
                .toFloat()
        )
        return if (isCelsius) {
            Math.round((avg - 32) * 5 / 9)
        } else Math.round(avg)
    }

    fun getBoost(): Int {
        return Math.round(boost)
    }

    fun getFuel(): Float {
        return fuel * 100;
//        return BigDecimal(fuel * 100).setScale(2, RoundingMode.DOWN).toFloat()
    }

    fun getDistanceTraveled(): Float {
        return distanceTraveled
    }

    fun getBestLap(): Float {
        return bestLap
    }

    fun getLastLap(): Float {
        return lastLap
    }

    fun getCurrentLap(): Float {
        return currentLap
    }

    fun getCurrentRaceTime(): Float {
        return currentRaceTime
    }

    fun getLapNumber(): Short {
        return lapNumber
    }

    fun getRacePosition(): Byte {
        return racePosition
    }

    fun getThrottle(): Int {
        return (throttle and 0xff.toByte()) * 100 / 255
    }

    fun getBrake(): Int {
        return (brake and 0xff.toByte()) * 100 / 255
    }

    fun getClutch(): Int {
        return (clutch and 0xff.toByte()) * 100 / 255
    }

    fun getHandbrake(): Int {
        return (handbrake and 0xff.toByte()) * 100 / 255
    }

    fun getGear(): Int {
        return (gear and 0xff.toByte()).toInt()
    }

    fun getSteer(): Int {
        return (steer and 0xff.toByte()) * 100 / 127
    }

    fun getNormalizedDrivingLine(): Int {
        return (normalizedDrivingLine and 0xff.toByte()) * 100 / 127
    }

    fun getNormalizedAIBrakeDifference(): Int {
        return (normalizedAIBrakeDifference and 0xff.toByte()) * 100 / 127
    }

    fun getOrdinal(): Int {
        return ordinal;
    }

    fun getIsRaceOn(): Boolean {
        return isRaceOn;
    }

    fun getCarName(): String {
        /*
        String[] ordinals = sb.toString().split("\n");
        for (String line : ordinals) {
            String name = line.trim().split(",")[0];
            String ordinal = line.trim().split(",")[1];
            if(Objects.equals(getCarOrdinal(), Integer.valueOf(ordinal))){
                return name;
            }
        }*/return "Unknown"
    }

    fun getAverageVelocity(): Int {
        return Math.round(
            getVector3DLength(
                getVelocityX().toFloat(),
                getVelocityY().toFloat(),
                getVelocityZ().toFloat()
            )
        )
    }

    private fun getAverage(valueOne: Float, valueTwo: Float): Float {
        return (valueOne + valueTwo) / 2f
    }

    private fun getAverage(
        valueOne: Float,
        valueTwo: Float,
        valueThree: Float,
        valueFour: Float
    ): Float {
        return (valueOne + valueTwo + valueThree + valueFour) / 4f
    }

    private fun getVector3DLength(x: Float, y: Float, z: Float): Float {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    fun angle(i: Float): Long {
        return Math.round(i * 180 / Math.PI)
    }

    override fun toString(): String {
        return "{" +
                " isRaceOn='" + isRaceOn + "'" +
                ", timeStampMS='" + timeStampMS + "'" +
                ", engineMaxRpm='" + getEngineMaxRpm() + "'" +
                ", engineIdleRpm='" + getEngineIdleRpm() + "'" +
                ", currentEngineRpm='" + getCurrentEngineRpm() + "'" +
                ", accelerationX='" + getAccelerationX() + "'" +
                ", accelerationY='" + getAccelerationY() + "'" +
                ", accelerationZ='" + getAccelerationZ() + "'" +
                ", velocityX='" + getVelocityX() + "'" +
                ", velocityY='" + getVelocityY() + "'" +
                ", velocityZ='" + getVelocityZ() + "'" +
                ", averageVelocityZ='" + getAverageVelocity() + "'" +
                ", angularVelocityX='" + getAngularVelocityX() + "'" +
                ", angularVelocityY='" + getAngularVelocityY() + "'" +
                ", angularVelocityZ='" + getAngularVelocityZ() + "'" +
                ", yaw='" + getYaw() + "'" +
                ", pitch='" + getPitch() + "'" +
                ", roll='" + getRoll() + "'" +
                ", normalizedSuspensionTravelFrontLeft='" + getNormalizedSuspensionTravelFrontLeft() + "'" +
                ", normalizedSuspensionTravelFrontRight='" + getNormalizedSuspensionTravelFrontRight() + "'" +
                ", normalizedSuspensionTravelRearLeft='" + getNormalizedSuspensionTravelRearLeft() + "'" +
                ", normalizedSuspensionTravelRearRight='" + getNormalizedSuspensionTravelRearRight() + "'" +
                ", tireSlipRatioFrontLeft='" + getTireSlipRatioFrontLeft() + "'" +
                ", tireSlipRatioFrontRight='" + getTireSlipRatioFrontRight() + "'" +
                ", tireSlipRatioRearLeft='" + getTireSlipRatioRearLeft() + "'" +
                ", tireSlipRatioRearRight='" + getTireSlipRatioRearRight() + "'" +
                ", wheelRotationSpeedFrontLeft='" + getWheelRotationSpeedFrontLeft() + "'" +
                ", wheelRotationSpeedFrontRight='" + getWheelRotationSpeedFrontRight() + "'" +
                ", wheelRotationSpeedRearLeft='" + getWheelRotationSpeedRearLeft() + "'" +
                ", wheelRotationSpeedRearRight='" + getWheelRotationSpeedRearRight() + "'" +
                ", wheelOnRumbleStripFrontLeft='" + wheelOnRumbleStripFrontLeft + "'" +
                ", wheelOnRumbleStripFrontRight='" + wheelOnRumbleStripFrontRight + "'" +
                ", wheelOnRumbleStripRearLeft='" + wheelOnRumbleStripRearLeft + "'" +
                ", wheelOnRumbleStripRearRight='" + wheelOnRumbleStripRearRight + "'" +
                ", wheelInPuddleDepthFrontLeft='" + wheelInPuddleDepthFrontLeft + "'" +
                ", wheelInPuddleDepthFrontRight='" + wheelInPuddleDepthFrontRight + "'" +
                ", wheelInPuddleDepthRearLeft='" + wheelInPuddleDepthRearLeft + "'" +
                ", wheelInPuddleDepthRearRight='" + wheelInPuddleDepthRearRight + "'" +
                ", surfaceRumbleFrontLeft='" + surfaceRumbleFrontLeft + "'" +
                ", surfaceRumbleFrontRight='" + surfaceRumbleFrontRight + "'" +
                ", surfaceRumbleRearLeft='" + surfaceRumbleRearLeft + "'" +
                ", surfaceRumbleRearRight='" + surfaceRumbleRearRight + "'" +
                ", tireSlipAngleFrontLeft='" + getTireSlipAngleFrontLeft() + "'" +
                ", tireSlipAngleFrontRight='" + getTireSlipAngleFrontRight() + "'" +
                ", tireSlipAngleRearLeft='" + getTireSlipAngleRearLeft() + "'" +
                ", tireSlipAngleRearRight='" + getTireSlipAngleRearRight() + "'" +
                ", tireCombinedSlipFrontLeft='" + getTireCombinedSlipFrontLeft() + "'" +
                ", tireCombinedSlipFrontRight='" + getTireCombinedSlipFrontRight() + "'" +
                ", tireCombinedSlipRearLeft='" + getTireCombinedSlipRearLeft() + "'" +
                ", tireCombinedSlipRearRight='" + getTireCombinedSlipRearRight() + "'" +
                ", suspensionTravelMetersFrontLeft='" + getSuspensionTravelMetersFrontLeft() + "'" +
                ", suspensionTravelMetersFrontRight='" + getSuspensionTravelMetersFrontRight() + "'" +
                ", suspensionTravelMetersRearLeft='" + getSuspensionTravelMetersRearLeft() + "'" +
                ", suspensionTravelMetersRearRight='" + getSuspensionTravelMetersRearRight() + "'" +
                ", carClass='" + getCarClass() + "'" +
                ", carPerformanceIndex='" + getPerformanceIndex() + "'" +
                ", drivetrainType='" + getDrivetrain() + "'" +
                ", numCylinders='" + numOfCylinders + "'" +
                ", carType='" + getCarType() + "'" +
                ", objectHit1='" + objectHit + "'" +
                ", carOrdinal='" + ordinal + "'" +
                ", positionX='" + getPositionX() + "'" +
                ", positionY='" + getPositionY() + "'" +
                ", positionZ='" + getPositionZ() + "'" +
                ", speedMps='" + getSpeedMps() + "'" +
                ", speedMph='" + getSpeedMph() + "'" +
                ", speedKph='" + getSpeedKph() + "'" +
                ", power='" + getPower() + "'" +
                ", horsepower='" + getHorsePower() + "'" +
                ", torque='" + getTorque() + "'" +
                ", tireTempFrontLeft='" + getTireTempFrontLeft() + "'" +
                ", tireTempFrontRight='" + getTireTempFrontRight() + "'" +
                ", tireTempRearLeft='" + getTireTempRearLeft() + "'" +
                ", tireTempRearRight='" + getTireTempRearRight() + "'" +
                ", boost='" + getBoost() + "'" +
                ", fuel='" + getFuel() + "'" +
                ", distanceTraveled='" + getDistanceTraveled() + "'" +
                ", bestLap='" + getBestLap() + "'" +
                ", lastLap='" + getLastLap() + "'" +
                ", currentLap='" + getCurrentLap() + "'" +
                ", currentRaceTime='" + getCurrentRaceTime() + "'" +
                ", lapNumber='" + getLapNumber() + "'" +
                ", racePosition='" + getRacePosition() + "'" +
                ", accel='" + getThrottle() + "'" +
                ", brake='" + getBrake() + "'" +
                ", clutch='" + getClutch() + "'" +
                ", handbrake='" + getHandbrake() + "'" +
                ", gear='" + getGear() + "'" +
                ", steer='" + getSteer() + "'" +
                ", normalizedDrivingLine='" + getNormalizedDrivingLine() + "'" +
                ", normalizedAIBrakeDifference='" + getNormalizedAIBrakeDifference() + "'" +
                "}"
    }

    companion object {
        private const val TAG = "ForzaTelemetryApi"
        const val DASH_PACKET_LENGTH = 311 // FM7
        const val FH4_PACKET_LENGTH = 324 // FH4
        const val FM8_PACKET_LENGTH = 331 // FM8

        //Method to check if selected type length is not overflowing the length of the bytebuffer
        private fun checkBuffer(buffer: ByteBuffer, size: Int): Boolean {
            return buffer.hasRemaining() && buffer.remaining() >= size
        }

        //Return data if requirements are met, sets default value to 0 otherwise
        @Throws(Exception::class)
        private fun <T> getFromBuffer(buffer: ByteBuffer, type: Class<T>?): T {
            when (type!!.name) {
                "int" -> return (if (checkBuffer(buffer, 4)) buffer.int else 0) as T
                "long" -> return (if (checkBuffer(
                        buffer,
                        4
                    )
                ) Integer.toUnsignedLong(buffer.int) else 0L) as T
                "byte" -> return (if (checkBuffer(buffer, 1)) buffer.get() else 0) as T
                "float" -> return (if (checkBuffer(buffer, 4)) buffer.float else 0f) as T
                "short" -> return (if (checkBuffer(buffer, 2)) buffer.short else 0) as T
            }
            throw Exception("Invalid Type")
        }

        //Object hit workaround. Unknown what this is yet. Forza Mystery!
        private fun <T> getFromBuffer(buffer: ByteBuffer): T {
            return (if (checkBuffer(buffer, 8)) buffer.long else 0L) as T
        }
    }
}