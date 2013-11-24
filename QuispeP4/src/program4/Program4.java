package program4;

import javax.swing.JOptionPane;

public class Program4 {

    public static void main(String[] args) {

        LQHashed lqh = new LQHashed(1);
        menu( lqh );
        /*        
        lqh.insert(new StudentListing("a", "a", "num"));
        lqh.insert(new StudentListing("b", "a", "num"));
        lqh.insert(new StudentListing("c", "a", "num"));
        lqh.insert(new StudentListing("d", "a", "num"));
        lqh.insert(new StudentListing("e", "a", "num"));
        lqh.insert(new StudentListing("f", "a", "num"));
        lqh.insert(new StudentListing("g", "a", "num"));
        lqh.insert(new StudentListing("h", "a", "num"));
        lqh.delete("a");
        lqh.delete("b");
        lqh.delete("c");
        lqh.delete("d");
        lqh.delete("e");
        lqh.delete("f");
        lqh.delete("g");
        lqh.delete("h");
        lqh.insert(new StudentListing("456546d", "a", "num"));
        lqh.insert(new StudentListing("4565346", "a", "num"));
        lqh.insert(new StudentListing("4562f46", "a", "num"));
        lqh.insert(new StudentListing("45gfg346", "a", "num"));
        lqh.insert(new StudentListing("45gd6", "a", "num"));
        lqh.insert(new StudentListing("45zaz46", "a", "num"));
        lqh.insert(new StudentListing("45zef46", "a", "num"));
        lqh.insert(new StudentListing("45ztgz46", "a", "num"));
        System.out.println(lqh.fetch("4565426"));
        */
    }

    private static void menu( LQHashed lqh ) {
        String msg = "Menu\n"
                + "Enter 1 to Insert\n"
                + "Enter 2 to Fetch\n"
                + "Enter 3 to Delete\n"
                + "Enter 4 to Update\n"
                + "Enter 5 or more to Exit";
        int option;
        StudentListing listing;
        String key;
        do {
            option = Integer.parseInt(JOptionPane.showInputDialog(msg));
            switch (option) {
                case 1: //Insert
                    listing = new StudentListing();
                    listing.input();
                    if (lqh.insert(listing)){
                        System.out.println("Insert of " + listing.getKey() + " was succesful");
                    }else{
                        System.out.println("Insert of " + listing.getKey() + " failed");
                    }
                    break;
                case 2: //Fetch
                    key = JOptionPane.showInputDialog("Fetch Key: ");
                    lqh.fetch(key);
                    break;
                case 3: //Delete
                    key = JOptionPane.showInputDialog("Delete Key: ");
                    lqh.delete(key);
                    break;
                case 4: //Update
                    key = JOptionPane.showInputDialog("Update Key: ");
                    listing = new StudentListing();
                    listing.input();
                    if ( lqh.update(key, listing) ) 
                        System.out.println("\n  \n");
                    break;
                default:
                    break;
            }
        } while (option > 0 && option < 5);
    }
    /*
     private static boolean case1(String key, TreeNodeWrapper p, TreeNodeWrapper c){
     findNode(key, p,c );
     if (c == null) return false;
     if (p.lc == c ){
     p.lc = null;
     }
     else if ( p.rc == c ){
     p.rc = null;
     }
     return true;
        
     }
     */

}
