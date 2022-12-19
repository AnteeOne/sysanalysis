package dz3;

public class DataModel {
    private String w11="0";
    private String w12="0";
    private String w21="0";
    private String w22="0";
    private String v11="0";
    private String v12="0";
    private String v13="0";
    private String v21="0";
    private String v22="0";
    private String v23="0";
    private String w1="0";
    private String w2="0";
    private String w3="0";
    private String e="0";
    private String publickey;

    // return as normalized JSON object
    public String toString() {
        return new StringBuilder().append("{")
                .append("\"w11\":\"").append(w11).append("\",")
                .append("\"w12\":\"").append(w12).append("\",")
                .append("\"w21\":\"").append(w21).append("\",")
                .append("\"w22\":\"").append(w22).append("\",")
                .append("\"v11\":\"").append(v11).append("\",")
                .append("\"v12\":\"").append(v12).append("\",")
                .append("\"v13\":\"").append(v13).append("\",")
                .append("\"v21\":\"").append(v21).append("\",")
                .append("\"v22\":\"").append(v22).append("\",")
                .append("\"v23\":\"").append(v23).append("\",")
                .append("\"w1\":\"").append(w1).append("\",")
                .append("\"w2\":\"").append(w2).append("\",")
                .append("\"w3\":\"").append(w3).append("\",")
                .append("\"e\":\"").append(e).append("\",")
                .append("\"publickey\":\"").append(publickey).append("\"}")
                .toString();
    }

    public DataModel() {
    }

    public DataModel(String w11, String w12, String w21, String w22, String v11, String v12, String v13, String v21, String v22, String v23, String w1, String w2, String w3, String e, String publickey) {
        this.w11 = w11;
        this.w12 = w12;
        this.w21 = w21;
        this.w22 = w22;
        this.v11 = v11;
        this.v12 = v12;
        this.v13 = v13;
        this.v21 = v21;
        this.v22 = v22;
        this.v23 = v23;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
        this.e = e;
        this.publickey = publickey;
    }

    public String getW11() {
        return w11;
    }

    public void setW11(String w11) {
        this.w11 = w11;
    }

    public String getW12() {
        return w12;
    }

    public void setW12(String w12) {
        this.w12 = w12;
    }

    public String getW21() {
        return w21;
    }

    public void setW21(String w21) {
        this.w21 = w21;
    }

    public String getW22() {
        return w22;
    }

    public void setW22(String w22) {
        this.w22 = w22;
    }

    public String getV11() {
        return v11;
    }

    public void setV11(String v11) {
        this.v11 = v11;
    }

    public String getV12() {
        return v12;
    }

    public void setV12(String v12) {
        this.v12 = v12;
    }

    public String getV13() {
        return v13;
    }

    public void setV13(String v13) {
        this.v13 = v13;
    }

    public String getV21() {
        return v21;
    }

    public void setV21(String v21) {
        this.v21 = v21;
    }

    public String getV22() {
        return v22;
    }

    public void setV22(String v22) {
        this.v22 = v22;
    }

    public String getV23() {
        return v23;
    }

    public void setV23(String v23) {
        this.v23 = v23;
    }

    public String getW1() {
        return w1;
    }

    public void setW1(String w1) {
        this.w1 = w1;
    }

    public String getW2() {
        return w2;
    }

    public void setW2(String w2) {
        this.w2 = w2;
    }

    public String getW3() {
        return w3;
    }

    public void setW3(String w3) {
        this.w3 = w3;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }
}
