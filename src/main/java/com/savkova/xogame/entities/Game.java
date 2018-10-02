package com.savkova.xogame.entities;

public class Game
{
    private final Board board;
    private final Player[] players;
    private final Difficulty level;

    public Game(final Board board, final Player[] players, Difficulty level)
    {
        this.board = board;
        this.players = players;
        this.level = level;
    }

    public Board getBoard()
    {
        return board;
    }

    public Player[] getPlayers()
    {
        return players;
    }

    public Difficulty getLevel()
    {
        return level;
    }
}
