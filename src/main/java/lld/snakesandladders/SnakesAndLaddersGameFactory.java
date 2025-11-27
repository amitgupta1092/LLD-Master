package lld.snakesandladders;

import java.util.*;
import java.util.stream.Collectors;

public class SnakesAndLaddersGameFactory {

    private static final int MAX_PLAYERS_ALLOWED = 4;
    private static final int LADDER_COUNT = 1;
    private static final int SNAKES_COUNT = 1;

    public static SnakesAndLadderGame createGameWithUserInputs() {
        return initializeGameWithUserInputs();
    }

    private static SnakesAndLadderGame initializeGameWithUserInputs() {

        SnakesAndLadderGame
                .SnakesAndLadderGameBuilder gameBuilder = SnakesAndLadderGame
                .builder();

        int numberOfPlayers = getNumberOfPlayers();
        gameBuilder.numberOfPlayers(numberOfPlayers);

        List<String> players = getPlayers(numberOfPlayers);
        gameBuilder.players(players);

        Map<Integer, Ladder> ladders = initLadders();
        gameBuilder.ladders(ladders);

        Map<Integer, Snake> snakes = initSnakes();
        gameBuilder.snakes(snakes);

        gameBuilder
                .playerPositionMap(new HashMap<>())
                .isActive(true);

        return gameBuilder
                .build();

    }

    private static Map<Integer, Snake> initSnakes() {

        List<Snake> snakes = new ArrayList<>();

        for (int i = 0; i < SNAKES_COUNT; i++) {

            Optional<Snake> snakeOptional = Optional.empty();
            for (int j = 1; j <= 2; j++) {
                snakeOptional = initSnake(i);
                if (snakeOptional.isPresent()) {
                    snakes.add(snakeOptional.get());
                    break;
                }
            }

            if (snakeOptional.isEmpty())
                throw new RuntimeException("max attempts done");
        }

        return
                snakes
                        .stream()
                        .collect(Collectors.toMap(
                                Snake::getStart,
                                snake -> snake
                        ));
    }

    private static Optional<Snake> initSnake(int i) {
        int start = UserInputService.takeIntInputWithRetries("snake " + i + " enter starting position ", 3);
        int end = UserInputService.takeIntInputWithRetries("snake " + i + " enter end position ", 3);

        try {
            Snake snake = Snake
                    .builder()
                    .start(start)
                    .end(end)
                    .build();
            return Optional.ofNullable(snake);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return Optional.empty();
    }

    private static Map<Integer, Ladder> initLadders() {

        List<Ladder> ladders = new ArrayList<>();

        for (int i = 0; i < LADDER_COUNT; i++) {

            Optional<Ladder> ladderOptional = Optional.empty();
            for (int j = 1; j <= 2; j++) {
                ladderOptional = initLadder(i);
                if (ladderOptional.isPresent()) {
                    ladders.add(ladderOptional.get());
                    break;
                }
            }

            if (ladderOptional.isEmpty())
                throw new RuntimeException("max attempts done");
        }

        return
                ladders
                        .stream()
                        .collect(Collectors.toMap(
                                Ladder::getStart,
                                ladder -> ladder
                        ));
    }

    private static Optional<Ladder> initLadder(int i) {
        int start = UserInputService.takeIntInputWithRetries("ladder " + i + " enter starting position ", 3);
        int end = UserInputService.takeIntInputWithRetries("ladder " + i + " enter end position ", 3);

        try {
            Ladder ladder = Ladder
                    .builder()
                    .start(start)
                    .end(end)
                    .build();
            return Optional.ofNullable(ladder);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return Optional.empty();
    }

    private static int getNumberOfPlayers() {

        String message = "Enter the number of players, max allowed players : " + MAX_PLAYERS_ALLOWED;
        int numPlayersInput = UserInputService.takeIntInputWithRetries(message, 3);

        if (numPlayersInput > MAX_PLAYERS_ALLOWED)
            throw new RuntimeException("Max " + MAX_PLAYERS_ALLOWED + " players allowed");

        return numPlayersInput;
    }

    private static List<String> getPlayers(int numberOfPlayers) {

        List<String> players = new ArrayList<>();
        Set<String> playersSet = new HashSet<>();

        for (int j = 1; j <= numberOfPlayers; j++) {

            String message = "enter player " + j + " name";

            String playerName = UserInputService.takeStringInput(message);

            if (playersSet.contains(playerName)) {
                String errorMessage = playerName + " already exists, enter a different name";
                playerName = UserInputService.takeStringInput(errorMessage);
                if (players.contains(playerName))
                    throw new RuntimeException("max attempts done");
            }
            players.add(playerName);
            playersSet.add(playerName);
        }

        return players;
    }

}
