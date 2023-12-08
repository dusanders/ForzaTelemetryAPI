package forza.telemetry;

public class VehicleData {
    private ForzaTelemetryApi forzaApi;
    public VehicleData(ForzaTelemetryApi forzaApi) {
        this.forzaApi = forzaApi;
    }
    public String getCarName() {
        return forzaApi.getCarName();
    }

    public String getCarClass() {
        return forzaApi.getCarClass();
    }

    public Integer getPerformanceIndex() {
        return forzaApi.getPerformanceIndex();
    }

    public String getDrivetrain() {
        return forzaApi.getDrivetrain();
    }

    public Integer getNumOfCylinders() {
        return forzaApi.getNumOfCylinders();
    }

    public String getCarType() {
        return forzaApi.getCarType();
    }

    public Integer getOrdinal() {
        return forzaApi.getOrdinal();
    }
}
