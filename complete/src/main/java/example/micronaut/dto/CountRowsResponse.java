package example.micronaut.dto;

public class CountRowsResponse {
    private long count;

    public CountRowsResponse(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
