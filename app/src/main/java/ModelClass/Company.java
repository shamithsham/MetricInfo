package ModelClass;

public class Company {
    private String clientName;
    private String clientId;

    public Company(String clientId, String clientName) {
        this.clientId = clientId;
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientId() {
        return clientId;
    }
}

