package mtopsdk.mtop.domain;

public enum EntranceEnum {
    GW_INNER("gw"),
    GW_OPEN("gw-open");
    
    private String entrance;

    public String getEntrance() {
        return this.entrance;
    }

    private EntranceEnum(String str) {
        this.entrance = str;
    }
}
