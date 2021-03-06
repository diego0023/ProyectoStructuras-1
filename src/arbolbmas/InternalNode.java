
package arbolbmas;

import java.util.Arrays;

public class InternalNode extends Node {

    //parecido a un nodo hoja pero el nodo interno tiene hijos
    int maxDegree;
    int minDegree;
    int degree;
    int cont = -1;//contador de keys
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
//
    
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
        contK(this.keys);
//        Arrays.sort(this.keys);
//        System.out.println(Arrays.toString(this.keys));
//        this.keys = removeNulls(this.keys);
//        recorrer();

    }

    private Node[] removeNulls(Node[] strs) {
        int contNulls = 0;
        for (int i = 0; i < this.childPointers.length; i++) {
            if (this.childPointers[i] == null) {
                contNulls++;
            }
        }
        Node[] nullsRemoved = new Node[childPointers.length - contNulls];

        for (int i = 0, j = 0; i < childPointers.length; i++) {

            if (childPointers[i] != null) {
                nullsRemoved[j] = childPointers[i];
                j++;
            }
        }
        return nullsRemoved;
    }

    public void sustituirKey(int key, int key2) {
        contK(this.keys);
        int index = -1;
        for (int i = 0; i <= cont; i++) {
            //System.out.println("key parent: "+this.keys[i]);
            if (this.keys[i] == key) {
                this.keys[i] = key2;
                break;
            }
        }

    }

    public void recorrer() {
        for (int i = 0; i < this.keys.length; i++) {
            System.out.println("key parent: " + this.keys[i]);

        }
    }

    //borra el puntero de un hijo
    public void removePointer(int index) {
        this.childPointers[index] = null;
        this.degree--;
    }
//borrar nulls caso especial

    public void removePointer1(int index) {
        this.childPointers[index] = null;
        this.degree--;
        this.childPointers = removeNulls(this.childPointers);
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

    private void contK(Integer[] k) {
        cont = 0;

        for (int i = 0; i < k.length; i++) {
            if (k[i] != null) {
                cont++;
            }
        }

    }
}
