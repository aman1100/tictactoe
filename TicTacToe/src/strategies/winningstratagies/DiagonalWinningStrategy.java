package strategies.winningstratagies;

import models.Board;
import models.Move;
import models.Symbol;

import java.util.HashMap;
import java.util.Map;

public class DiagonalWinningStrategy implements WinningStrategy{

    private Map<Symbol, Integer> leftDiagonalCount = new HashMap<>();
    private Map<Symbol, Integer> rightDiagonalCount = new HashMap<>();
    @Override
    public boolean checkWinner(Board board, Move move) {
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();

        //check for left diagonal
        if(row == col){
            leftDiagonalCount.put(symbol, leftDiagonalCount.getOrDefault(symbol, 0) + 1);
            if(leftDiagonalCount.get(symbol) == board.getSize()){
                return true;
            }
        }

        //check for right diagonal
        if(row + col == board.getSize() - 1){
            rightDiagonalCount.put(symbol, rightDiagonalCount.getOrDefault(symbol, 0) + 1);
            if(rightDiagonalCount.get(symbol) == board.getSize()){
                return true;
            }
        }

        return false;
    }

    @Override
    public void handleUndo(Board board, Move move) {
        int row = move.getCell().getRow();
        int col = move.getCell().getCol();
        Symbol symbol = move.getPlayer().getSymbol();

        //check for left diagonal
        if(row == col){
            leftDiagonalCount.put(symbol, leftDiagonalCount.get(symbol) - 1);
        }
        //check for right diagonal
        if(row + col == board.getSize() - 1){
            rightDiagonalCount.put(symbol, rightDiagonalCount.get(symbol) - 1);
        }
    }
}
