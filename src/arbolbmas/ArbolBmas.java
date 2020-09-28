/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolbmas;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Diego
 */
public class ArbolBmas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner entrada= new Scanner(System.in);
         BPlusTree tree =  new BPlusTree(3);
         String nombre;
         int key=0;
         int op;
         boolean salir=false;
         while (salir==false) {            
             System.out.println("1)ingresar");
             System.out.println("2)ver"); 
             System.out.println("3)salir");
             System.out.print("eliga una opcion");
             op=Integer.parseInt(entrada.nextLine());
             switch(op){
                 case 1:
                     System.out.println("ingrese un nombre");
                     nombre=entrada.nextLine();
                     tree.insert(key, nombre);
                     key++;
                     break;
                 case 2:
                     ArrayList<String> aux= tree.search(0, key);
                     for (int i = 0; i <key; i++) {
                         System.out.println(aux.get(i));
                     }
                     break;
                  case 3:
                      salir=true;
                     break;
                 default:
             }
        }
    }
    
}
