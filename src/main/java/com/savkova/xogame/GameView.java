package com.savkova.xogame;

import com.savkova.xogame.controllers.GameController;
import com.savkova.xogame.entities.Board;
import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.entities.Player;

public class GameView
{
    private final GameController gameController;

    public GameView(final GameController gameController)
    {
        this.gameController = gameController;
    }

    public GameController getGameController()
    {
        return gameController;
    }

    public void showPlayers()
    {
        Player human = gameController.getPlayers()[0];
        Player computer = gameController.getPlayers()[1];

        System.out.printf("%s ('%s') vs. %s ('%s')",
                human.getName(), human.getFigure(),
                computer.getName(), computer.getFigure());
    }

    public static void showBoard(Board board)
    {
        Figure[] figures = board.getFigures();

        int horizontalLength = 11;
        char horizontalSeparator = '-';
        char verticalSeparator = '|';

        for (int i = 0; i < figures.length; i++)
        {
            if (figures[i] != null)
                System.out.print(" " + figures[i] + " ");
            else
                System.out.print(" " + (i+1) + " ");

            if (((i + 1) % 3 == 0) && (i < figures.length - 1))
                createHorizontalLine(horizontalSeparator, horizontalLength);
            else if (i < figures.length - 1)
                System.out.print(verticalSeparator);
        }
    }

    private static void createHorizontalLine(final char separator, final int length)
    {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        for (int i = 0; i < length; i++)
        {
            result.append(separator);
        }
        System.out.print(result.append("\n").toString());
    }
}






