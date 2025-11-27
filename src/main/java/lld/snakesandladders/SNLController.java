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

    public void runSnakesAndLaddersGame() throws InterruptedException {

        SnakesAndLadderGame snakesAndLadderGame = SnakesAndLaddersGameFactory.createGameWithUserInputs();

        List<String> playersSequence = snakesAndLadderGame.getPlayers();
        PlayersCoordinator playersCoordinator = new PlayersCoordinator(playersSequence);

        int numberOfPlayers = snakesAndLadderGame.getNumberOfPlayers();
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPlayers);
        SynchronousQueue<PlayerRoll> playerRollQueue = new SynchronousQueue<>();

        List<Runnable> playerThreads = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            Runnable playerThread = new SNLPlayerThread(playersSequence.get(i - 1), playerRollQueue, playersCoordinator);
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
                executorService.shutdownNow();
                break;
            }
        }

        executorService.shutdown();
        boolean gameCompleted = executorService.awaitTermination(5, TimeUnit.SECONDS);

        if (gameCompleted) {
            System.out.println("game completed, winner is : " + snakesAndLadderGame.getWinner());
        }

    }


}
