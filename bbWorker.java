import java.util.PriorityQueue;
import java.util.*;

public class bbWorker {
    int nodeNumber = 1;
    private int[][] items;
    private double[] valPerWeight;
    private int maxWeight;
    private int numberOfItems;
    private PriorityQueue<Node> pq = new PriorityQueue<>(100, new SortingHat());
    Node bestNode = new Node();

    public bbWorker(){

    }

    
    public int getMaxWeight(){
        return 100;
    }

    public void fromFile(List<String> inputs){
        String[] splited;
        String z;
        
        maxWeight = Integer.parseInt(inputs.get(0));
        inputs.remove(0);
        
        numberOfItems = Integer.parseInt(inputs.get(0));
        inputs.remove(0);
        
        items = new int[numberOfItems][3];
        valPerWeight = new double[numberOfItems];
        
        for (int i=0; i < numberOfItems; i++){
            splited = inputs.get(0).split(" ");
            items[i][0] = i+1;
            items[i][1] = Integer.valueOf(splited[0]);
            items[i][2] = Integer.valueOf(splited[1]);
            valPerWeight[i] = (double) items[i][1] / items[i][2];
            inputs.remove(0);
        }
        
        z = "" + "Knapsack capacity: " + maxWeight + "\n" + "Items are:\n";
        
        for (int i=0; i < items.length; i++){
            z = z + items[i][0] + ": " + items[i][1] + " " + items[i][2] + " " + valPerWeight[i] + "\n";
        }
        System.out.println(z);
        
        engage();
        
    }
    
    private void engage(){
        int level = 0;
        Node first = new Node(calcFirstBound(), nodeNumber, level);
        pq.add(first);
        queueWorker();
        
        while (!(pq.peek() == null)){
            queueWorker();
        }
        bestOutput(bestNode);
    }
    
    private void queueWorker(){
        int nxtItem;
        Node n = pq.poll();
        ArrayList<Integer> tempTakenItems = convert(n.getTakenItems());
        ArrayList<Integer> tempExcludedItems = convert(n.getExcludedItems());
        nxtItem = nextItem(n.getTakenItems(), n.getExcludedItems());
        tempTakenItems.add(nxtItem);
        nodeNumber++;
        
        
        if ((nxtItem <= numberOfItems) && (n.getBound() > bestNode.getBound())){
            textOutput(n);
            Node leftChild = new Node(
                (revert(tempTakenItems)), 
                (revert(tempExcludedItems)), 
                calcBound(revert(tempTakenItems), revert(tempExcludedItems)), 
                calcWeight(revert(tempTakenItems)),
                calcValue(revert(tempTakenItems)),
                nodeNumber,
                (n.getLevel()+1)
                );
            tempTakenItems.remove(tempTakenItems.size()-1);
            tempExcludedItems.add(nxtItem);
                
            nodeNumber++;
            
            Node rightChild = new Node(
                (revert(tempTakenItems)), 
                (revert(tempExcludedItems)), 
                calcBound(revert(tempTakenItems), revert(tempExcludedItems)), 
                calcWeight(revert(tempTakenItems)),
                calcValue(revert(tempTakenItems)),
                nodeNumber,
                (n.getLevel()+1)
            );
            
            lChildOutput(leftChild);
            if ((leftChild.getWeight() < maxWeight) && (leftChild.getBound() >= bestNode.getBound())){
                pq.add(leftChild);
            } else {
                System.out.println("pruned because too heavy or bound is less than best node previously found");
            }
            rChildOutput(rightChild);
            if ((rightChild.getWeight() < maxWeight)  && (leftChild.getBound() >= bestNode.getBound())){
                pq.add(rightChild);
            } else {
                System.out.println("pruned because too heavy or bound is less than best node previously found");
            }
    
            if ((done(leftChild)) && (leftChild.getBound() >= bestNode.getBound()) && (leftChild.getWeight() >= 0)){
                System.out.println("Hit capacity so dont explore further\n Note achievable profit " + leftChild.getValue());
                bestNode = leftChild;
            }

            if ((done(rightChild)) && (rightChild.getBound() >= bestNode.getBound()) && (rightChild.getWeight() >= 0)){
                System.out.println("Hit capacity so dont explore further\n Note achievable profit " + rightChild.getValue());
                bestNode = rightChild;
            }
            System.out.println("Best Node number is: " + bestNode.getNodeNumber());

        } else {
            textOutput(n);
            System.out.println("pruned, dont explore children because bound " + n.getBound() + " is smaller than known achievable profit " + bestNode.getBound());
        }
    }
        
