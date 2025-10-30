package lld.snakesandladders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnakesAndLadderGame {

    private int numberOfPlayers;
    private List<String> players;
    private Map<Integer, Snake> snakes;
    private Map<Integer, Ladder> ladders;
    private Map<String, Integer> playerPositionMap;
    private boolean isActive;
    private String winner;

    public SnakesAndLadderGame movePlayer(PlayerRoll playerRoll) {

        String username = playerRoll.getUsername();
        int diceValue = playerRoll.getDiceValue();

        Integer currentPosition = playerPositionMap.get(username);

        if (Objects.isNull(currentPosition)) {
            currentPosition = 0;
        }

        int newPosition = currentPosition + diceValue;

        if (newPosition > 100) {
            System.out.println("cannot move " + diceValue + " steps");
            return this;
        }

        Snake snake = snakes.get(newPosition);
        Ladder ladder = ladders.get(newPosition);

        if (Objects.nonNull(snake)) {
            System.out.println("snake at : " + newPosition);
            newPosition = snake.getEnd();
        } else if (Objects.nonNull(ladder)) {
            System.out.println("ladder at : " + newPosition);
            newPosition = ladder.getEnd();
        }

        System.out.println(username + " reached at position " + newPosition);
        playerPositionMap.put(username, newPosition);

        if (newPosition == 100) {
            isActive = false;
            winner = username;
        }
        return this;
    }
}
