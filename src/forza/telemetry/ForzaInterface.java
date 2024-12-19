package forza.telemetry;

import java.net.*;
import java.util.Arrays;

public interface ForzaInterface {
         default Thread startConnection(int port) {
             return new Thread() {
                 private DatagramSocket datagramSocket;

                 @Override
                 public synchronized void run() {
                     super.run();
                     try {
                         datagramSocket = new DatagramSocket(port);
                         // Allocate the largest expected bytes - FM8
                         byte[] receive = new byte[ForzaTelemetryApi.FM8_PACKET_LENGTH];
                         DatagramPacket datagramPacket = new DatagramPacket(receive, receive.length);
                         int lastOrdinal = 0;
                         boolean isPaused = false, isConnected = false;
                         while (!interrupted()) {
                             try {
                                 datagramSocket.receive(datagramPacket);
                                 if (!isConnected) {
                                     ForzaTelemetryApi tempApi = new ForzaTelemetryApi(datagramPacket.getLength(), datagramPacket.getData());
                                     if (tempApi.getTimeStampMS() != 0) {
                                         lastOrdinal = tempApi.getOrdinal();
                                         //Set ForzaApi to null if game is paused, as all values will return 0
                                         if(!tempApi.getIsRaceOn()) tempApi = null;
                                         //Call onConnected when first data stream is received
                                         onConnected(tempApi, datagramPacket);
                                         isConnected = true;
                                     }
                                 }
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             //Send data to the ForzaApi parsing class
                             try {
                                 byte[] data = datagramPacket.getData();
                                 ForzaTelemetryApi api = new ForzaTelemetryApi(datagramPacket.getLength(),data);
                                 //Call onGamePaused when isRaceOn is false, call onGameUnpaused when true while game is paused
                                 if(!api.getIsRaceOn() && !isPaused){
                                     onGamePaused();
                                     isPaused= true;
                                 } else if (api.getIsRaceOn() && isPaused) {
                                     onGameUnpaused();
                                     isPaused = false;
                                 }
                                 //Call onCarChanged when ordinal changes
                                 if(api.getOrdinal() != lastOrdinal && !isPaused) {
                                     onCarChanged(api, new VehicleData(api));
                                     lastOrdinal = api.getOrdinal();
                                 }
                                 //Send datastream every single loop unless game is paused
                                 if(!isPaused) onDataReceived(api);

                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     } catch (SocketException e) {
                         throw new RuntimeException(e);
                     }
                 }

                 @Override
                 public void interrupt() {
                     super.interrupt();
                     datagramSocket.close();
                 }
             };
         }
         static String getDeviceIp() throws UnknownHostException { return InetAddress.getLocalHost().getHostAddress(); }
         void onDataReceived(ForzaTelemetryApi api);
         void onConnected(ForzaTelemetryApi api, DatagramPacket packet);
         void onGamePaused();
         void onGameUnpaused();
         void onCarChanged(ForzaTelemetryApi api, VehicleData data);
}
