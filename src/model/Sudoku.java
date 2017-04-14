package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
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
			Scanner scan = new Scanner(new FileReader(file));
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
	
	public Sudoku(String f){
		this.setGrid(new Cell[9][9]);
		try {
			Scanner scan = new Scanner(new FileReader(f));
			for(int l=0; l<9; l++){
				for(int c=0; c<9; c++){
					setCell(new Cell(scan.nextInt(), l, c));
				}
			}
			scan.close();
//			updatePossibleValues();
			this.printSudoku();
			this.printAll();
		} catch (FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void printSudoku(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(grid[i][j].getValue() +" ");
			}
			System.out.print("\n");
		}
	}
	
	public void printAll(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(grid[i][j].getValue() + "" + grid[i][j].getPossibleValues() + " ");
//						+ "" + grid[i][j].getPossibleValues().size() + " ");
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
	
	public Group getLineGroup(int l){
		Group result = new Group();
		for(int j=0; j<9; j++){
			result.addElement(grid[l][j]);
		}
		return result;
	}
	public Group getColumnGroup(int c){
		Group result = new Group();
		for(int i=0; i<9; i++){
			result.addElement(grid[i][c]);
		}
		return result;
	}
	public Group getBlockGroup(int lmin, int lmax, int cmin, int cmax){
		// /!\ no error raised if lmin/lmax or cmin/cmax problems
		Group result = new Group();
		for(int l=lmin; l<=lmax; l++){
			for(int c=cmin; c<=cmax; c++){
				result.addElement(grid[l][c]);
			}
		}
		return result;
	}
	
	public void updatePossibleValues(){
		HashSet<Integer> usedValues;
		HashSet<Integer> pv;
		ArrayList<Integer> newPossibleValues;
		int lmin, cmin;
		for(int l=0; l<9; l++){
			pv = new HashSet<Integer>();
			lmin = (l/3)*3;
			for(int c=0; c<9; c++){
				usedValues = new HashSet<Integer>();
				usedValues.addAll(getLineGroup(l).mapValues());
//				System.out.println(usedValues);
				usedValues.addAll(getColumnGroup(c).mapValues());
//				System.out.println(usedValues);
				lmin = (l/3)*3;
				cmin = (c/3)*3;
//				System.out.println(lmin + " - " + cmin);
				usedValues.addAll(getBlockGroup(lmin, (lmin+2), cmin, (cmin+2)).mapValues());
//				System.out.println(usedValues);
//				System.out.println("-----");
				if(grid[l][c].getValue() == 0){
					cmin = (c/3)*3;
					for(int k=1; k<=9; k++){
						if(!usedValues.contains(k)){
							pv.add(k);
						}
					}
					newPossibleValues = new ArrayList<Integer>();
					newPossibleValues.addAll(pv);
					grid[l][c].setPossibleValues(newPossibleValues);
				} else {
					grid[l][c].setPossibleValues(new ArrayList<Integer>());
				}
			}
		}
	}
}
