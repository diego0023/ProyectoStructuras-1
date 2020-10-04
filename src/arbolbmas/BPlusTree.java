/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolbmas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Diego
 */
public class BPlusTree {
      int m;//grado del arbol
  InternalNode root; //raiz
  LeafNode firstLeaf;//primera hoja
  
  // busqueda binaria resive un array, numero de parejas y otro numero
  private int binarySearch(DictionaryPair[] dps, int numPairs, int t) {
    Comparator<DictionaryPair> c = new Comparator<DictionaryPair>() {
      @Override
      public int compare(DictionaryPair o1, DictionaryPair o2) {
        Integer a = Integer.valueOf(o1.key);
        Integer b = Integer.valueOf(o2.key);
        return a.compareTo(b);
      }
    };
    return Arrays.binarySearch(dps, 0, numPairs, new DictionaryPair(t, ""), c);
  }
  
    // busca un nodo hoja
  private LeafNode findLeafNode(int key) {

    Integer[] keys = this.root.keys;
    int i;

    for (i = 0; i < this.root.degree - 1; i++) {
      if (key < keys[i]) {
        break;
      }
    }

    Node child = this.root.childPointers[i];
    if (child instanceof LeafNode) {
      return (LeafNode) child;
    } else {
      return findLeafNode((InternalNode) child, key);
    }
  }
  
  // Find the leaf node
  private LeafNode findLeafNode(InternalNode node, int key) {

    Integer[] keys = node.keys;
    int i;

    for (i = 0; i < node.degree - 1; i++) {
      if (key < keys[i]) {
        break;
      }
    }
    Node childNode = node.childPointers[i];
    if (childNode instanceof LeafNode) {
      return (LeafNode) childNode;
    } else {
      return findLeafNode((InternalNode) node.childPointers[i], key);
    }
  }
  
  // Finding the index of the pointer
  private int findIndexOfPointer(Node[] pointers, LeafNode node) {
    int i;
    for (i = 0; i < pointers.length; i++) {
      if (pointers[i] == node) {
        break;
      }
    }
    return i;
  }
  
  // Get the mid point
  private int getMidpoint() {
    return (int) Math.ceil((this.m + 1) / 2.0) - 1;
  }
  
  // Balance the tree
  private void handleDeficiency(InternalNode in) {

    InternalNode sibling;
    InternalNode parent = in.parent;

    if (this.root == in) {
      for (int i = 0; i < in.childPointers.length; i++) {
        if (in.childPointers[i] != null) {
          if (in.childPointers[i] instanceof InternalNode) {
            this.root = (InternalNode) in.childPointers[i];
            this.root.parent = null;
          } else if (in.childPointers[i] instanceof LeafNode) {
            this.root = null;
          }
        }
      }
    }

    else if (in.leftSibling != null && in.leftSibling.isLendable()) {
      sibling = in.leftSibling;
    } else if (in.rightSibling != null && in.rightSibling.isLendable()) {
      sibling = in.rightSibling;

      int borrowedKey = sibling.keys[0];
      Node pointer = sibling.childPointers[0];

      in.keys[in.degree - 1] = parent.keys[0];
      in.childPointers[in.degree] = pointer;

      parent.keys[0] = borrowedKey;

      sibling.removePointer(0);
      Arrays.sort(sibling.keys);
      sibling.removePointer(0);
      shiftDown(in.childPointers, 1);
    } else if (in.leftSibling != null && in.leftSibling.isMergeable()) {

    } else if (in.rightSibling != null && in.rightSibling.isMergeable()) {
      sibling = in.rightSibling;
      sibling.keys[sibling.degree - 1] = parent.keys[parent.degree - 2];
      Arrays.sort(sibling.keys, 0, sibling.degree);
      parent.keys[parent.degree - 2] = null;

      for (int i = 0; i < in.childPointers.length; i++) {
        if (in.childPointers[i] != null) {
          sibling.prependChildPointer(in.childPointers[i]);
          in.childPointers[i].parent = sibling;
          in.removePointer(i);
        }
      }

      parent.removePointer(in);

      sibling.leftSibling = in.leftSibling;
    }

    if (parent != null && parent.isDeficient()) {
      handleDeficiency(parent);
    }
  }
    private boolean isEmpty() {
    return firstLeaf == null;
  }
    private int linearNullSearch(DictionaryPair[] dps) {
    for (int i = 0; i < dps.length; i++) {
      if (dps[i] == null) {
        return i;
      }
    }
    return -1;
  }
   private int linearNullSearch(Node[] pointers) {
    for (int i = 0; i < pointers.length; i++) {
      if (pointers[i] == null) {
        return i;
      }
    }
    return -1;
  }
     private void shiftDown(Node[] pointers, int amount) {
    Node[] newPointers = new Node[this.m + 1];
    for (int i = amount; i < pointers.length; i++) {
      newPointers[i - amount] = pointers[i];
    }
    pointers = newPointers;
  }
  private void sortDictionary(DictionaryPair[] dictionary) {
    Arrays.sort(dictionary, new Comparator<DictionaryPair>() {
      @Override
      public int compare(DictionaryPair o1, DictionaryPair o2) {
        if (o1 == null && o2 == null) {
          return 0;
        }
        if (o1 == null) {
          return 1;
        }
        if (o2 == null) {
          return -1;
        }
        return o1.compareTo(o2);
      }
    });
  }
  
