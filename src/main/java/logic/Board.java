package logic;

import gui.Panel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    private Slot[][] slots = new Slot[8][8];
    private static final int MAX_BOARD_SIZE = 8;
    private List<List<Point>> legalMovesList = new ArrayList(MAX_BOARD_SIZE);

    private boolean boardFull = false;

    private Slot[][] defaultBoard = {
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.PLAYER1, Slot.PLAYER2, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.PLAYER2, Slot.PLAYER1, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
            {Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY, Slot.EMPTY},
    };

    public Board() {
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[0].length; j++) {
                this.slots[i][j] = defaultBoard[i][j];
            }
        }
    }
    public Board(Slot[][] slots) {
        for (int i = 0; i < slots.length; i++) {
            for (int j = 0; j < slots[0].length; j++) {
                this.slots[i][j] = slots[i][j];
            }
        }
    }

    public boolean calculateLegalMoves(Player currentPlayer) {
        getLegalMovesList().clear();
        checkSlotForCurrentPlayer(currentPlayer);
        if (legalMovesList.size() == 0) {
           boardFull = true;
           return false;
        }
        setLegalMovesSlots(currentPlayer);
        return true;
    }

    private void checkSlotForCurrentPlayer(Player currentPlayer) {
        for (int i = 0; i < MAX_BOARD_SIZE; i++) {
            for (int j = 0; j < MAX_BOARD_SIZE; j++) {
                if (slots[i][j].toString() == currentPlayer.toString()) {
                    Point playerPosition = new Point(j, i);
                    checkForSurroundingEnemies(playerPosition, currentPlayer);
                }
            }
        }
    }

    private void checkForSurroundingEnemies(Point playerPosition, Player currentPlayer) {
        Point direction;
        Point slotToCheck;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                direction = new Point(j, i);
                slotToCheck = new Point(playerPosition.x + direction.x, playerPosition.y + direction.y);
                if (slotToCheck.x < MAX_BOARD_SIZE &&
                        slotToCheck.y < MAX_BOARD_SIZE &&
                        slotToCheck.x >= 0 &&
                        slotToCheck.y >= 0) {
                    if (!(i == 0 && j == 0) &&
                            ((slots[slotToCheck.y][slotToCheck.x].toString() != Slot.EMPTY.toString()) &&
                                    slots[slotToCheck.y][slotToCheck.x].toString() != currentPlayer.toString())) {
                        calculateLegalMovesForThisSlot(direction, playerPosition, slotToCheck, currentPlayer);
                    }
                }
            }
        }
    }

    private void calculateLegalMovesForThisSlot(Point direction, Point playerPosition, Point enemyPosition, Player currentPlayer) {
        Point cumulativeDistanceToTarget = new Point(direction.getLocation());
        Point slotToCheck = new Point(enemyPosition.x + cumulativeDistanceToTarget.x, enemyPosition.y + cumulativeDistanceToTarget.y);

        while (true) {
            if ((slotToCheck.y >= 0 && slotToCheck.x >= 0 && slotToCheck.y < MAX_BOARD_SIZE && slotToCheck.x < MAX_BOARD_SIZE)) {
                if ((slots[slotToCheck.y][slotToCheck.x].toString() == Slot.EMPTY.toString())) {
                    addSlotToMoveList(direction, cumulativeDistanceToTarget, playerPosition, enemyPosition);
                    break;
                }
                else if ((slots[slotToCheck.y][slotToCheck.x].toString() != currentPlayer.toString())) {
                    cumulativeDistanceToTarget.y += direction.y;
                    cumulativeDistanceToTarget.x += direction.x;
                    slotToCheck.y += direction.getY();
                    slotToCheck.x += direction.getX();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void addSlotToMoveList(Point direction, Point cumulativeDistanceToTarget, Point playerPosition, Point enemyPosition) {
        Point pointToSet = new Point(enemyPosition.x + cumulativeDistanceToTarget.x, enemyPosition.y + cumulativeDistanceToTarget.y);
        getLegalMovesList().add(setSubList(pointToSet, direction, playerPosition));
    }

    // 0 = setted legal movepoint 1 = direction 2 = player position
    private List<Point> setSubList(Point pointToSet, Point direction, Point playerPosition) {
        List<Point> subList = new ArrayList(3);
        subList.add(new Point(pointToSet));
        subList.add(new Point(direction));
        subList.add(new Point(playerPosition));
        return subList;
    }

    private void setLegalMovesSlots(Player currentPlayer) {
        for (int i = 0; i < legalMovesList.size(); i++) {
            getSlots()[legalMovesList.get(i).get(0).y][legalMovesList.get(i).get(0).x] =
                    currentPlayer.toString() == Player.PLAYER1.toString() ? Slot.LEGAL_POSITION_P1 : Slot.LEGAL_POSITION_P2;
        }
    }

    public void changeEnemySlotsToCurrentPlayer(Point chosenLegalMovePoint, Player currentPlayer) {

        List<List<Point>> listWithTargetedLegalMoves = new ArrayList(legalMovesList.size());
        listWithTargetedLegalMoves.addAll(getTargetedMovePoints(chosenLegalMovePoint));

        clearBoardFromEnemyLegalMoves();
        if (legalMovesList.size() > 1) {
        deleteUsedPointsFromParentList(listWithTargetedLegalMoves);
        }

        for (int i = 0; i < listWithTargetedLegalMoves.size(); i++) {
            Point nextStep = new Point(listWithTargetedLegalMoves.get(i).get(2).x + listWithTargetedLegalMoves.get(i).get(1).x,
                    listWithTargetedLegalMoves.get(i).get(2).y + listWithTargetedLegalMoves.get(i).get(1).y);

            while (true) {
                if (!nextStep.equals(listWithTargetedLegalMoves.get(i).get(0))) {
                    getSlots()[nextStep.y][nextStep.x] = currentPlayer.toString() == "PLAYER1" ? Slot.PLAYER1 : Slot.PLAYER2;
                } else {
                    break;
                }
                nextStep.x += listWithTargetedLegalMoves.get(i).get(1).x;
                nextStep.y += listWithTargetedLegalMoves.get(i).get(1).y;
            }
        }
    }

    public int[] countPoints() {
        int[] tempPlayer = new int[2];
        for (int i = 0; i < MAX_BOARD_SIZE; i++) {
            for (int j = 0; j < MAX_BOARD_SIZE; j++) {
                if (slots[i][j] == Slot.PLAYER1) {
                    tempPlayer[0]++;
                } else  if (slots[i][j] == Slot.PLAYER2) {
                    tempPlayer[1]++;
                }
            }
        }
        return tempPlayer;
    }

    private List<List<Point>> getTargetedMovePoints(Point chosenLegalMovePoint) {
        List<List<Point>> tempListWithTargetedLegalMoves = new ArrayList(legalMovesList.size());

        for (int i = 0; i < legalMovesList.size(); i++) {
            Point pointToCheck = new Point(legalMovesList.get(i).get(0)); // 0 = target legal move
            if (pointToCheck.equals(chosenLegalMovePoint)) {
                tempListWithTargetedLegalMoves.add(setSubList(legalMovesList.get(i).get(0), legalMovesList.get(i).get(1), legalMovesList.get(i).get(2)));
            }
        }
        return tempListWithTargetedLegalMoves;
    }

    private void deleteUsedPointsFromParentList(List<List<Point>> listWithTargetedLegalMoves) {
        for (int i = 0; i < listWithTargetedLegalMoves.size(); i++) {
            if (legalMovesList.size() > 1) {
                if (legalMovesList.contains(listWithTargetedLegalMoves.get(i))) {
                    legalMovesList.remove(i);
                }
            }
        }
    }

    public void changeLegalSlotToPlayerSlot(Slot slotValue, int y, int x, Player currentPlayer) {
        setSlot(slotValue, y, x);
        Point legalMovePoint = new Point(x, y);
        changeEnemySlotsToCurrentPlayer(legalMovePoint, currentPlayer);
    }

    private void clearBoardFromEnemyLegalMoves() {
        for (int i = 0; i < legalMovesList.size(); i++) {
            if (getSlots()[getLegalMovesList().get(i).get(0).y][getLegalMovesList().get(i).get(0).x] == Slot.LEGAL_POSITION_P1 ||
                    getSlots()[getLegalMovesList().get(i).get(0).y][getLegalMovesList().get(i).get(0).x] == Slot.LEGAL_POSITION_P2) {
                getSlots()[getLegalMovesList().get(i).get(0).y][getLegalMovesList().get(i).get(0).x] = Slot.EMPTY;
            }
        }
    }

    public Slot[][] getSlots() {
        return slots;
    }
    public void setSlot(Slot slotValue, int y, int x) {
        slots[y][x] = slotValue;
    }
    public List<List<Point>> getLegalMovesList() {
        return legalMovesList;
    }
    public boolean isBoardFull() {
        return boardFull;
    }
}
