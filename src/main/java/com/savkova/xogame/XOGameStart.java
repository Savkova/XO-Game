package com.savkova.xogame;

public class XOGameStart
{
    private InputAgent console;

    private XOGameStart()
    {
        this.console = new InputAgent();
    }

    public static void main(String[] args)
    {
        XOGameStart xoGameStart = new XOGameStart();
        System.out.println("Welcome to XO-Game!");
        System.out.println("(enter \'quit\' for quit)\n");
        xoGameStart.startGame();
    }

    public void startGame()
    {
        GameView gameView = new GameView(console);
        System.out.println("\nLet's start!\n");
        gameView.showPlayers();
        gameView.showBoard();

        MoveController moveController = new MoveController(this);
        while (moveController.move(gameView.getGame()))
        {
                gameView.showBoard();
        }

        console.freeResources();
    }

    InputAgent getConsole()
    {
        return console;
    }
}
