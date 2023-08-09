package controller;

import exceptions.BotCountMoreThanOneException;
import exceptions.DuplicateSymbolException;
import exceptions.PlayerCountException;
import models.Game;
import models.GameState;
import models.Player;
import strategies.winningstratagies.WinningStrategy;

import java.util.List;

public class GameController {
    //Should GameController stateful or stateless
    //it should be stateful so in future can control multiple games -> So there will be one game controller and multiple games
    //stateful means creating a global variable of game
    //But in case of multiple thread env, any thread can perform some action on one of the game and other thread checking the state of the game
    //In that case it should be stateless -> removing gloabal variable of game

    //Game would be created inside startGame using BuilderDesignPattern
    public Game startGame(int dimensions, List<Player> players, List<WinningStrategy> winningStrategyList) throws BotCountMoreThanOneException, PlayerCountException, DuplicateSymbolException {
        return Game.builder()
                .setDimensions(dimensions)
                .setPlayers(players)
                .setWinningStrategies(winningStrategyList)
                .build();
    }

    public void makeMove(Game game){
        game.makeMove();

    }
    public GameState checkGameState(Game game){
        return game.getGameState();
    }

    public void printBoard(Game game){
        game.printBoard();
    }

    public void undo(Game game){
        game.undo();
    }

}
