package lld.snakesandladders;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SNLPlayerThread implements Runnable {

    private final String username;
    final SynchronousQueue<PlayerRoll> playerRollQueue;
    private final PlayersCoordinator playersCoordinator;

    public SNLPlayerThread(final String username,
                           final SynchronousQueue<PlayerRoll> playerRollQueue,
                           final PlayersCoordinator playersCoordinator) {
        this.username = username;
        this.playerRollQueue = playerRollQueue;
        this.playersCoordinator = playersCoordinator;
    }

    @Override
    public void run() {

        while (playersCoordinator
                .isGameActive()
                && !Thread
                .currentThread()
                .isInterrupted()) {
            try {
                playersCoordinator.waitForTurn(username);

                if (!playersCoordinator.isGameActive())
                    break;

                int diceValue = ThreadLocalRandom
                        .current()
                        .nextInt(1, 7);

                PlayerRoll playerRoll = PlayerRoll
                        .builder()
                        .username(username)
                        .diceValue(diceValue)
                        .build();

                playerRollQueue.offer(playerRoll, 5, TimeUnit.SECONDS);
                playersCoordinator.completeTurn();
            } catch (InterruptedException e) {
                Thread.currentThread()
                        .interrupt();
            }
        }
        System.out.println("player thread " + username + " completed");

    }
}
