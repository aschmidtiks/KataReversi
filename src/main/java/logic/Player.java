package logic;

public enum Player {
    PLAYER1(1),
    PLAYER2(2);

    public final int playerValue;

    Player(int value) {
        this.playerValue = value;
    }
}
