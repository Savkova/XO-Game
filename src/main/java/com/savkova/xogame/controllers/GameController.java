package com.savkova.xogame.controllers;

import com.savkova.xogame.GameView;
import com.savkova.xogame.InputAgent;
import com.savkova.xogame.Main;
import com.savkova.xogame.entities.Board;
import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.entities.Player;
import com.savkova.xogame.exceptions.AlreadyOccupiedException;
import com.savkova.xogame.exceptions.NoExistPositionException;

import java.util.ArrayList;
import java.util.List;
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
            GameView.showBoard(board);
            return false;
        }

        Figure humanFigure = players[0].getFigure();
        Figure computerFigure = players[1].getFigure();
        Figure currentFigure = findCurrentMoveFigure(board);

        if ((currentFigure != null) && (currentFigure.equals(humanFigure)))
        {
            humanMove(humanFigure);
        } else if ((currentFigure != null) && (currentFigure.equals(computerFigure)))
        {
            computerMove(computerFigure);
        } else if (currentFigure == null)             // if board full
        {
            showWinnerName(false, null);
            return false;
        }

        return true;
    }

    private void humanMove(Figure humanFigure)
    {
        System.out.println("\n");
        GameView.showBoard(board);

        InputAgent inputAgent = Main.getInputAgent();
        try
        {
            int position = inputAgent.askMovePosition();
            putFigure(board, position, humanFigure);

            if (isWinner(board, position))
            {
                isWin = true;
                showWinnerName(true, humanFigure);
            }

        } catch (NoExistPositionException e)
        {
            System.out.print("No such position on this board. Try again: ");
        } catch (AlreadyOccupiedException e)
        {
            System.out.print("This place is already occupied, enter other position: ");
        }
    }

    private void computerMove(Figure computerFigure)
    {
        int position = 0;
        try
        {
            position = findBestPosition(board);
            putFigure(board, position, computerFigure);
            System.out.println("\n\nComputer move: " + (position + 1));
        } catch (AlreadyOccupiedException e)
        {
        }

        if (isWinner(board, position))
        {
            isWin = true;
            showWinnerName(true, computerFigure);
        }
    }

    private Figure findCurrentMoveFigure(Board board)
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

    private int findBestPosition(final Board board)
    {
        Random random = new Random(System.currentTimeMillis());

        int center = 4;
        int[] corners = {0, 2, 6, 8};
        int[] sides = {1, 3, 5, 7};

        Figure[] figures = board.getFigures();
        List<Integer> emptyIndexes = findEmptyIndexes(board);

        int index;

        if (emptyIndexes.size() == 1)
            return emptyIndexes.get(0);

        if (figures[4] == null)
            return center;
        else if (emptyIndexes.size() > 7)
        {
            index = random.nextInt(4);
            return corners[index];
        } else if (emptyIndexes.size() == 7)
        {
            index = random.nextInt(4);
            return sides[index];
        } else
        {
            for (int emptyIndex : emptyIndexes)
            {
                int row = emptyIndex - emptyIndex % 3;
                if (checkRow(figures, row))
                    return emptyIndex;

                int column = emptyIndex % 3;
                if (checkColumn(figures, column))
                    return emptyIndex;

                if (emptyIndex % 4 == 0)
                {
                    int k = checkLeftDiagonal(figures);
                    if (k != -1)
                        return k;
                } else if (emptyIndex % 2 == 0)
                {
                    int d = checkRightDiagonal(figures);
                    if (d != -1)
                        return d;
                }
            }
        }

        return findRandomPosition(emptyIndexes);
    }

    private int findRandomPosition(List<Integer> emptyIndexes)
    {
        Random random = new Random(System.currentTimeMillis());
        int i;
        while (true)
        {
            i = random.nextInt(emptyIndexes.size());
            if (board.getFigure(emptyIndexes.get(i)) == null)
                break;
        }
        return emptyIndexes.get(i);
    }

    private List<Integer> findEmptyIndexes(Board board)
    {
        Figure[] figures = board.getFigures();
        ArrayList<Integer> emptyIndexes = new ArrayList<>();
        for (int i = 0; i < figures.length; i++)
        {
            if (figures[i] == null)
                emptyIndexes.add(i);
        }
        return emptyIndexes;
    }

    private void putFigure(final Board board, final int position, final Figure figure) throws AlreadyOccupiedException
    {
        if (board.getFigure(position) != null)
            throw new AlreadyOccupiedException();

        board.setFigure(position, figure);
    }

    private boolean checkRow(Figure[] figures, int row)
    {
        return (((figures[row] == figures[row + 1]) && (figures[row] != null))
                || ((figures[row] == figures[row + 2]) && (figures[row] != null))
                || ((figures[row + 1] == figures[row + 2]) && (figures[row + 1] != null)));
    }

    private boolean checkColumn(Figure[] figures, int column)
    {
        return (((figures[column] == figures[column + 3]) && (figures[column] != null))
                || ((figures[column] == figures[column + 6]) && (figures[column] != null))
                || ((figures[column + 3] == figures[column + 6]) && (figures[column + 3] != null)));
    }

    private int checkLeftDiagonal(Figure[] figures)
    {
        if ((figures[0] == figures[4]) && (figures[0] != null) && (figures[8] == null))
            return 8;

        if ((figures[0] == figures[8]) && (figures[0] != null) && (figures[4] == null))
            return 4;

        if ((figures[4] == figures[8]) && (figures[4] != null) && (figures[0] == null))
            return 0;

        return -1;
    }

    private int checkRightDiagonal(Figure[] figures)
    {
        if ((figures[2] == figures[4]) && (figures[2] != null) && (figures[6] == null))
            return 6;

        if ((figures[2] == figures[6]) && (figures[2] != null) && (figures[4] == null))
            return 4;

        if ((figures[4] == figures[6]) && (figures[4] != null) && (figures[2] == null))
            return 2;

        return -1;
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

    private void showWinnerName(boolean isWin, Figure figure)
    {
        if (isWin)
        {
            GameView.showBoard(board);
            Player winner = players[0].getFigure().equals(figure) ? players[0] : players[1];
            System.out.println("\n\n" + winner.getName() + " win!");
        } else
        {
            System.out.println("\n\nNo winner.");
        }
    }
}
