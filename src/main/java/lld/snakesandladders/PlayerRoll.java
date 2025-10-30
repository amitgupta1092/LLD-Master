package lld.snakesandladders;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlayerRoll {
    private String username;
    private int diceValue;
}
