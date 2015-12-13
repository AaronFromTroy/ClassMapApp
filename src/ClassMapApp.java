/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author acous
 */
public class ClassMapApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("ClassMap App Starts Here!");
        System.out.println("Hello, WOrld!");

        DictParser dictionary = new DictParser();

        dictionary.searchForWord("acute");

        System.out.println(dictionary.getCount());

        System.out.println(dictionary.getIndex(0) + "\n");

        dictionary.printResults();
    }
    
}
