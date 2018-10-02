package com.savkova.xogame;

import com.savkova.xogame.entities.Board;
import com.savkova.xogame.entities.Difficulty;
import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.entities.Game;
import com.savkova.xogame.exceptions.AlreadyOccupiedException;
import com.savkova.xogame.exceptions.NoExistPositionException;

import java.util.Random;

public class MoveController
{
    public static boolean isWin = false;
    private XOGameStart xoGameStart;

    public MoveController(XOGameStart xoGameStart)
    {
        this.xoGameStart = xoGameStart;
    }

    boolean move(Game game)
    {
        if (isWin)
        {
            System.out.println("\nWin!");
            return false;
        }

        Board board = game.getBoard();
        Figure playerFigure = game.getPlayers()[0].getFigure();
        Figure computerFigure = game.getPlayers()[1].getFigure();
        Figure currentFigure = getCurrentMoveFigure(board);
        int position = -1;


        if ((currentFigure != null) && (currentFigure.equals(playerFigure)))
        {
            try
            {
                position = xoGameStart.getConsole().askMovePosition();
                putFigure(board, position, playerFigure);
                isWin = isWinner(board, position);
            } catch (NoExistPositionException e)
            {
                System.out.println("No such position on this board.");
            } catch (AlreadyOccupiedException e)
            {
                System.out.println("This place is already occupied, enter other position:");
            }
        } else if ((currentFigure != null) && (currentFigure.equals(computerFigure)))
        {
            if (game.getLevel().equals(Difficulty.EASY) || game.getLevel().equals(Difficulty.E))
            {
                try
                {
                    Thread.sleep(1000);

                    position = opponentRandomMove(board, computerFigure);
                    isWin = isWinner(board, position);
                } catch (InterruptedException e)
                {
                }
            } else
            {
                int prevPos = position;
                opponentStrategyMove(board, computerFigure, prevPos);
            }
        } else if (currentFigure == null)
        {
            isWin = isWinner(board, position);
        } else
        {
                boolean isStartNewGame = false;
                while (!isStartNewGame)
                    isStartNewGame = xoGameStart.getConsole().askStartNewGame();
                xoGameStart.startGame();
        }
        return true;
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

    void putFigure(final Board board, final int position, final Figure figure) throws AlreadyOccupiedException
    {
        if (board.getFigure(position) != null)
            throw new AlreadyOccupiedException();

        board.setFigure(position, figure);
    }

    int opponentRandomMove(final Board board, final Figure figure)
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

    int opponentStrategyMove(final Board board, final Figure figure, final int prevPos)
    {
        //TODO
        Figure[] figures = board.getFigures();
        int count = 0;
        try
        {
            if (figure.equals(Figure.X))
            {
                int position = -2;
                if (prevPos == -1)
                {
                    position = 4;
                    putFigure(board, position, figure);
                    System.out.println("\n\nOpponent move: " + (position + 1));

                }

                for (Figure f : figures)
                {
                    if (f != null)
                        count++;
                }
                if ((count == 1) && (prevPos % 2 == 0))
                {
                    for (int i = 0; i < 5; i++)
                    {
                        position = position + 2;
                        if ((position != prevPos) && (position != 4)) ;
                        break;
                    }
                    putFigure(board, position - 1, figure);
                }
            } else
            {
                //TODO for 0
            }
        } catch (AlreadyOccupiedException e)
        {
            opponentRandomMove(board, figure);
        }


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

}
