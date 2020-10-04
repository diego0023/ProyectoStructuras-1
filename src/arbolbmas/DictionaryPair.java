/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolbmas;

/**
 *
 * @author Diego
 */
public class DictionaryPair  implements Comparable<DictionaryPair> {
      //cada slot tiene una clave y un nombre 
    int key;
    String value;

    //constructor
    public DictionaryPair(int key, String value) {
      this.key = key;
      this.value = value;
    }
    //se compara y si la clave es mayor regresa 1 si no -1(metodo burbuja)
    public int compareTo(DictionaryPair o) {
      if (key == o.key) {
        return 0;
      } else if (key > o.key) {
        return 1;
      } else {
        return -1;
      }
    }
    
}
