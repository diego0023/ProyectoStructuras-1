
package arbolbmas;

import java.util.ArrayList;
import java.util.Scanner;

public class ArbolBmas {

    /**
     * @param args the command line arguments
     */
    public static void menu() {
        System.out.println("M  E  N  U");
        System.out.println("1)INGRESAR");
        System.out.println("2)IMPRIMIR");
        System.out.println("3)ALTURA ");
        System.out.println("4)NIVEL");
        System.out.println("5)BUSCAR CLAVE");
        System.out.println("6)BUSCAR NOMBRE");
        System.out.println("7)ELIMINAR");
        System.out.println("8)SALIR");
        System.out.print("eliga una opcion");

    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        String nombre;
        int clave, orden;
        int key = 0;
        int op;
        //secuencia de inicio
        System.out.println("ingrese el orden del arbol B+");
        orden = Integer.parseInt(entrada.nextLine());
        BPlusTree tree = new BPlusTree(orden);
        boolean salir = false;
        while (salir == false) {
            menu();
            op = Integer.parseInt(entrada.nextLine());
            switch (op) {
                case 1:
                    System.out.println("ingrese un nombre");
                    nombre = entrada.nextLine();
                    System.out.println("ingrese la clave");
                    key = Integer.parseInt(entrada.nextLine());
                    tree.insert(key, nombre);

                    break;
                case 2:
                    System.out.println("1) solo hojas");
                    System.out.println("2) todo el arbol");
                    op = Integer.parseInt(entrada.nextLine());
                    if (op == 1) {
                        tree.print();
                    } else if (op == 2) {
                        tree.printTree();
                    } else {
                        System.out.println("opcion no valida");
                    }
                    break;
                case 3:

                    System.out.println("LA ALTURA ES: " + tree.height());
                    break;
                case 4:
                    System.out.println("ingrese la clave a buscar");
                    clave = Integer.parseInt(entrada.nextLine());
                    tree.leve(clave);
                    break;
                case 5:
                    System.out.println("Ingreese la clave a buscar");
                    clave = Integer.parseInt(entrada.nextLine());
                    if (tree.search(clave) == null) {
                        System.out.println("no existe");
                    } else {
                        System.out.println("si existe, para " + clave + " nombre " + tree.search(clave));
                    }
                    break;
                case 6:
                    System.out.println("Ingreese el nombre a buscar");
                    nombre = entrada.nextLine();
                    int validar = tree.searchName(nombre);
                    if (validar == -1) {
                        System.out.println("no existe");
                    } else {
                        System.out.println("si existe, para " + nombre + " clave " + validar);
                    }
                    break;
                case 7:
                    System.out.println("Ingreese la clave a eliminar");
                    clave = Integer.parseInt(entrada.nextLine());
                    tree.eliminar(clave);

                    break;
                case 8:
                    salir = true;
                    break;
                default:
            }
        }
    }

}
