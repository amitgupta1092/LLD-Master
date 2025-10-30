package lld.snakesandladders;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Snake {
    private int start;
    private int end;

    public Snake(int start, int end) {

        validate(start, end);
        this.start = start;
        this.end = end;
    }

    private void validate(int start, int end) {

        if (start == 100) {
            throw new RuntimeException("ladder cant start at 100");
        }

        if (start < 0) {
            throw new RuntimeException("invalid start position : " + start);
        }

        if (end > 100) {
            throw new RuntimeException("invalid end position : " + end);
        }

        if (start < end) {
            throw new RuntimeException("snake start cannot be less than snake end : ");
        }
        if (start == end) {
            throw new RuntimeException("snake cannot start and end at same position");
        }
    }
}
