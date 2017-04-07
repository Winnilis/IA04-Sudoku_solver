package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Sudoku {
	Cell[][] grid;
	
	public Sudoku(){
		grid = new Cell[9][9];
	}

	public void parse(String file){
		try {
			Scanner scan = new Scanner(new FileReader("sudoku1.txt"));
			int readInt;
			for(int i=0; i<9; i++){
				for(int j=0; j<9; j++){
					readInt = scan.nextInt();
					System.out.println(readInt);
					grid[i][j].setValue(readInt);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void printSudoku(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(grid[i][j].value +" ");
			}
			System.out.println("");
		}
	}
}
