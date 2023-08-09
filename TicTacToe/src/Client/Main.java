package Client;

import controller.GameController;
import models.*;
import strategies.botplayingstrategies.BotPlayingStrategy;
import strategies.botplayingstrategies.EasyBotPlayingStrategy;
import strategies.winningstratagies.ColWinningStrategy;
import strategies.winningstratagies.DiagonalWinningStrategy;
import strategies.winningstratagies.RowWinningStrategy;
import strategies.winningstratagies.WinningStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);
        Game game = null;

        int dimensionsOfGame = 3;
        List<Player> players = new ArrayList<>();
        players.add(
                new Player(new Symbol('X'), "Alexa", 1l, PlayerType.HUMAN)
        );
        players.add(
                new Bot(2l, "GPT", new Symbol('O'), BotDifficultyLevel.EASY)
        );
        List<WinningStrategy> winningStrategies = new ArrayList<>();
        winningStrategies.add(new RowWinningStrategy());
        winningStrategies.add(new ColWinningStrategy());
        winningStrategies.add(new DiagonalWinningStrategy());

        try {
            game = gameController.startGame(dimensionsOfGame, players, winningStrategies);
            while(gameController.checkGameState(game).equals(GameState.IN_PROGRESS)){
                gameController.printBoard(game);

                System.out.println("Does you want to undo your last move (y/n)");
                String undoAnswer = scanner.next();
                if(undoAnswer.equalsIgnoreCase("y")){
                    gameController.undo(game);
                }

                gameController.makeMove(game);
            }
        } catch (Exception e){
            System.out.println("Something went wrong");
        }

        if(gameController.checkGameState(game).equals(GameState.WIN)){
            System.out.println("Player " + game.getWinner().getName() + " wins the game");
        }else if(gameController.checkGameState(game).equals(GameState.DRAW)){
            System.out.println("The game is draw");
        }
    }
}