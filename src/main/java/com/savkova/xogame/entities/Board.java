package com.savkova.xogame.entities;

public class Board
{
    private final int size = 3;
    private final Figure[] figures;

    public Board()
    {
        this.figures = new Figure[size*size];
    }

    public int getSize()
    {
        return size;
    }

    public Figure getFigure(final int position) {
        return figures[position];
    }

    public Figure[] getFigures()
    {
        return figures;
    }

    public void setFigure(final int position, final Figure figure) {
        figures[position] = figure;
    }
}
