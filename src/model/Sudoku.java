package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Sudoku {
	private Cell[][] grid;
	
	public Sudoku(){
		this.setGrid(new Cell[9][9]);
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				getGrid()[i][j] = new Cell(0, i, j);
			}
		}
	}

	public void parse(String file){
		try {
			Scanner scan = new Scanner(new FileReader("sudoku1.txt"));
			int readInt;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					readInt = scan.nextInt();
					getGrid()[i][j].setValue(readInt);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printSudoku(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(getGrid()[i][j].getValue() +" ");
			}
			System.out.print("\n");
		}
	}

	public Cell[][] getGrid() {
		return grid;
	}

	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}
	
	public Cell getCell(int line, int column){
		return grid[line][column];
	}
	public void setCell(Cell newCell){
		this.grid[newCell.getL()][newCell.getC()] = newCell;
	}
	public void setCell(int line, int column, int v, ArrayList<Integer> pv){
		this.grid[line][column].setValue(v);
		this.grid[line][column].setPossibleValues(pv);
	}
}
