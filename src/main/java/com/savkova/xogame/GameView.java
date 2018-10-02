package com.savkova.xogame;

import com.savkova.xogame.entities.*;

public class GameView
{
    private final Game game;

    public GameView(InputAgent console)
    {
        String playerName = console.askPlayerName();
        Figure playerFigure = console.askFigureType();
        final Player player = new Player(playerName, playerFigure);

        String opponentName = "AI";
        Figure opponentFigure = playerFigure.name().equalsIgnoreCase(Figure.X.name()) ? Figure.O : Figure.X;
        final Player computer = new Player(opponentName, opponentFigure);

        final Board board = new Board();
        final Player[] players = {player, computer};
        final Difficulty level = console.askDifficulty();
        game = new Game(board, players, level);
    }

    public Game getGame()
    {
        return game;
    }

    public void showPlayers()
    {
        Player player = game.getPlayers()[0];
        Player computer = game.getPlayers()[1];

        System.out.printf("%s ('%s') vs. %s ('%s')\n\n",
                player.getName(), player.getFigure(),
                computer.getName(), computer.getFigure());
    }

    public void showBoard()
    {
        Board board = game.getBoard();
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

    private void createHorizontalLine(final char separator, final int length)
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






