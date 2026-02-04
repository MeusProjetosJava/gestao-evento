package vitor.gestaoevento.integration.payment.dto;

public class MercadoPagoWebhookPayload {

    private String type;
    private Data data;

    public String getType() {
        return type;
    }

    public Data getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
