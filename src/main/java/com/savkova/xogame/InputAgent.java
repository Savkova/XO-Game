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
        System.out.print("Enter your name, please: ");
        String line = scanner.nextLine();
        Main.quit(line);
        return line;
    }

    public Figure askFigureType()
    {
        System.out.print("Сhoose the figure you want to play ('X' or 'O'): ");
        String line = "";
        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();
            Main.quit(line);

            if (Figure.isFigure(line))
                break;
            else
                System.out.print("Try again, please: ");
        }
        return (line.equalsIgnoreCase(Figure.X.name())) ? Figure.X : Figure.O;
    }

    public int askMovePosition() throws NoExistPositionException
    {
        String line = "";
        int i = -1;
        System.out.print("\n\nYour move: ");

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
        System.out.print("\n\nEnd game! Try again? (y/n): ");
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
