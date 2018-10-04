package com.savkova.xogame;

import com.savkova.xogame.entities.Figure;
import com.savkova.xogame.exceptions.NoExistPositionException;

import java.util.Scanner;

public class InputAgent
{
    private final Scanner scanner;

    InputAgent(Scanner scanner)
    {
        this.scanner = scanner;
    }

    public String askPlayerName()
    {
        System.out.println("Enter your name, please:");
        String line = scanner.nextLine();
        Main.quit(line);
        return line;
    }

    public Figure askFigureType()
    {
        System.out.println("Сhoose the figure you want to play ('X' or 'O'):");
        String line = "";
        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();
            Main.quit(line);

            if ((line.equalsIgnoreCase(Figure.X.name()))
                    || (line.equalsIgnoreCase(Figure.O.name()))
                    || (line.equalsIgnoreCase("0")))
                break;
            else
                System.out.println("Try again, please:");
        }
        return (line.equalsIgnoreCase(Figure.X.name())) ? Figure.X : Figure.O;
    }

    public int askMovePosition() throws NoExistPositionException
    {
        String line = "";
        int i = -1;
        System.out.println("\n\nEnter number from 1 to 9 for move:");

        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();
            Main.quit(line);

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
        return (i - 1);
    }

    public boolean askStartNewGame()
    {
        System.out.println("\n\nEnd game! Try again? (y/n)");
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase("y"))
        {
            return true;
        }
        if (line.equalsIgnoreCase("n"))
        {
            Main.quit("quit");
        }

        return false;
    }
}
