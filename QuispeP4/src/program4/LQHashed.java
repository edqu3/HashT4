package program4;

import javax.swing.JOptionPane;

public class LQHashed {

    private int n;
    private int nd;                                         // dummy node count
    private int N;
    private final int DEFAULT_QUOTIENT = 9967;
    private final int NODE_WIDTH;
    private final double MAX_LOADING_FACTOR = 0.75;
    private final StudentListing deleted = new StudentListing();    // V2
    private StudentListing[] sListings;

    public LQHashed() {
        int size = Integer.parseInt(
                JOptionPane.showInputDialog("How many nodes will this structure hold?"));
        NODE_WIDTH = Integer.parseInt(
                JOptionPane.showInputDialog("Enter size of the node width"));
        N = fourKPlus3(size, (1.0 / MAX_LOADING_FACTOR - 1) * 100.00);
        sListings = new StudentListing[N];
    }

    public LQHashed(int size) {
        NODE_WIDTH = Integer.parseInt(
                JOptionPane.showInputDialog("Enter size of the node width"));
        N = fourKPlus3(size, (1.0 / MAX_LOADING_FACTOR - 1) * 100.00); //33%
        sListings = new StudentListing[N];
        for (StudentListing sl : sListings) {
            sl = null;
        }
    }

    public static int fourKPlus3(int n, double pct) // from Figure 5.16
    {
        boolean fkp3 = false;
        boolean aPrime = false;
        int prime, highDivisor, d;
        double pctd = pct;
        prime = (int) (n * (1.0 + (pctd / 100.0)));  // guess the prime pct percent larger than n
        if (prime % 2 == 0) //if even make the prime guess odd
        {
            prime = prime + 1;
        }
        while (fkp3 == false) // not a 4k+3 prime
        {
            while (aPrime == false) // not a prime
            {
                highDivisor = (int) (Math.sqrt(prime) + 0.5);
                for (d = highDivisor; d > 1; d--) {
                    if (prime % d == 0) {
                        break;
                    }
                }
                if (d != 1) // prime not found
                {
                    prime = prime + 2;
                } else {
                    aPrime = true;
                }
            }// end of the prime search loop
            if ((prime - 3) % 4 == 0) {
                fkp3 = true;
            } else {
                prime = prime + 2;
                aPrime = false;
            }
        }// end of 4k+3 prime search loop
        return prime;
    }

    public boolean expand() {
        int newN = fourKPlus3(N * 2, (1.0 / MAX_LOADING_FACTOR - 1) * 100.00);
        StudentListing[] temp = sListings;
        sListings = new StudentListing[newN];
        n = 0;
        N = newN;   //set new array size
        System.out.println("\nExpanding Array, re-inserting nodes into expanded array\n");
        for (StudentListing sl : temp) {
            if (sl != null && sl != deleted ) {
                this.insert(sl);
            }
        }
        System.out.println(
                "\nPrimary storage area was expanded. The array size is now " + N + "\n");
        return true;
    }
    
    private boolean clearDummyNodes(){
        
        StudentListing[] temp = sListings;
        sListings = new StudentListing[N];
        if (sListings == null) {
            return false;
        }
        n = 0;
        nd = 0;
        System.out.println("\nClearing Array of V2 nodes, re-inserting nodes into array\n");
        for ( StudentListing sl : temp ){
            if ( sl != null && sl != deleted ) {
                this.insert(sl);
            }
        }
        System.out.println("\nClearing V2 nodes done\n");
        return true;
    }

    public boolean insert(StudentListing newNode) {
        int pk = stringToInt(newNode.getKey());
        boolean hit = false, noError = true;
        int pass = 0, offset = 0;                
        // get loading factor after the node insertion
        if ( getCurrentLoadingFactor(1) < MAX_LOADING_FACTOR ) {
            int ip = pk % N;            // home
            int quotient = pk / N;      // offset
            offset = quotient;
            if (quotient % N == 0) {
                offset = DEFAULT_QUOTIENT;
            }
            while (pass < N) {
                if (sListings[ip] == null) {
                    hit = true;
                    System.out.println("Insert Overwrote null");
                    break;
                }else if(sListings[ip] == deleted){
                    hit = true;
                    nd--;
                    System.out.println("Insert Overwrote V2");
                    break;
                }
                ip = (ip + offset) % N;   //try next ip
                pass++;
            }
            if (hit) {
                sListings[ip] = newNode.deepCopy();
                n++;
                //let the expand method clear the V2 nodes if loadFactor == pseudoLoadFactor
                if ( getCurrentLoadingFactor() != getPseudoLoadingFactor() 
                        && getPseudoLoadingFactor() >= MAX_LOADING_FACTOR){
                    System.out.println("\nPseudo Loading Factor exceeded " + 
                            MAX_LOADING_FACTOR);
                    noError = clearDummyNodes();
                }
                System.out.println("Successfully Inserted " + newNode.getKey());
                showStats();
                return noError;
            } else {
                // Could not find element to insert at.
                return false;
            }
        } else {   //Structure is full, expand structure.
            System.out.println("Insert will cause loading factor to exceed " + 
                    MAX_LOADING_FACTOR);
            if (expand()) {
                System.out.println("Trying to insert " + newNode.getKey() + " again\n");
                return insert(newNode); //Successfully expanded and inserted
            }
            return false;               //Failed Expand
        }

    }

