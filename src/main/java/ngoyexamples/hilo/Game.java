package ngoyexamples.hilo;

import java.util.Random;
import java.util.stream.IntStream;

public class Game {

	public static enum State {
		GUESSING, TOO_LOW, TOO_HIGH, FOUND
	}

	public State state;

	private final Random random = new Random(System.nanoTime());

	public final int[] numbers = IntStream.rangeClosed(1, 10)
			.toArray();

	public int secretNumber;
	public int guesses;

	public Game() {
		init();
	}

	public void guess(int number) {
		if (number == secretNumber) {
			state = State.FOUND;
		} else if (number < secretNumber) {
			state = State.TOO_LOW;
		} else {
			state = State.TOO_HIGH;
		}
		guesses++;
	}

	public void init() {
		guesses = 0;
		state = State.GUESSING;
		secretNumber = random.nextInt(10) + 1;
	}

	public boolean tooLow() {
		return state == State.TOO_LOW;
	}

	public boolean tooHigh() {
		return state == State.TOO_HIGH;
	}

	public boolean found() {
		return state == State.FOUND;
	}
}
