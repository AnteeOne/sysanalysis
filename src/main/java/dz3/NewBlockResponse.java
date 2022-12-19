package dz3;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class NewBlockResponse {
    private int status;
    private String statusString;
    private BlockModel block;


    public NewBlockResponse(int status, String statusString, BlockModel block) {
        this.status = status;
        this.statusString = statusString;
        this.block = block;
    }

    public NewBlockResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public BlockModel getBlock() {
        return block;
    }

    public void setBlock(BlockModel block) {
        this.block = block;
    }
}
