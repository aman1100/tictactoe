package models;

import strategies.botplayingstrategies.BotPlayingStrategy;

public class Bot extends Player{
    private BotDifficultyLevel botDifficultyLevel;
    private BotPlayingStrategy botPlayingStartegy;

    public Bot(Long id, String name, Symbol symbol, BotDifficultyLevel botDifficultyLevel){
        super(symbol, name, id, PlayerType.BOT);
        this.botDifficultyLevel = botDifficultyLevel;
    }

    public BotDifficultyLevel getBotDifficultyLevel() {
        return botDifficultyLevel;
    }

    public void setBotDifficultyLevel(BotDifficultyLevel botDifficultyLevel) {
        this.botDifficultyLevel = botDifficultyLevel;
    }

    public BotPlayingStrategy getBotPlayingStartegy() {
        return botPlayingStartegy;
    }

    public void setBotPlayingStartegy(BotPlayingStrategy botPlayingStartegy) {
        this.botPlayingStartegy = botPlayingStartegy;
    }

    @Override
    public Move makeMove(Board board){
        Move botMove = botPlayingStartegy.makeMove(board);
        botMove.setPlayer(this);
        return botMove;
    }
}
