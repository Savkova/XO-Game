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
            return false;

        Figure humanFigure = players[0].getFigure();
        Figure computerFigure = players[1].getFigure();
        Figure currentFigure = getCurrentMoveFigure(board);

        InputAgent inputAgent = Main.getInputAgent();

        int position;
        // human move
        if ((currentFigure != null) && (currentFigure.equals(humanFigure)))
        {
            System.out.println("\n");
            GameView.showBoard(board);
            try
            {
                position = inputAgent.askMovePosition();
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
            // computer move
        } else if ((currentFigure != null) && (currentFigure.equals(computerFigure)))
        {
            position = 0;
            try
            {
                position = computerMove(board);
                putFigure(board, position, computerFigure);
                System.out.println("\n\nComputer move: " + (position + 1));
            } catch (AlreadyOccupiedException e) {}

            if (isWinner(board, position))
            {
                isWin = true;
                showWinnerName(true, computerFigure);
            }
            // board full
        } else if (currentFigure == null)
        {
            showWinnerName(false, null);
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

    private int computerMove(final Board board)
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
            for (int i = 0; i < emptyIndexes.size(); i++)
            {
                //check rows
                int row = emptyIndexes.get(i) - emptyIndexes.get(i) % 3;
                if (((figures[row] == figures[row + 1]) && (figures[row] != null))
                        || ((figures[row] == figures[row + 2]) && (figures[row] != null))
                        || ((figures[row + 1] == figures[row + 2]) && (figures[row + 1] != null)))
                    return emptyIndexes.get(i);

                //check columns
                int column = emptyIndexes.get(i) % 3;
                if (((figures[column] == figures[column + 3]) && (figures[column] != null))
                        || ((figures[column] == figures[column + 6]) && (figures[column] != null))
                        || ((figures[column + 3] == figures[column + 6]) && (figures[column + 3] != null)))
                    return emptyIndexes.get(i);
                //check left diagonal
                if (emptyIndexes.get(i) % 4 == 0)
                {
                    if (((figures[0] == figures[4]) && (figures[0] != null))
                            || ((figures[0] == figures[8]) && (figures[0] != null))
                            || ((figures[4] == figures[8]) && (figures[4] != null)))
                        return emptyIndexes.get(i);
                // check right diagonal
                } else if (((figures[2] == figures[4]) && (figures[2] != null))
                        || ((figures[2] == figures[6]) && (figures[2] != null))
                        || ((figures[4] == figures[6]) && (figures[4] != null)))
                    return emptyIndexes.get(i);
            }
        }
        //random choice
        while (true)
        {
            index = random.nextInt(emptyIndexes.size());
            if (board.getFigure(index) == null)
                break;
        }
        return emptyIndexes.get(index);
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
