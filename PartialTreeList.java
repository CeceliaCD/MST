package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;
import structures.MinHeap;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		PartialTreeList ptlist = new PartialTreeList ();
		
		for(Vertex vert : graph.vertices) {
			
			PartialTree v = new PartialTree(vert);
			
			Vertex.Neighbor ptr = vert.neighbors;
			
			while(ptr != null) {
				Vertex v1 = vert;
				Vertex v2 = ptr.vertex;
				int wght = ptr.weight;
				
				Arc dist = new Arc(v1, v2, wght);
				v.getArcs().insert(dist);
				ptr = ptr.next;
			}
			
			 System.out.println(vert.name + ": " +  v.toString());
			ptlist.append(v);
		}
		return ptlist;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {

		/* COMPLETE THIS METHOD */
		ArrayList<Arc> ptArcs = new ArrayList<Arc>();

		while(ptlist.size > 1) {
			PartialTree PTX = ptlist.remove();
			Arc alpha = PTX.getArcs().deleteMin();
			

			// If v2 also belongs to PTX, go back to Step 4 and pick the next highest priority arc, otherwise continue to the next step.
			//  C belongs to a different partial tree (T3) than A, continue to the next step.
			while(alpha.getv2().getRoot() == PTX.getRoot()) {
				alpha = PTX.getArcs().deleteMin();
			}
			//Report α - this is a component of the minimum spanning tree.
			ptArcs.add(alpha);
			//Find the partial tree PTY to which v2 belongs. Remove PTY from the partial tree list L. Let PQY be PTY's priority queue.
			PartialTree PTY = ptlist.removeTreeContaining(alpha.getv2());
			PTY.merge(PTX);
			ptlist.append(PTY);
			
		}
		return ptArcs;
	}

    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    		throws NoSuchElementException {
    	/* COMPLETE THIS METHOD */
    	Node tempptr = this.rear;
    	Node ahead = this.rear.next;
    	

    	PartialTree returnedTree = null;
    	
    	if(size == 0) {
    		throw new NoSuchElementException();
    	}


    	if(tempptr.tree.getRoot() == vertex.getRoot()) {
    		size--;
    		Node tempptr2 = tempptr;
    		tempptr = ahead;
    		return returnedTree = tempptr2.tree;

    	}else {
    		while(ahead != rear) {
    			if(ahead.tree.getRoot() == vertex.getRoot()) { 
    				size--;
    				tempptr.next = ahead.next;
    				return returnedTree = ahead.tree;
    		
    			}
    			tempptr = ahead;
    			ahead = ahead.next;	
    		}

    	}

    		throw new NoSuchElementException();
    }

    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}