    public StudentListing fetch(String targetKey) {
        boolean hit = false;
        int pass, q, offset, ip;
        int pk = stringToInt(targetKey);  // preprocess the key
        pass = 0;
        q = pk / N;
        offset = q;
        ip = pk % N;
        if (q % N == 0) {
            offset = DEFAULT_QUOTIENT;
        }
        while (pass < N) {
            if (sListings[ip] == null || sListings[ip] == deleted) //node not in structure
            {
                break;
            }
            if (sListings[ip].compareTo(targetKey) == 0) //node found
            {
                hit = true;
                break;
            }
            ip = (ip + offset) % N;  //collision occurred
            pass = pass + 1;
        }
        if (hit == true) //return a deep copy of the node
        {
            System.out.println("\nSuccessfully Found " + targetKey);
            showStats();
            return sListings[ip].deepCopy();
        } else {
            System.out.println("\nCould not fetch " + targetKey + "\n");
            return null;
        }
    }//end of the Fetch method

    public boolean delete(String targetKey) {
        boolean noError;
        boolean hit = false;
        int pass, q, offset, ip;
        int pk = stringToInt(targetKey);  // preprocess the key
        pass = 0;
        q = pk / N;
        offset = q;
        ip = pk % N;
        if (q % N == 0) {
            offset = DEFAULT_QUOTIENT;
        }
        while (pass < N) {
            if (sListings[ip] == null)    //node not in structure
            {
                break;
            }
            if (sListings[ip].compareTo(targetKey) == 0) // node found
            {
                hit = true;
                break;
            }
            ip = (ip + offset) % N;                     //collision occurred
            pass = pass + 1;
        }
        if (hit == true) //delete the node
        {
            sListings[ip] = deleted;
            n--;
            nd++;
            System.out.println("Successfully Deleted " + targetKey);
            showStats();
            return noError = true;
        } else {
            System.out.println("Could not find " + targetKey);
            return noError = false;
        }
    }//end of the Delete method

    public boolean update(String targetKey, StudentListing newListing) {
        if (delete(targetKey) == false) {
            return false;
        } else if (insert(newListing) == false) {
            return false;
        }
        showStats();
        return true;
    }//end of the Update method

    private double getPseudoLoadingFactor( int... plus ) {
        if (plus.length > 0 && plus.length < 2) {
            return (double) (int) (((double) (n + nd + plus[0]) / N) * 10000) / 10000;            
        }
        return (double) (int) (((double) (n + nd) / N) * 10000) / 10000;
    }

    private double getCurrentLoadingFactor( int... plus ) {
        if (plus.length > 0 && plus.length < 2) {
            return (double) (int) (((double) (n + plus[0]) / N) * 10000) / 10000;                        
        }
        return (double) (int) (((double) n / N) * 10000) / 10000;
    }

    private int getSearchLength() {
        return (int) (1 / (1 - getCurrentLoadingFactor()));
    }

    private double getDensity() {
        return (double) (int) (1 / (1 + 4 / (NODE_WIDTH * ((double) n / N))) * 10000) / 10000;
    }

    private void showStats() {

        System.out.format("%17s%22s%22s%22s",
                "||Search Length||\t", "||Current Loading Factor||\t",
                "||Pseudo Loading Factor||\t", "||Current Density||\n");
        System.out.format("%10s%29s%30s%32s\n",
                getSearchLength(), getCurrentLoadingFactor(),
                getPseudoLoadingFactor(), getDensity());

    }

    public static int stringToInt(String aKey) // from Figure 5.18
    {
        int pseudoKey = 0;
        int n = 1;
        int cn = 0;
        char c[] = aKey.toCharArray();
        int grouping = 0;
        while (cn < aKey.length()) // there is still more character in the key
        {
            grouping = grouping << 8;      // pack next four characters in i
            grouping = grouping + c[cn];
            cn = cn + 1;
            if (n == 4 || cn == aKey.length()) // 4 characters are processed or no more characters
            {
                pseudoKey = pseudoKey + grouping;
                n = 0;
                grouping = 0;
            }
            n = n + 1;
        }
        return Math.abs(pseudoKey);
    }
}
