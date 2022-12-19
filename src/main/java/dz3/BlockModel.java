package dz3;

import lombok.Data;

public class BlockModel {
    private String prevhash;
    private DataModel data;
    private String signature;
    private String info;
    private String ts;
    private String arbitersignature;

    // return as normalized JSON object
    public String toString() {
        return new StringBuilder().append("{")
                .append("\"prevhash\":\"").append(prevhash).append("\",")
                .append("\"data\":").append(data.toString()).append(",")
                .append("\"signature\":\"").append(signature).append("\",")
                .append("\"info\":\"").append(info).append("\",")
                .append("\"ts\":\"").append(ts).append("\",")
                .append("\"arbitersignature\":\"").append(arbitersignature).append("\",")
                .append("\"info\":\"").append(info).append("\"}")
                .toString();
    }

    public BlockModel(String prevhash, DataModel data, String signature, String info, String ts, String arbitersignature) {
        this.prevhash = prevhash;
        this.data = data;
        this.signature = signature;
        this.info = info;
        this.ts = ts;
        this.arbitersignature = arbitersignature;
    }

    public BlockModel() {
    }

    public String getPrevhash() {
        return prevhash;
    }

    public void setPrevhash(String prevhash) {
        this.prevhash = prevhash;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getArbitersignature() {
        return arbitersignature;
    }

    public void setArbitersignature(String arbitersignature) {
        this.arbitersignature = arbitersignature;
    }
}
