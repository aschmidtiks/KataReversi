package gui;
import logic.Game;
import logic.Slot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Panel extends JPanel {

    private final int WIDTH;
    private final int HEIGHT;
    private final int ROWS;
    private final int COLUMNS;
    private final int SLOT_WIDTH;
    private final int SLOT_HEIGHT;
    private final int START_INFO_RECT_WIDTH;
    private final int START_INFO_RECT_HEIGHT;
    private final int START_INFO_RECT_X_POSITION;
    private final int START_INFO_RECT_Y_POSITION;

    private JButton startButton = new JButton();

    private JLabel infoText = new JLabel();
    private JLabel infoTextPlayer1 = new JLabel();
    private JLabel infoTextPlayer2 = new JLabel();
    private JLabel winnerText1 = new JLabel();
    private JLabel winnerText2 = new JLabel();

    private Game game;

    public Panel(int rows, int columns) {
        this.setFocusable(true);
        this.setLayout(null);
        this.ROWS = rows;
        this.COLUMNS = columns;
        this.WIDTH = columns * 50;
        this.HEIGHT = rows * 50;
        this.SLOT_WIDTH = WIDTH / COLUMNS;
        this.SLOT_HEIGHT = HEIGHT / ROWS;
        this.setPreferredSize(new Dimension(this.WIDTH, this.HEIGHT));

        this.START_INFO_RECT_WIDTH = WIDTH/2;
        this.START_INFO_RECT_HEIGHT = HEIGHT/2;
        this.START_INFO_RECT_X_POSITION = (WIDTH/2)-(START_INFO_RECT_WIDTH/2);
        this.START_INFO_RECT_Y_POSITION = (HEIGHT/2)-(START_INFO_RECT_HEIGHT/2);

        createButtons();
        createLabels();
        createListener();
    }

    private void createLabels() {
        this.infoText.setText("Free slots are empty and legal slots are green");
        this.infoText.setBounds(START_INFO_RECT_X_POSITION + 50,START_INFO_RECT_Y_POSITION,
                START_INFO_RECT_WIDTH - 50,START_INFO_RECT_HEIGHT/5);

        this.infoTextPlayer1.setText("Player 1 color: Blue");
        this.infoTextPlayer1.setBounds(START_INFO_RECT_X_POSITION + 50,infoText.getY() + infoText.getHeight(),
                START_INFO_RECT_WIDTH - 50,START_INFO_RECT_HEIGHT/5);

        this.infoTextPlayer2.setText("Player 2 color: Red");
        this.infoTextPlayer2.setBounds(START_INFO_RECT_X_POSITION + 50,infoTextPlayer1.getY() + infoTextPlayer1.getHeight(),
                START_INFO_RECT_WIDTH - 50,START_INFO_RECT_HEIGHT/5);

        this.winnerText1.setText("Player 1 won!");
        this.winnerText1.setBounds(START_INFO_RECT_X_POSITION + 50,infoTextPlayer1.getY() + infoTextPlayer1.getHeight(),
                START_INFO_RECT_WIDTH - 50,START_INFO_RECT_HEIGHT/5);

        this.winnerText2.setText("Player 2 won!");
        this.winnerText2.setBounds(START_INFO_RECT_X_POSITION + 50,infoTextPlayer1.getY() + infoTextPlayer1.getHeight(),
                START_INFO_RECT_WIDTH - 50,START_INFO_RECT_HEIGHT/5);

        winnerText1.setVisible(false);
        winnerText2.setVisible(false);

        this.add(winnerText1);
        this.add(winnerText2);
        this.add(infoText);
        this.add(infoTextPlayer1);
        this.add(infoTextPlayer2);
    }
    private void createButtons() {
        this.startButton.setText("Start");
        this.startButton.setSize(100, 50);
        this.startButton.setLocation((START_INFO_RECT_X_POSITION+(START_INFO_RECT_WIDTH/2)-(startButton.getWidth()/2)),
                (START_INFO_RECT_Y_POSITION*2)+(START_INFO_RECT_HEIGHT/3));
        this.startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                infoText.setVisible(false);
                infoTextPlayer1.setVisible(false);
                infoTextPlayer2.setVisible(false);
                startButton.setVisible(false);
                repaint();
                start();
            }
        });
        this.add(startButton);
    }
    private void createListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (game != null) {
                    Point mousePos = new Point(e.getX() / SLOT_WIDTH, e.getY() / SLOT_HEIGHT);
                    if (game.getBoard().getSlots()[mousePos.y][mousePos.x] == Slot.LEGAL_POSITION_P1) {
                        game.getBoard().changeLegalSlotToPlayerSlot(Slot.PLAYER1, mousePos.y, mousePos.x, game.getTurn().getCurrentPlayer());
                        repaint();
                        game.nextRound();
                       if (!game.checkBoard()) {
                         repaint();
                        }

                    } else if (game.getBoard().getSlots()[mousePos.y][mousePos.x] == Slot.LEGAL_POSITION_P2) {
                        game.getBoard().changeLegalSlotToPlayerSlot(Slot.PLAYER2, mousePos.y, mousePos.x, game.getTurn().getCurrentPlayer());
                        repaint();
                        game.nextRound();
                        if (game.checkBoard()) {
                           repaint();
                        }
                    }
                }
            }
        });
    }

    private void start() {
        game = new Game();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(game != null) {
            if (!game.isGameHasEnd()) {
                drawBoard(g);
            } else {
                drawEndScreen(g);
            }
        } else {
            drawStartInfo(g);
        }
    }

    private void drawEndScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(START_INFO_RECT_X_POSITION, START_INFO_RECT_Y_POSITION, START_INFO_RECT_WIDTH, START_INFO_RECT_HEIGHT);
        if (game.pointsPlayer[0] > game.pointsPlayer[1]) {
            this.winnerText1.setVisible(true);
        } else {
            this.winnerText2.setVisible(true);
        }
    }

    private void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                switch(game.getBoard().getSlots()[i][j])
                {
                    case EMPTY: {
                        g.setColor(Color.BLACK);
                        g.drawRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        break;
                    }
                    case PLAYER1: {
                        g.setColor(Color.BLUE);
                        g.fillRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        break;
                    }
                    case PLAYER2: {
                        g.setColor(Color.RED);
                        g.fillRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        break;
                    }
                    case LEGAL_POSITION_P1: {
                        g.setColor(Color.GREEN);
                        g.fillRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        break;
                    }
                    case LEGAL_POSITION_P2: {
                        g.setColor(Color.GREEN);
                        g.fillRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        g.setColor(Color.BLACK);
                        g.drawRect(j * SLOT_WIDTH, i * SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
                        break;
                    }
                }
            }
        }
    }

    private void drawStartInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(START_INFO_RECT_X_POSITION, START_INFO_RECT_Y_POSITION, START_INFO_RECT_WIDTH, START_INFO_RECT_HEIGHT);
    }
}