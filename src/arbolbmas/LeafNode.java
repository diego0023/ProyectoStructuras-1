/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolbmas;

import java.util.Arrays;

/**
 *
 * @author Diego
 */
public class LeafNode extends Node{
    int maxNumPairs;//numero maximo
    int minNumPairs;//numero minimo
    int numPairs;//contador
    LeafNode leftSibling;//puntero de hermano derecho
    LeafNode rightSibling;//puntero de hermano izquierdo
    DictionaryPair[] dictionary;//claves

    //borra una clave por posicion
    public void delete(int index) {
      this.dictionary[index] = null;
      numPairs--;
    }
    //
    
    //inserta un valor
    public boolean insert(DictionaryPair dp) {
      if (this.isFull()) {//revisa no esta llena la hoja
        return false;
      } else {
        this.dictionary[numPairs] = dp;//inserta el valor en la ultima posicion disponible
        numPairs++;//se aumenta la posicion
        Arrays.sort(this.dictionary, 0, numPairs);//ordena el array (menor a mayor)

        return true;
      }
    }
    //esta por debajo del minimo
     public boolean isDeficient() {
      return numPairs < minNumPairs;
    }
     //esta lleno
    public boolean isFull() {
      return numPairs == maxNumPairs;
    }
    //se le puede prestar una clave
    public boolean isLendable() {
      return numPairs > minNumPairs;
    }
    //se puede usar para hacer una funcion
    public boolean isMergeable() {
      return numPairs == minNumPairs;
    }
    //constructor de una pagina
    public LeafNode(int m, DictionaryPair dp) {
      this.maxNumPairs = m - 1;//maximo numero de clavez
      this.minNumPairs = (int) (Math.ceil(m / 2) - 1);//minimo numero de claves
      this.dictionary = new DictionaryPair[m];//tamaño del array que es m
      this.numPairs = 0;//inicia el contador
      this.insert(dp);//se incerta un valor
    }
    //constructor para una hoja con padre
     public LeafNode(int m, DictionaryPair[] dps, InternalNode parent) {
      this.maxNumPairs = m - 1;
      this.minNumPairs = (int) (Math.ceil(m / 2) - 1);
      this.dictionary = dps;
      this.numPairs = linearNullSearch(dps);
      this.parent = parent;
    }
     //busqueda lineal de un valor
     private int linearNullSearch(DictionaryPair[] dps) {
    for (int i = 0; i < dps.length; i++) {
      if (dps[i] == null) {
        return i;
      }
    }
    return -1;
  }
}
