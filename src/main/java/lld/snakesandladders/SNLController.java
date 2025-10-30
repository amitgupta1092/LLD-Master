package lld.snakesandladders;

import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@NoArgsConstructor
public class SNLController {

    private static final int MAX_PLAYERS_ALLOWED = 4;
    private static final int LADDER_COUNT = 1;
    private static final int SNAKES_COUNT = 1;

    public void runSnakesAndLaddersGame() throws InterruptedException {

        SnakesAndLadderGame snakesAndLadderGame = initializeGame();

        List<String> playersSequence = snakesAndLadderGame.getPlayers();
        PlayersCoordinator playersCoordinator = new PlayersCoordinator(playersSequence);

        int numberOfPlayers = snakesAndLadderGame.getNumberOfPlayers();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPlayers);
        SynchronousQueue<PlayerRoll> playerRollQueue = new SynchronousQueue<>();

        List<Runnable> playerThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            Runnable playerThread = new SNLPlayerThread(playersSequence.get(i-1), playerRollQueue, playersCoordinator);
            playerThreads.add(playerThread);
        }

        playerThreads
                .forEach(executorService::submit);


        while (playersCoordinator.isGameActive()) {

            PlayerRoll playerRoll = playerRollQueue.take();
            snakesAndLadderGame = snakesAndLadderGame
                    .movePlayer(playerRoll);

            if (!snakesAndLadderGame.isActive()) {
                playersCoordinator.markGameAsInactive();
            }
        }

        executorService.shutdown();
        boolean gameCompleted = executorService.awaitTermination(5, TimeUnit.SECONDS);

        if (gameCompleted) {
            System.out.println("game completed, winner is : " + snakesAndLadderGame.getWinner());
        }

    }

    private SnakesAndLadderGame initializeGame() {

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

    private Map<Integer, Snake> initSnakes() {

        Scanner sc = new Scanner(System.in);
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

    private Optional<Snake> initSnake(int i) {
        int start = takeIntInputWithRetries("snake " + i + " enter starting position ", 3);
        int end = takeIntInputWithRetries("snake " + i + " enter end position ", 3);

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

    private Map<Integer, Ladder> initLadders() {

        Scanner sc = new Scanner(System.in);
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

    private Optional<Ladder> initLadder(int i) {
        int start = takeIntInputWithRetries("ladder " + i + " enter starting position ", 3);
        int end = takeIntInputWithRetries("ladder " + i + " enter end position ", 3);

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

    private int takeIntInputWithRetries(String message, int maxRetries) {

        for (int i = 0; i < maxRetries; i++) {
            Scanner sc = new Scanner(System.in);
            System.out.println(message);

            String inputStr = sc.next();
            try {
                return Integer.parseInt(inputStr);
            } catch (Exception ex) {
                System.out.println("invalid input, please enter again");
            }
        }
        throw new RuntimeException("max retries attempted");
    }

    private int getNumberOfPlayers() {
        Scanner sc = new Scanner(System.in);

        for (int i = 1; i <= 3; i++) {
            System.out.println("Enter the number of players, max allowed players : " + MAX_PLAYERS_ALLOWED);
            String inp = sc.nextLine();

            try {
                int numPlayersInt = Integer.parseInt(inp);

                if (numPlayersInt > MAX_PLAYERS_ALLOWED)
                    throw new RuntimeException("Max " + MAX_PLAYERS_ALLOWED + " players allowed");


                return numPlayersInt;
            } catch (Exception ex) {
                System.out.println("Invalid Input !");
            }
        }
        throw new RuntimeException("max attempts done ");
    }

    private List<String> getPlayers(int numberOfPlayers) {

        List<String> players = new ArrayList<>();
        Set<String> playersSet = new HashSet<>();
        Scanner sc = new Scanner(System.in);

        for (int j = 1; j <= numberOfPlayers; j++) {

            System.out.println("enter player " + j + " name");
            String playerName = sc.nextLine();

            if (playersSet.contains(playerName)) {
                System.out.println(playerName + " already exists, enter a different name");
                playerName = sc.nextLine();
                if (players.contains(playerName))
                    throw new RuntimeException("max attempts done");
            }
            players.add(playerName);
            playersSet.add(playerName);
        }

        return players;
    }

}
