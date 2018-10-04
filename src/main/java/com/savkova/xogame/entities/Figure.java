package com.savkova.xogame.entities;

public enum Figure
{
    X, O;

    public static boolean isFigure(String s)
    {
        if ((s.equalsIgnoreCase(Figure.X.name()))
                || (s.equalsIgnoreCase(Figure.O.name()))
                || (s.equalsIgnoreCase("0"))
                || (s.equalsIgnoreCase("Х"))
                || (s.equalsIgnoreCase("О")))
            return true;
        else
            return false;
    }
}