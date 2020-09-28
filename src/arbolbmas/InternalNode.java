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
public class InternalNode  extends Node {
    //parecido a un nodo hoja pero el nodo interno tiene hijos
    int maxDegree;
    int minDegree;
    int degree;
    InternalNode leftSibling;
    InternalNode rightSibling;
    Integer[] keys;
    Node[] childPointers;
    
    //poner el apuntador a un hijo
    void appendChildPointer(Node pointer) {
      this.childPointers[degree] = pointer;
      this.degree++;
    }
    //busca el index de un hijo
     int findIndexOfPointer(Node pointer) {
      for (int i = 0; i < childPointers.length; i++) {
        if (childPointers[i] == pointer) {
          return i;
        }
      }
      return -1;
    }
     //se incerta un hijo nuevo
      void insertChildPointer(Node pointer, int index) {
      for (int i = degree - 1; i >= index; i--) {
        childPointers[i + 1] = childPointers[i];
      }
      this.childPointers[index] = pointer;
      this.degree++;
    }
      //el nodo tiene menos del minimo
       boolean isDeficient() {
      return this.degree < this.minDegree;
    }
       //el nodo puede prestar alguna clave
    boolean isLendable() {
      return this.degree > this.minDegree;
    }
    //se puede fucionar
    boolean isMergeable() {
      return this.degree == this.minDegree;
    }
    //esta mas que lleno
    boolean isOverfull() {
      return this.degree == maxDegree + 1;
    }
    
     void prependChildPointer(Node pointer) {
      for (int i = degree - 1; i >= 0; i--) {
        childPointers[i + 1] = childPointers[i];
      }
      this.childPointers[0] = pointer;
      this.degree++;
    }
     //borra una clave
     public void removeKey(int index) {
      this.keys[index] = null;
    }
     //borra el puntero de un hijo
    public void removePointer(int index) {
      this.childPointers[index] = null;
      this.degree--;
    }

    void removePointer(Node pointer) {
      for (int i = 0; i < childPointers.length; i++) {
        if (childPointers[i] == pointer) {
          this.childPointers[i] = null;
        }
      }
      this.degree--;
    }
    //constructor sin padre
    public InternalNode(int m, Integer[] keys) {
      this.maxDegree = m;
      this.minDegree = (int) Math.ceil(m / 2.0);
      this.degree = 0;
      this.keys = keys;
      this.childPointers = new Node[this.maxDegree + 1];
    }
    //constructor con hijos
    InternalNode(int m, Integer[] keys, Node[] pointers) {
      this.maxDegree = m;
      this.minDegree = (int) Math.ceil(m / 2.0);
      this.degree = linearNullSearch(pointers);//busca cantas claves tiene un nodo
      this.keys = keys;
      this.childPointers = pointers;
    }
    
    private int linearNullSearch(Node[] pointers) {
    for (int i = 0; i < pointers.length; i++) {
      if (pointers[i] == null) {
        return i;
      }
    }
    return -1;
  }
}