   private Node[] splitChildPointers(InternalNode in, int split) {

    Node[] pointers = in.childPointers;
    Node[] halfPointers = new Node[this.m + 1];

    for (int i = split + 1; i < pointers.length; i++) {
      halfPointers[i - split - 1] = pointers[i];
      in.removePointer(i);
    }

    return halfPointers;
  }
  private DictionaryPair[] splitDictionary(LeafNode ln, int split) {

    DictionaryPair[] dictionary = ln.dictionary;

    DictionaryPair[] halfDict = new DictionaryPair[this.m];

    for (int i = split; i < dictionary.length; i++) {
      halfDict[i - split] = dictionary[i];
      ln.delete(i);
    }

    return halfDict;
  }
  
  private void splitInternalNode(InternalNode in) {

    InternalNode parent = in.parent;

    int midpoint = getMidpoint();
    int newParentKey = in.keys[midpoint];
    Integer[] halfKeys = splitKeys(in.keys, midpoint);
    Node[] halfPointers = splitChildPointers(in, midpoint);

    in.degree = linearNullSearch(in.childPointers);

    InternalNode sibling = new InternalNode(this.m, halfKeys, halfPointers);
    for (Node pointer : halfPointers) {
      if (pointer != null) {
        pointer.parent = sibling;
      }
    }

    sibling.rightSibling = in.rightSibling;
    if (sibling.rightSibling != null) {
      sibling.rightSibling.leftSibling = sibling;
    }
    in.rightSibling = sibling;
    sibling.leftSibling = in;

    if (parent == null) {

      Integer[] keys = new Integer[this.m];
      keys[0] = newParentKey;
      InternalNode newRoot = new InternalNode(this.m, keys);
      newRoot.appendChildPointer(in);
      newRoot.appendChildPointer(sibling);
      this.root = newRoot;

      in.parent = newRoot;
      sibling.parent = newRoot;

    } else {

      parent.keys[parent.degree - 1] = newParentKey;
      Arrays.sort(parent.keys, 0, parent.degree);

      int pointerIndex = parent.findIndexOfPointer(in) + 1;
      parent.insertChildPointer(sibling, pointerIndex);
      sibling.parent = parent;
    }
  }

  private Integer[] splitKeys(Integer[] keys, int split) {

    Integer[] halfKeys = new Integer[this.m];

    keys[split] = null;

    for (int i = split + 1; i < keys.length; i++) {
      halfKeys[i - split - 1] = keys[i];
      keys[i] = null;
    }

    return halfKeys;
  }
  
