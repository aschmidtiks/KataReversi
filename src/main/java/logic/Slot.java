package logic;

import java.awt.*;

public enum Slot {

    EMPTY(0),
    PLAYER1(1),
    PLAYER2(2),
    LEGAL_POSITION_P1(4),
    LEGAL_POSITION_P2(5);

    public final int slotValue;

    Slot(int value) {
        this.slotValue = value;
    }

    private Point[] neighboursPosition;
    public Point[] getNeighboursPosition() {
        return neighboursPosition;
    }
}
