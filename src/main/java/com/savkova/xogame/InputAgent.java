package com.savkova.xogame;

import com.savkova.xogame.entities.Difficulty;
import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.exceptions.NoExistPositionException;

import java.util.Scanner;

public class InputAgent
{
    private final Scanner sc;

    InputAgent()
    {
        this.sc = new Scanner(System.in);
    }

    String askPlayerName()
    {
        System.out.println("Enter your name, please:");
        String line = sc.nextLine();
        isQuit(line);
        return line;
    }

    Figure askFigureType()
    {
        System.out.println("Сhoose the figure you want to play ('X' or 'O'):");
        String line = "";
        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            isQuit(line);

            if ((line.equalsIgnoreCase(Figure.X.name()))
                    || (line.equalsIgnoreCase(Figure.O.name()))
                    || (line.equalsIgnoreCase("0")))
                break;
            else
                System.out.println("Try again, please:");
        }
        return (line.equalsIgnoreCase(Figure.X.name())) ? Figure.X : Figure.O;
    }

    int askMovePosition() throws NoExistPositionException
    {
        String line = "";
        int i = -1;
        System.out.println("\n\nEnter number from 1 to 9 for move:");

        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            isQuit(line);

            try
            {
                i = Integer.parseInt(line);
                if ((i > 0) && (i <= 9))
                    break;
                else
                    throw new NoExistPositionException();
            } catch (NumberFormatException e)
            {
                System.out.print("You did not enter a number. Try again, please: ");
            }

        }
        return i - 1;
    }

    public Difficulty askDifficulty()
    {
        System.out.println("Choose difficulty level, please: (e)asy or (m)edium:");
        String line = "";
        while (sc.hasNextLine())
        {
            line = sc.nextLine();
            isQuit(line);
            if ((line.equalsIgnoreCase(Difficulty.MEDIUM.name()))
                    || (line.equalsIgnoreCase(Difficulty.M.name()))
                    || (line.equalsIgnoreCase(Difficulty.EASY.name()))
                    || (line.equalsIgnoreCase(Difficulty.E.name())))
                break;
            else
                System.out.println("Try again, please:");
        }
        return (line.equalsIgnoreCase(Difficulty.MEDIUM.name()) || line.equalsIgnoreCase(Difficulty.M.name()))
                ? Difficulty.MEDIUM : Difficulty.EASY;
    }

    boolean askStartNewGame()
    {
        System.out.println("\nEnd game! Try again? (y/n)");
        String line = sc.nextLine();
        if (line.equalsIgnoreCase("y"))
        {
            return true;
        }
        if (line.equalsIgnoreCase("n"))
        {
            freeResources();
            System.exit(0);
        }

        return false;
    }

    private void isQuit(String line)
    {
        if (line.equalsIgnoreCase("quit"))
        {
            freeResources();
            System.exit(0);
        }
    }

    public void freeResources()
    {
        if (sc != null)
            sc.close();
    }
}
