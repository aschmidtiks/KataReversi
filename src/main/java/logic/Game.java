package logic;

import gui.Panel;

import javax.swing.*;

public class Game {

    private Board board;

    private Turn turn = new Turn(Player.PLAYER1);

    public Game(Board board) {
        this.board = board;
        runGame();
    }
    public Game() {
        board = new Board();
        runGame();
    }

    public Turn getTurn() {
        return turn;
    }
    public Board getBoard() {
        return board;
    }

    private void runGame() {
        turn.setCurrentPlayer(Player.PLAYER1);
        board.calculateLegalMoves(turn.getCurrentPlayer());
    }

    public void nextRound() {
        turn.setCurrentPlayer( turn.getCurrentPlayer() == Player.PLAYER1 ? Player.PLAYER2 : Player.PLAYER1);
        board.calculateLegalMoves(turn.getCurrentPlayer());
    }
}
