public class Node {
    private int[] takenItems;
    private int[] excludedItems;
    private int weight, nodeNumber, level;
    private double value, bound;

    public Node() {

    }

    public Node(double bound, int nodeNumber, int level){
        this.bound = bound;
        this.nodeNumber = nodeNumber;
        this.level = level;
    }

    public Node(int[] takenItems, int[] excludedItems, double bound, int weight, double value, int nodeNumber, int level){
        this.takenItems = takenItems.clone();
        this.excludedItems = excludedItems.clone();
        this.bound = bound;
        this.weight = weight;
        this.value = value;
        this.nodeNumber = nodeNumber;
        this.level = level;
    }

    public int[] getItems(){
        if (!(takenItems == null && excludedItems == null)){
            int[] newReturn = new int[(takenItems.length + excludedItems.length)];
            System.arraycopy(takenItems, 0, newReturn, 0, (takenItems.length));
            System.arraycopy(excludedItems, 0, newReturn, 0, (excludedItems.length));
            return newReturn;
        } else {
            return null;
        }
    }

    public int[] getTakenItems(){
        if(!(takenItems == null)){
            return takenItems;
        } else {
            return null;
        }
    }

    public int[] getExcludedItems(){
        if(!(excludedItems == null)){
            return excludedItems;
        } else {
            return null;
        }
    }
    
    public int getNodeNumber(){
        return Integer.valueOf(nodeNumber);
    }

    public int getLevel(){
        return Integer.valueOf(level);
    }

    public int getWeight(){
        if (weight > 0){
            return Integer.valueOf(weight);
        } else {
            return 0;
        }
    }

    public double getValue(){
        if (value > 0){
            return Double.valueOf(value);
        } else {
            return 0;
        }
    }

    public double getBound() {
        return Double.valueOf(bound);
    }

    @Override
    public String toString(){
        String items;
        if (takenItems == null){
            items = "[]";
        } else if(takenItems.length == 1){
            items = "[" + takenItems[0] + "]";
        } else {        
            items = "[";
            for (int i=0; i < takenItems.length; i++){
                items += takenItems[i];
                if (i < takenItems.length-1){
                    items += ", ";
                }
            }
            items += "]";
        }

        String eitems;
        if (excludedItems == null){
            eitems = "[]";
        } else if(excludedItems.length == 1){
            eitems = "[" + excludedItems[0] + "]";
        } else {        
            eitems = "[";
            for (int i=0; i < excludedItems.length; i++){
                eitems += excludedItems[i];
                if (i < excludedItems.length-1){
                    eitems += ", ";
                }
            }
            eitems += "]";
        }

        return (
            "Node: " + nodeNumber + "\t" +
            "items: " + items + " excluded items: " + eitems + 
            " Level: " + level +
            " profit: " + value + 
            " weight: " + weight +
            " bound: " + bound
        );
    }
    
}