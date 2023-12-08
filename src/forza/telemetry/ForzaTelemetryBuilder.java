package forza.telemetry;

public class ForzaTelemetryBuilder {
    ForzaInterface listener;
    Thread connectionThread;
    int port = 5300;

    public ForzaTelemetryBuilder(){} //Used for default
    public ForzaTelemetryBuilder(int port) {
        this.port = port;
    }

    public ForzaTelemetryBuilder addListener(ForzaInterface listener) {
        this.listener = listener;
        return this;
    }

    public Thread getThread() {
        if(connectionThread == null) {
            connectionThread = listener.startConnection(port);
        }
        return connectionThread;
    }
}
