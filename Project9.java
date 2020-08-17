import java.util.*;
import java.io.*;

public class Project9 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] msgs = {"Enter file name to import:", "File not Found"};
        String filename;
        List<String> lines = new ArrayList<>();
        bbWorker bbw = new bbWorker();
        File input;
        boolean loop=true;

        System.out.println(msgs[0]);
        filename = sc.nextLine();

        try {
            input = new File(filename);
            Scanner fscan = new Scanner(input);
            
            while (loop){
                lines.add(fscan.nextLine());
                if (!(fscan.hasNextLine())){
                    loop=false;
                }
            }
            
            fscan.close();
        } catch (FileNotFoundException e){
            System.err.println(msgs[1]);
        }
    
        bbw.fromFile(lines);

    sc.close();
    }    
}