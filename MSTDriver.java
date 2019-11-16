package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import structures.Arc;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;
import structures.MinHeap;


public class MSTDriver {
	
		static Scanner stdin = new Scanner(System.in);
		
		public static void main(String[] args) throws IOException {
			
			System.out.println("Enter File Name for Graph: ");
			String txtFile = stdin.nextLine();
			Graph newGraph = new Graph(txtFile);
			
			PartialTreeList test = PartialTreeList.initialize(newGraph);
			System.out.println();
			
			System.out.println("The is the Minimum Spanning Tree: ");
			
			System.out.println(PartialTreeList.execute(test));
			
		}
	}
