package lld.snakesandladders;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor
public class PlayersCoordinator {

    private final List<String> playersSequence;
    private int currentPlayerIndex = 0;
    private final Map<Integer, String> playersSequenceMap;
    private volatile boolean isGameActive;


    public PlayersCoordinator(final List<String> playersSequence) {
        this.playersSequence = playersSequence;
        this.isGameActive = true;
        playersSequenceMap = IntStream
                .range(0, playersSequence.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i,
                        playersSequence::get
                ));
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public synchronized void markGameAsInactive() {
        isGameActive = false;
        currentPlayerIndex = -1;
        this.notifyAll();
    }

    public synchronized void waitForTurn(String username) throws InterruptedException {
        if (!isGameActive) return;
        if (currentPlayerIndex >= 0
                && playersSequence
                .get(currentPlayerIndex)
                .equals(username)) return;
        this.wait();
    }

    public synchronized void completeTurn() throws InterruptedException {

        currentPlayerIndex++;

        int maxIndex = playersSequence.size();
        if (currentPlayerIndex >= maxIndex) {
            currentPlayerIndex %= maxIndex;
        }
        this.notifyAll();
    }
}
