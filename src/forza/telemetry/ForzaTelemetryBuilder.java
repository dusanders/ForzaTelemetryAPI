package forza.telemetry;

public class ForzaTelemetryBuilder {
    ForzaInterface listener;
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
        return listener.startConnection(port);
    }
}
