package lld.snakesandladders;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Ladder {
    private int start;
    private int end;

    public Ladder(int start, int end) {

        validate(start, end);
        this.start = start;
        this.end = end;
    }

    private void validate(int start, int end) {

        if (start == 0) {
            throw new RuntimeException("ladder cant start at 0");
        }

        if (start < 0) {
            throw new RuntimeException("invalid start position : " + start);
        }
        if (end > 100) {
            throw new RuntimeException("invalid end position : " + end);
        }

        if (start > end) {
            throw new RuntimeException("ladder start cannot be after ladder end : ");
        }

        if (start == end) {
            throw new RuntimeException("ladder cannot start and end at same position");
        }
    }
}
