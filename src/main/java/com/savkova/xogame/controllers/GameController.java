package com.savkova.xogame.controllers;

import com.savkova.xogame.InputAgent;
import com.savkova.xogame.Main;
import com.savkova.xogame.entities.Board;
import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.entities.Player;
import com.savkova.xogame.exceptions.AlreadyOccupiedException;
import com.savkova.xogame.exceptions.NoExistPositionException;

import java.util.Random;

public class GameController
{
    private static boolean isWin;

    private final Board board;
    private final Player[] players;

    public GameController(final Board board, final Player[] players)
    {
        this.board = board;
        this.players = players;
        isWin = false;
    }

    public Board getBoard()
    {
        return board;
    }

    public Player[] getPlayers()
    {
        return players;
    }

    public boolean move()
    {
        if (isWin)
        {
            return false;
        }

        Figure huFigure = players[0].getFigure();
        Figure aiFigure = players[1].getFigure();
        Figure currentFigure = getCurrentMoveFigure(board);
        InputAgent inputAgent = Main.getInputAgent();

        Player winner;
        int position = -1;
        if ((currentFigure != null) && (currentFigure.equals(huFigure)))
        {
            try
            {
                position = inputAgent.askMovePosition();
                putFigure(board, position, huFigure);

                if (isWinner(board, position))
                {
                    isWin = true;
                    winner = players[0].getFigure().equals(huFigure) ? players[0] : players[1];
                    System.out.println("\n" + winner.getName() + " win!");
                }

            } catch (NoExistPositionException e)
            {
                System.out.println("No such position on this board.");
            } catch (AlreadyOccupiedException e)
            {
                System.out.println("This place is already occupied, enter other position:");
            }
        } else if ((currentFigure != null) && (currentFigure.equals(aiFigure)))
        {
            position = aiRandomMove(board, aiFigure);

            if (isWinner(board, position))
            {
                isWin = true;
                winner = players[0].getFigure().equals(aiFigure) ? players[0] : players[1];
                System.out.println("\n" + winner.getName() + " win!");
            }
        } else if (currentFigure == null)
        {
            isWin = false;
            System.out.println("\nNo winner.");
            return false;
        } else
        {
            return false;
        }
        return true;
    }

    private void putFigure(final Board board, final int position, final Figure figure) throws AlreadyOccupiedException
    {
        if (board.getFigure(position) != null)
            throw new AlreadyOccupiedException();

        board.setFigure(position, figure);
    }

    private int aiRandomMove(final Board board, final Figure figure)
    {
        Random r = new Random(System.currentTimeMillis());
        int position;
        while (true)
        {
            position = r.nextInt(9);
            if (board.getFigure(position) == null)
                break;
        }

        try
        {
            putFigure(board, position, figure);
            System.out.println("\n\nOpponent move: " + (position + 1));
        } catch (AlreadyOccupiedException e)
        {
        }
        return position;
    }

    private boolean isWinner(Board board, int lastMove)
    {
        Figure[] figures = board.getFigures();
        int row = lastMove - lastMove % 3;
        if ((figures[row] == figures[row + 1]) && (figures[row] == figures[row + 2]))
            return true;

        int column = lastMove % 3;
        if ((figures[column] == figures[column + 3]) && (figures[column] == figures[column + 6]))
            return true;

        if (lastMove % 2 != 0) return false;

        if (lastMove % 4 == 0)
        {
            if ((figures[0] == figures[4]) && (figures[0] == figures[8]))
                return true;
            if (lastMove != 4)
                return false;
        }

        return ((figures[2] == figures[4]) && (figures[2] == figures[6]));
    }

    private Figure getCurrentMoveFigure(Board board)
    {
        int count = 0;
        Figure[] figures = board.getFigures();
        for (int i = 0; i < figures.length; i++)
        {
            if (board.getFigure(i) != null)
                count++;
        }

        if (count == figures.length)
            return null;

        if (count % 2 == 0)
            return Figure.X;

        return Figure.O;
    }


}
