package models;

import exceptions.BotCountMoreThanOneException;
import exceptions.DuplicateSymbolException;
import exceptions.PlayerCountException;
import strategies.winningstratagies.WinningStrategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private List<Player> players;
    private Board board;
    private List<Move> moves;
    private List<WinningStrategy> winningStrategies;
    private Player winner;
    private int nextPlayerIndex;
    private GameState gameState;
    private int dimensions;

    private Game(List<Player> players, List<WinningStrategy> winningStrategies, int dimensions) {
        this.players = players;
        this.board = new Board(dimensions);
        this.moves = new ArrayList<>();
        this.winningStrategies = winningStrategies;
        this.nextPlayerIndex = 0;
        this.dimensions = dimensions;
        this.gameState = GameState.IN_PROGRESS;
    }

    public static Builder builder(){
        return new Builder();
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public List<WinningStrategy> getWinningStrategies() {
        return winningStrategies;
    }

    public void setWinningStrategies(List<WinningStrategy> winningStrategies) {
        this.winningStrategies = winningStrategies;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }

    public void setNextPlayerIndex(int nextPlayerIndex) {
        this.nextPlayerIndex = nextPlayerIndex;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    //whatever we need from Builder only those information we put, and which are provided by client
    public static class Builder{
        private List<Player> players;
        private List<WinningStrategy> winningStrategies;
        private int dimensions;

        public Builder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Builder setWinningStrategies(List<WinningStrategy> winningStrategies) {
            this.winningStrategies = winningStrategies;
            return this;
        }

        public Builder setDimensions(int dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        //validations
        private void validateBotCount() throws BotCountMoreThanOneException{
            int botCount = 0;
            for(Player player : players){
                if(player.getPlayerType().equals(PlayerType.BOT)){
                    botCount++;
                }
            }
            if(botCount > 1){
                throw new BotCountMoreThanOneException();
            }
        }

        private void validateDimensionAndPlayerCount() throws PlayerCountException {
            if(players.size() != this.dimensions - 1){
                throw new PlayerCountException();
            }
            //size should not be zero or -ve
        }

        private void validateSymbolUniqueness() throws DuplicateSymbolException{
            Set<Character> symbolSet = new HashSet<>();
            for(Player player : players){
                if(!symbolSet.add(player.getSymbol().getSymbol())){
                    throw new DuplicateSymbolException();
                }
            }
        }

        private void validate() throws BotCountMoreThanOneException, PlayerCountException, DuplicateSymbolException {
            validateBotCount();
            validateDimensionAndPlayerCount();
            validateSymbolUniqueness();
        }
        public Game build() throws BotCountMoreThanOneException, PlayerCountException, DuplicateSymbolException {
            validate();
            return new Game(players, winningStrategies, dimensions);
        }
    }

    private boolean validateMove(Move move){
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();

        if(row >= board.getSize() || col >= board.getSize()){
            return false;
        }
        else if(!board.getBoard().get(row).get(col).getCellState().equals(CellState.EMPTY)){
            return false;
        }
        return true;
    }

    private boolean checkWinner(Board board, Move move){
        for(WinningStrategy winningStrategy : winningStrategies){
            if(winningStrategy.checkWinner(board, move)){
                return true;
            }
        }
        return false;
    }
    public void makeMove(){
        Player currentMovePlayer = players.get(nextPlayerIndex);
        System.out.println("It is " + currentMovePlayer.getName() + "'s turn. Please make your move");
        Move currentPlayerMove = currentMovePlayer.makeMove(board);
        if(!validateMove(currentPlayerMove)){
            System.out.println("Invalid move. Please try again");
            return;
        }
        int row = currentPlayerMove.getCell().getRow();
        int col = currentPlayerMove.getCell().getCol();

        Cell cellToUpdate = board.getBoard().get(row).get(col);
        cellToUpdate.setCellState(CellState.FILLED);
        cellToUpdate.setPlayer(currentMovePlayer);

        Move finalMoveObject = new Move(cellToUpdate, currentMovePlayer); //reference of board
        moves.add(finalMoveObject);

        nextPlayerIndex++;
        nextPlayerIndex %= players.size();//to limit number of players

        if(checkWinner(board, finalMoveObject)){
            gameState = GameState.WIN;
            winner = currentMovePlayer;
        }else if(moves.size() == this.board.getSize() * this.board.getSize()){
            gameState = GameState.DRAW;
        }
    }

    public void printBoard(){
        board.printBoard();
    }

    public void undo(){
        if(moves.isEmpty()){
            System.out.println("No move to undo");
            return;
        }

        Move lastMove = moves.get(moves.size() - 1);
        moves.remove(lastMove);

        Cell cell = lastMove.getCell();
        cell.setPlayer(null);
        cell.setCellState(CellState.EMPTY);

        // need to update winning strategies map
        handleUndo(board, lastMove);

        nextPlayerIndex--;
        nextPlayerIndex = (nextPlayerIndex + players.size()) % players.size();
    }

    private void handleUndo(Board board, Move lastMove) {
        for(WinningStrategy winningStrategy : winningStrategies){
            winningStrategy.handleUndo(board,lastMove);
        }
    }
}
