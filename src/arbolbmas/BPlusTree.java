/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arbolbmas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
    ///pruebas

    public void eliminar(int key) {

        if (firstLeaf.rightSibling != null) {

            otro(key, getLevel(key, 1));
        } else {

            DictionaryPair[] dps = this.firstLeaf.dictionary;
            int index = binarySearch(dps, firstLeaf.numPairs, key);
            firstLeaf.delete(index);
            sortDictionary(dps);

        }
    }

    public void otro(int key, int nivel) {
        LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);
        ///ELIMINAR CASO 1 ES HOJA----------------------------------------------------------------------------------
        if (isHoja(ln, key) == true) {
            DictionaryPair[] dps = ln.dictionary;
            int index = binarySearch(dps, ln.numPairs, key);
            /////////////////////////////////CASO1(1)el nodo tiene mas del minNumPairs
            if (ln.numPairs > (ln.minNumPairs) + 1) {
                System.out.println("ENTRO CASO 1_1");
                ln.delete(index);
                sortDictionary(dps);
            } //CASO 1(2)
            else if (ln.numPairs == (ln.minNumPairs + 1)) {

                ///CASO 1(2) ALGUNOS DE LOS HERMANOS DEL NODO LE PUEDE PRESTAR
                if ((ln.rightSibling.isLendable2() == true) && (ln.leftSibling.isLendable2() == true)) {
                    System.out.println("ENTRO CASO 1_2");
                    //primero eliminar llave
                    ln.delete(index);
                    sortDictionary(dps);
                    //ver si tiene hermano derecho o izquierdo
                    if ((ln.rightSibling != null) && (ln.parent == ln.rightSibling.parent)) {
                        susSimple(ln.rightSibling, ln);
                    } else {
                        susSimple(ln.rightSibling, ln);
                    }
                    System.out.println("Eliminado, con sustitucion");
                } else {
                    //CASO 1(2) NINGUN HERMANO PUEDE PRESTAR HAY QUE ELIMINAR EL NODO
                    System.out.println("ENTRO CASO 1_3");
                    //eliminar conecciones del LEAFNODE
                    LeafNode hermanoD = ln.rightSibling;
                    LeafNode hermanoI = ln.leftSibling;
                    if (hermanoI != null) {
                        hermanoI.rightSibling = hermanoD;
                    }// el hermano derecho del hermano izquierdo ahora es el hermano derecho del eliminado
                    if (hermanoD != null) {
                        hermanoD.leftSibling = hermanoI;
                    }// el hermano izquierdo  del hermano derecho del elminado ahora es ell hermano izquierdo
                    //eliminar pointers de parent
                    InternalNode parent1 = ln.parent;
                    int puntero = findIndexOfPointer(parent1.childPointers, ln);//puntero del nodo
                    if (ln == firstLeaf) {
                        firstLeaf = hermanoD;
                    }
                    parent1.removePointer1(puntero);
                }

            }

        } //CASO 2 no es hoja--------------------------------------------------------------------------------------------------------
        else if ((isHoja(ln, key) == false) && (ln.numPairs >= ln.minNumPairs + 1)) {
            System.out.println("entro a caso dos ");
            //CASO 2.1 El nodo tiene mas del minimo y no se encuntra en la raiz
            if ((ln.numPairs > ln.minNumPairs + 1) && (nivel > 1)) {
                System.out.println("entro a caso dos .1");
                System.out.println("keys: " + ln.numPairs);
                //eliminar llave
                DictionaryPair[] dps = ln.dictionary;
                int index = binarySearch(dps, ln.numPairs, key);
                ln.delete(index);
                sortDictionary(dps);
                //sustituir key que tambien esta en internal node por la siguiente del eliminar en LeafNode
                InternalNode parent2 = ln.parent;
                parent2.sustituirKey(key, dps[0].key);

            }//CASO 2.2  tiene exactamente el minimo de claves y no se encuetra en la raiz
            else if ((ln.numPairs == (ln.minNumPairs) + 1) && (nivel != 1)) {
                System.out.println("entro a caso dos .2");
                //mandar dicionario
                DictionaryPair[] dps = ln.dictionary;
                //si ninguno de los hermano puede prestar
                if ((ln.rightSibling.isLendable2() == false) && (ln.leftSibling.isLendable2() == false)) {
                    //eliminar3(ln, key, dps);
                    System.out.println("todavia no existe ese metodo");
                } else {
                    eliminar2_2(ln, key, dps);
                }
            }//CASO 2.3 el nodo tiene mas o igual claves al minimo de claves y si se encuentra en la raiz
            else if ((ln.numPairs >= ln.minNumPairs + 1) && (nivel == 1)) {
                DictionaryPair[] dps = ln.dictionary;
                System.out.println("entro a caso dos .3");
                eliminar2_3(ln, key, dps);
            }

        } else if ((isHoja(ln, key) == false) && (ln.numPairs < ln.minNumPairs + 1)) {
            System.out.println("todavia no existe ese metodo");
        }
        ////////////////////////////////////////

    }

    public void eliminar3(LeafNode ln, int key, DictionaryPair[] dpsLn) {
        //eliminar leafNode
        if (ln != firstLeaf) {
            InternalNode parent1 = ln.parent;
            LeafNode hermanoL = ln.leftSibling;
            DictionaryPair[] dps1 = hermanoL.dictionary;
            //eliminar nodo
            int index = binarySearch(dpsLn, ln.numPairs, key);
            ln.delete(index);
            sortDictionary(dpsLn);
            //eliminar pointers de la raiz
            int puntero = root.findIndexOfPointer(parent1);//puntero de la raiz al padre
            root.removePointer(parent1);
            //
            InternalNode nuevaRaiz = hermanoL.leftSibling.parent;
            root = nuevaRaiz;
            root.keys[1] = dps1[0].key;

        } else {
            firstLeaf = ln.rightSibling;
            LeafNode hermanoD = ln.rightSibling;
            LeafNode hermanoI = ln.leftSibling;
            if (hermanoI != null) {
                hermanoI.rightSibling = hermanoD;
            }// el hermano derecho del hermano izquierdo ahora es el hermano derecho del eliminado
            if (hermanoD != null) {
                hermanoD.leftSibling = hermanoI;
            }// el hermano izquierdo  del hermano derecho del elminado ahora es ell hermano izquierdo
        }
    }

    public void eliminar2_3(LeafNode ln, int key, DictionaryPair[] dpsLn) {
        ////// (1)  Cuando el nodo tiene mas que minNumPairs y tambien se encuentra en la raiz
        if (ln.numPairs > ln.minNumPairs + 1) {
            System.out.println("ELIMINAR SI ESTA EN RAIZ Y TIENE MAS DEL MINIMO DE LLAVES");
            //eliminar llave
            DictionaryPair[] dps = ln.dictionary;
            int index = binarySearch(dps, ln.numPairs, key);
            ln.delete(index);
            sortDictionary(dps);
            //sustituir key que se quiere elminar de root por el que quedo en el primer lugar de LeafNode
            InternalNode raiz = this.root;
            raiz.sustituirKey(key, dps[0].key);
        }//
        ////(2) cuando el que se quiere eliminar tiene el minimo de claves
        //// y al que le debe prestar(el hermano) tiene MAS del minimo de claves
        else if ((ln.numPairs == ln.minNumPairs + 1) && (ln.rightSibling.numPairs > ln.rightSibling.minNumPairs + 1)) {
            System.out.println("ELIMINAR SI ESTA EN RAIZ Y TIENE EL MINIMO DE LLAVES Y EL HERMANO PUEDE PRESTAR");
            LeafNode hermanoD = ln.rightSibling;//guarda el hermnao derecho
            DictionaryPair[] dpsH = hermanoD.dictionary;//guarda el diccionario del hermano
            //key que se quiere eliminar se convierte en el primero del hermano derecho
            dpsLn[0] = dpsH[0];
            //sustituir key que se quiere elminar de root por el que quedo en el primer lugar de LeafNode que quedo
            InternalNode raiz = this.root;
            raiz.sustituirKey(key, dpsLn[0].key);
            //sustituir key que quedo en el internal node por segunda key del hermano
            InternalNode parent1 = ln.parent;
            parent1.sustituirKey(dpsH[0].key, dpsH[1].key);
            //finalmente se elimna el key prestado del hermano
            int index = binarySearch(dpsH, hermanoD.numPairs, dpsH[0].key);
            hermanoD.delete(index);
            sortDictionary(dpsH);

        } ////(3)cuando el nodo tiene el minimo numero de claves y el hermano tambien tiene el numero mínimo de claves
        else if ((ln.numPairs == ln.minNumPairs + 1) && (ln.rightSibling.numPairs == ln.rightSibling.minNumPairs + 1)) {
            System.out.println("ELIMINAR SI ESTA EN RAIZ Y TIENE EL MINIMO DE LLAVES Y EL HERMANO NOO PUEDE PRESTAR");
            LeafNode hermanoD = ln.rightSibling;//guarda el hermnao derecho
            DictionaryPair[] dpsH = hermanoD.dictionary;//guarda el diccionario del hermano
            //key que se quiere eliminar se convierte en el primero del hermano derecho
            dpsLn[0] = dpsH[0];
            //PRIMERA KEY DEL PARENT SE PASA A ROOT
            InternalNode parent1 = ln.parent;//guarda parent
            this.root.sustituirKey(key, parent1.keys[0]);//sustituye key a eliminar por primera key del internalNode
            //eliminar primera key del parent
            parent1.removeKey(0);
            //eliminar CONEXION nodo hermanno (se supone que ya no tiene nada)
            if (hermanoD.rightSibling != null) {
                ln.rightSibling = hermanoD.rightSibling; //ahora el hermano derecho del nodo es el hermano derecho del eliminado
                hermanoD.leftSibling = ln;//
            }
            //eliminar child pointer
            parent1.removePointer(0);//elimina el pointer del hermano del que se elimino 
        }

    }

    public void eliminar2_2(LeafNode ln, int key, DictionaryPair[] dpsLn) {
        //este elimaniar se utiliza cuando la llave a eliminar tiene nodo interno 
        //y el numPairs == minNumPairs
        LeafNode aux1 = ln.leftSibling;
        if ((aux1.numPairs > aux1.minNumPairs + 1) && (aux1.parent == ln.parent)) {// si el hermano izquierdo puede prestar y vienen del mismo padre
            System.out.println("presto izquierdo");
            DictionaryPair[] dpsHermano = aux1.dictionary;//guarda diccionario de hermano
            InternalNode parent2 = ln.parent;//guarda padre
            parent2.sustituirKey(key, dpsHermano[aux1.numPairs - 1].key);//sustituye en el padre la llave que se quiere borrar por el ultimo key del hermano
            //
            dpsLn[0] = dpsHermano[aux1.numPairs - 1];//el lugar que quedo donde se elimino el leafNode aora es igual al ultimo del hermano ozquierdo
            //Ahora se debe borrar el ultimo key del nodo del  que se presto es decir el hermano izquierdo
            int indexH = binarySearch(dpsHermano, aux1.numPairs, dpsHermano[aux1.numPairs - 1].key);//guarda el index del key para borrar
            aux1.delete(indexH);//borra key
            sortDictionary(dpsHermano);//ordena arreglo de nodos
        } else {
            //SI SE TIENE QUE PRESTAR DEL LADO DERECHO
            aux1 = ln.rightSibling;
            if ((aux1.numPairs > aux1.minNumPairs + 1) && (aux1.parent == ln.parent)) {
                System.out.println("presto derecho");
                DictionaryPair[] dpsHermanoD = aux1.dictionary;//guarda diccionario de hermano
                InternalNode parent2 = aux1.parent;//guarda padre
                System.out.println(key + " el prestado: " + dpsHermanoD[0].key);
                dpsLn[0] = dpsHermanoD[0];////el lugar que quedo donde se elimino el leafNode ahora es igual al primero del hermano derecho
                //ahora hay que sustituir el que quedo en la posicion 0 al internal node por el siguiente key
                parent2.sustituirKey(dpsHermanoD[0].key, dpsHermanoD[1].key);
                parent2.sustituirKey(key, dpsHermanoD[0].key);//sustituye en el padre la llave que se quiere borrar por el primer key del hermano
                //
                //elimino el hermano que preste
                int indexH = binarySearch(dpsHermanoD, aux1.numPairs, dpsHermanoD[0].key);//guarda el index del key para borrar
                aux1.delete(indexH);//borra key
                sortDictionary(dpsHermanoD);//ordena arreglo de nodos

            }

        }

    }

    public void susSimple(LeafNode hermano, LeafNode aux) {
        //jalar primera llave del hermano 
        DictionaryPair[] dps1 = hermano.dictionary;
        //guardar en el nodo que no hay nada la primmera llave del hermano
        aux.insert(dps1[0]);
        //encontrar la mediana del hermano
        int mediana = mediana(dps1);
        //sustituir llave de parent(internalNode) por la mediana
        InternalNode parent1 = aux.parent; //guardar parent del nodo
        parent1.sustituirKey(dps1[0].key, dps1[mediana].key);//dps[0].key porque la primera del nodo siempre va a ser la que apunta
        //eliminar llave robada del nodo hermano
        int key = dps1[0].key;
        int index = binarySearch(dps1, hermano.numPairs, key);
        hermano.delete(index);
        sortDictionary(dps1);
        //

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

    //MEDIANA
    public int mediana(DictionaryPair[] dictionary) {
        int mediana;
        int mitad = ((dictionary.length - 1) / 2);

//        if (dictionary.length % 2 == 0) {
//            mediana = (arreglo[mitad - 1] + arreglo[mitad]) / 2;
//        } else {
//            mediana = arreglo[mitad];
//        }
        return mitad;
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
        } else if (in.leftSibling != null && in.leftSibling.isLendable()) {
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
        if (isEmpty()) {//si el arbol esta vacio
            //creo un nuevo nodo hoja con el grado del arbol y un nuevo slot
            LeafNode ln = new LeafNode(this.m, new DictionaryPair(key, value));
            //se iguala el nuevo nodo al primer nodo hoja
            this.firstLeaf = ln;

        } else {
            //si no esta vacio 
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

    public int searchName(String value) {
        LeafNode currNode = this.firstLeaf;
        while (currNode != null) {
            DictionaryPair dps[] = currNode.dictionary;
            for (int i = 0; i < dps.length - 1; i++) {
                if (value.equals(dps[i].value)) {
                    return dps[i].key;
                }
            }
            currNode = currNode.leftSibling;

        }
        return -1;
    }

    public void print() {
        LeafNode currNode = this.firstLeaf;
        while (currNode != null) {
            DictionaryPair dps[] = currNode.dictionary;
            for (int i = 0; i < currNode.numPairs; i++) {
                System.out.println("clave: " + dps[i].key + " nombre: " + dps[i].value);
            }
            currNode = currNode.rightSibling;
        }
    }

    public void printTree() {
        if (root == null) {//el arbol solo tiene un nivel
            System.out.println(pritLeaf(this.firstLeaf));

        } else {
            System.out.println(pritInternal(root));
            Node childNode = root.childPointers[0];
            if (childNode instanceof LeafNode) {
                System.out.println(printLeaf((LeafNode) childNode));
            } else {
                //el hijo es interno
                reTree(childNode);

            }

        }

    }

    private void reTree(Node x) {

        if (x instanceof LeafNode) {
            System.out.println(printLeaf((LeafNode) x));
        } else {
            //el hijo es interno
            Node aux = printInternal((InternalNode) x);

            reTree(aux);

        }
    }

    private String printLeaf(LeafNode sheet) {
        String cadena = "";
        LeafNode currNode = sheet;
        while (currNode != null) {
            cadena = cadena + pritLeaf(currNode);
            currNode = currNode.rightSibling;
        }
        return cadena;
    }

    private String pritLeaf(LeafNode sheet) {
        String cadena = "[";
        DictionaryPair dps[] = sheet.dictionary;
        for (int i = 0; i < sheet.numPairs; i++) {
            cadena = cadena + dps[i].key + "|" + dps[i].value + ",";
        }

        return cadena = cadena + "]";
    }

    private Node printInternal(InternalNode internal) {
        String cadena = "";
        InternalNode currNode = internal;
        while (currNode != null) {
            cadena = cadena + pritInternal(currNode);
            currNode = currNode.rightSibling;
        }

        Node childNode = internal.childPointers[0];
        System.out.println(cadena);
        return childNode;

    }

    private String pritInternal(InternalNode internal) {
        String cadena = "[";
        Integer[] key = internal.keys;
        for (int i = 0; i < internal.degree; i++) {
            cadena = cadena + key[i] + ",";
        }
        return cadena = cadena + "]";
    }

    public int height() {
        int altura = 1;
        if (root == null) {
            if (firstLeaf == null) {
                System.out.println(" EL ARBOL NO TIENE NADA");
                return 0;
            } else {
                return altura;
            }
        } else {
            Node aux = this.firstLeaf.parent;
            while (aux != null) {
                altura++;
                aux = aux.parent;
            }

            return altura;

        }

    }

    public void leve(int key) {
        System.out.println("el nivel de: " + key + " es: " + getLevel(key, 1));

    }

    public int getLevel(int key, int lv) {
        //buscar si se encuentra en la raiz
        Integer[] keys = this.root.keys;
        int i;

        for (i = 0; i < this.root.degree - 1; i++) {
            if (key == keys[i]) {
                return lv;
            } else if (key < keys[i]) {
                break;//si en la raiz hay una clave mayor se rompe el ciclo 
            }
        }
        Node child = this.root.childPointers[i];
        if (child instanceof LeafNode) {
            return lv + 1;
        } else {
            return getLevel((InternalNode) child, key, (lv + 1));
        }

    }

    private int getLevel(InternalNode node, int key, int lv) {
        Integer[] keys = node.keys;
        int i;
        for (i = 0; i < node.degree - 1; i++) {
            if (key == keys[i]) {
                return lv;
            } else if (key < keys[i]) {
                break;//si en la raiz hay una clave mayor se rompe el ciclo 
            }

        }
        Node childNode = node.childPointers[i];
        if (childNode instanceof LeafNode) {
            return lv + 1;
        } else {
            return getLevel((InternalNode) node.childPointers[i], key, (lv + 1));
        }

    }

    public BPlusTree(int m) {
        this.m = m;
        this.root = null;
    }

    public boolean isHoja(LeafNode aux, int key) {
        int altura1 = height();
        int nivel1 = getLevel(key, 1);
        if (nivel1 == altura1) {
            //System.out.println("ES HOJA");
            return true;
        } else {
            //System.out.println("NO ES HOJA");
            return false;
        }
    }
}