        private ArrayList<Integer> convert(int[] in){
        ArrayList<Integer> rInt = new ArrayList<Integer>();
        if (!(in == null)){
            for(int i:in){
                rInt.add(i);
            }
        }
        return rInt;
    }
    
    private int[] revert(ArrayList<Integer> in){
        int[] out = new int[in.size()];
        for(int i=0; i < in.size(); i++){
            out[i] = in.get(i);
        }
        return out;
    }
    
    private double calcFirstBound(){
        ArrayList<Integer> tempItems = new ArrayList<Integer>();
        double currentValue = 0;
        int nxtItem = 1;
        int remainingWeight = maxWeight;
        while (remainingWeight >= 0){
            if ((remainingWeight - items[nxtItem-1][2]) >= 0){
                tempItems.add(items[nxtItem-1][0]);
                remainingWeight = remainingWeight - items[nxtItem-1][2];
                currentValue += items[nxtItem-1][1];
                nxtItem++;
            } else {
                tempItems.add(items[nxtItem-1][0]);
                currentValue += remainingWeight * valPerWeight[nxtItem-1];
                remainingWeight=-1;
            }
        }
        return currentValue;
    }
    
    private double calcBound(int[] takenItems, int[] excludedItems){
        ArrayList<Integer> tempItems = new ArrayList<Integer>();
        double currentValue = calcValue(takenItems);
        int nxtItem = nextItem(takenItems, excludedItems);
        int weight = calcWeight(takenItems);
        int remainingWeight = remainWeight(weight);
        
        while (remainingWeight >= 0){
            if (nxtItem <= numberOfItems){
                if ((remainingWeight - items[nxtItem-1][2]) > 0){
                    tempItems.add(items[nxtItem-1][0]);
                    remainingWeight -= items[nxtItem-1][2];
                    currentValue += items[nxtItem-1][1];
                    nxtItem++;
                } else {
                    tempItems.add(items[nxtItem-1][0]);
                    currentValue += remainingWeight * valPerWeight[nxtItem-1];
                    remainingWeight = -1;
                }
            } else {
                remainingWeight = -1;
            }
        }
        return currentValue;
    }
    
    private int nextItem(int[] taken, int[] excluded){
        int nxtItem = 0;
        if (!(taken == null)){
            for(int i: taken){
                if (i > nxtItem){
                    nxtItem = i;
                }
            }
        }
        if (!(excluded == null)){
            for(int i: excluded){
                if (i > nxtItem){
                    nxtItem = i;
                }
            }
        }
        if (nxtItem == 0){
            return 1;
        } else {
            return nxtItem+1;
        }
    }

    private double calcValue(int[] takenItems){
        int cValue=0;
        for(int i:takenItems){
            cValue += items[i-1][1];
        }
        return cValue;
    }
    
    private int calcWeight(int[] uItems){
        int weight = 0;
        for(int i:uItems){
            weight += items[i-1][2];
        }
        return weight;
    }
    
    private int remainWeight(int currentWeight){
        return (maxWeight - currentWeight);
    }
    
    private void textOutput(Node n){
        System.out.print("Exploring <" + n.toString() + ">\n");
    }

    private void bestOutput(Node n){
        System.out.print("Best Node: <" + n.toString() + ">\n");
    }
    
    private void lChildOutput(Node n){
        System.out.println("Left child is <" + n.toString() + ">");
    }

    private void rChildOutput(Node n){
        System.out.println("Right child is <" + n.toString() + ">");
    }
    
    private boolean done(Node node){
        return ((node.getBound() == node.getValue() && node.getWeight() <= maxWeight));        
    }
}

class SortingHat implements Comparator<Node>{
    @Override
    public int compare(Node node, Node node2){
        if (node.getBound() < node2.getBound()){
            return 1;
        } else if (node.getBound() > node2.getBound()){
            return -1;
        }
        else {
            return 0;
        }
    }
}