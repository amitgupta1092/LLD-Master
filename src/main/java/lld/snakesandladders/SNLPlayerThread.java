package lld.snakesandladders;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;

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

        while (playersCoordinator.isGameActive()) {
            try {
                playersCoordinator.waitForTurn(username);

                int diceValue = ThreadLocalRandom
                        .current()
                        .nextInt(1, 7);

                PlayerRoll playerRoll = PlayerRoll
                        .builder()
                        .username(username)
                        .diceValue(diceValue)
                        .build();

                playerRollQueue.put(playerRoll);
                playersCoordinator.completeTurn();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("player thread " + username + " completed");

    }
}
