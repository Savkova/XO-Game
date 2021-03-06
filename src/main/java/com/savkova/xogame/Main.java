package com.savkova.xogame;

import com.savkova.xogame.controllers.GameController;
import com.savkova.xogame.entities.*;

import java.util.Scanner;

public class Main
{
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputAgent inputAgent = new InputAgent(scanner);

    public static void main(String[] args)
    {
        System.out.println("Welcome to XO-Game!");
        System.out.println("(enter \'quit\' for quit)\n");

        startGame(initNewGame());

        freeResources();
    }

    public static InputAgent getInputAgent()
    {
        return inputAgent;
    }

    public static void quit(String line)
    {
        if (line.equalsIgnoreCase("quit"))
        {
            freeResources();
            System.exit(0);
        }
    }

    private static void startGame(GameView gameView)
    {
        System.out.println("\n\nLet's start!\n");
        gameView.showPlayers();

        while (gameView.getGameController().move());

        continueGame();
    }

    private static GameView initNewGame()
    {
        final Board board = new Board();
        final Player[] players = initPlayers();
        final GameController gameController = new GameController(board, players);

        return new GameView(gameController);
    }

    private static Player[] initPlayers()
    {
        String humanName = inputAgent.askPlayerName();
        Figure humanFigure = inputAgent.askFigureType();
        Player human = new Player(humanName, humanFigure);

        String computerName = "Computer";
        Figure computerFigure = humanFigure.equals(Figure.X) ? Figure.O : Figure.X;
        Player computer = new Player(computerName, computerFigure);

        return new Player[]{human, computer};
    }

    private static void continueGame()
    {
        InputAgent inputAgent = Main.getInputAgent();

        boolean isStartNewGame = false;
        while (!isStartNewGame)
            isStartNewGame = inputAgent.askStartNewGame();

        startGame(initNewGame());
    }

    private static void freeResources()
    {
        if (scanner != null)
            scanner.close();
    }
}