  //metodo insertar (resive una clave y el valor a insertar)
   public void insert(int key, String value) {
    if (isEmpty()) {//si el arbol esta bacio
        //creo un nuevo nodo hoja con el grado del arbol y un nuevo slot
      LeafNode ln = new LeafNode(this.m, new DictionaryPair(key, value));
      //se iguala el nuevo nodo al primer nodo hoja
      this.firstLeaf = ln;

    } else {
        //si no esta bacio 
      LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);

      if (!ln.insert(new DictionaryPair(key, value))) {
          //se va a necesitar dividir
        ln.dictionary[ln.numPairs] = new DictionaryPair(key, value);
        ln.numPairs++;
        sortDictionary(ln.dictionary);

        int midpoint = getMidpoint();
        DictionaryPair[] halfDict = splitDictionary(ln, midpoint);

        if (ln.parent == null) {

          Integer[] parent_keys = new Integer[this.m];
          parent_keys[0] = halfDict[0].key;
          InternalNode parent = new InternalNode(this.m, parent_keys);
          ln.parent = parent;
          parent.appendChildPointer(ln);

        } else {
          int newParentKey = halfDict[0].key;
          ln.parent.keys[ln.parent.degree - 1] = newParentKey;
          Arrays.sort(ln.parent.keys, 0, ln.parent.degree);
        }

        LeafNode newLeafNode = new LeafNode(this.m, halfDict, ln.parent);

        int pointerIndex = ln.parent.findIndexOfPointer(ln) + 1;
        ln.parent.insertChildPointer(newLeafNode, pointerIndex);

        newLeafNode.rightSibling = ln.rightSibling;
        if (newLeafNode.rightSibling != null) {
          newLeafNode.rightSibling.leftSibling = newLeafNode;
        }
        ln.rightSibling = newLeafNode;
        newLeafNode.leftSibling = ln;

        if (this.root == null) {

          this.root = ln.parent;

        } else {
          InternalNode in = ln.parent;
          while (in != null) {
            if (in.isOverfull()) {
              splitInternalNode(in);
            } else {
              break;
            }
            in = in.parent;
          }
        }
      }
    }
  }
   
    //buscar un nodo por clave
   public String search(int key) {

    if (isEmpty()) {
      return null;
    }

    LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);

    DictionaryPair[] dps = ln.dictionary;
    int index = binarySearch(dps, ln.numPairs, key);

    if (index < 0) {
      return null;
    } else {
      return dps[index].value;
    }
  }
    public ArrayList<String> search(int lowerBound, int upperBound) {

    ArrayList<String> values = new ArrayList<String>();

    LeafNode currNode = this.firstLeaf;
    while (currNode != null) {

      DictionaryPair dps[] = currNode.dictionary;
      for (DictionaryPair dp : dps) {

        if (dp == null) {
          break;
        }

        if (lowerBound <= dp.key && dp.key <= upperBound) {
          values.add(dp.value);
        }
      }
      currNode = currNode.rightSibling;

    }

    return values;
  }
    
    public void height(){
       int altura=1;
        if (root==null) {
            if (firstLeaf==null) {
                System.out.println(" EL ARBOL NO TIENE NADA");
            }else{
                System.out.println("LA ALTURA ES: " + altura);
            }
        }else{
              Node aux= this.firstLeaf.parent;
            while(aux!=null){
                altura++;
                aux=aux.parent;
            }
            
            System.out.println("LA ALTURA ES: " + altura);
            
           
        }
    
    
    }
    public void leve(int key){
        System.out.println("el nivel de: " + key+ " es: "+ getLevel(key,1));
    
    }
    
    private int getLevel(int key, int lv){
        //buscar si se encuentra en la raiz
        Integer[] keys =this.root.keys;
        int i;
        
        for ( i = 0; i < this.root.degree-1; i++) {
            if (key==keys[i]) {
                return lv;
            }else if (key<keys[i]) {
                break;//si en la raiz hay una clave mayor se rompe el ciclo 
            }
        }
       Node child = this.root.childPointers[i];
      if (child instanceof LeafNode) {
        return lv+1;
        } else {
      return getLevel((InternalNode) child, key, (lv+1));
     }  
    
    }
    
    private int getLevel(InternalNode node, int key,int lv){
       Integer[] keys = node.keys;
        int i;
         for (i = 0; i < node.degree - 1; i++) {
              if (key==keys[i]) {
                return lv;
            }else if (key<keys[i]) {
                break;//si en la raiz hay una clave mayor se rompe el ciclo 
            }
          
        }
         Node childNode = node.childPointers[i];
         if (childNode instanceof LeafNode) {
        return lv+1;
        } else {
      return getLevel((InternalNode) node.childPointers[i], key,(lv+1));
     }
         
         
     }
        
    

    public BPlusTree(int m) {
    this.m = m;
    this.root = null;
  }
}
