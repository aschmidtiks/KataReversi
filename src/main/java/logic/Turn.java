package logic;

public class Turn {
    private Player player;

    public Turn(Player player) {
        this.player = player;
    }

    public Player getCurrentPlayer() {
        return player;
    }

    public void setCurrentPlayer(Player player) {
        this.player = player;
    }
}
