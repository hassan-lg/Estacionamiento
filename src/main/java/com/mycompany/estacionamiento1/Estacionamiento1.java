package com.mycompany.estacionamiento1;

/**
 *
 * @author Hassan López
 */
import java.util.Scanner;

public class Estacionamiento1 {
    static final int tamano = 10;
    static final String[][] mapa = new String[tamano][tamano];
    static final String[][] placas = new String[tamano][tamano];  

    
    public static void main(String[] args) {
    
    Scanner teclado = new Scanner(System.in);
    for(int i = 0; i < tamano; i++){
        for(int j = 0; j < tamano; j++){
            if(i == 0 || i  == tamano - 1){
                if(i == 0 && j ==3){
                    mapa[i][j] = "[E]";
                }else {
                    mapa[i][j] = "[=]";
                }
            }
            else if(j == 0 || j == tamano - 1){
                if(i == 6 && j == tamano - 1){
                    mapa[i][j] = "[S]";
                }else {
                    mapa[i][j] = "[=]";
                }    
            }
            else {
                mapa[i][j] = "[L]";    
            } 
        }
    }
    
    boolean continuarEjecucion = true;
    
    
    
    do{
        mostrarMenu();
        
        
        
        if(!teclado.hasNextInt()) {
            System.out.println("Se debe ingresar un número entre 1 y 7");
            teclado.next();
            continue;
        }
        
        int opcion = teclado.nextInt();
        
        switch(opcion){
            case 1:
                
                System.out.println("Ingrese un vehiculo");
                  
                break;
            case 2:
                System.out.println("Retire su vehiculo");
                break;
            case 3:
                System.out.println("Mostrar el estacionamiento");
                for(int i = 0; i < tamano; i++){
                    if (i > 0 && i < tamano - 1 ){
                        System.out.print(i + "_");
                    }else {
                        System.out.print("_");
                    }
                    
                    for(int j = 0; j < tamano; j++){
                        System.out.print(mapa[i][j] + "_");
                    }
                    System.out.println();
                }
                break;
            case 4:
                System.out.println("Buscar por placa");
                break;
            case 5:
                System.out.println("Cálculo de ruta más corta");
                break;
            case 6:
                System.out.println("Ingresos");
                break;
            case 7:
                System.out.println("Salir del sistema");
                continuarEjecucion = false;
                break;
            default:
                System.out.println("Debe escoger un número del 1 al 7");
        }
    } while (continuarEjecucion);
    teclado.close();
}
static void mostrarMenu() {
        System.out.println();
        System.out.println("===== SISTEMA DE ESTACIONAMIENTO =====");
        System.out.println("1. Ingresar vehiculo");
        System.out.println("2. Retirar vehiculo");
        System.out.println("3. Mostrar estacionamiento");
        System.out.println("4. Buscar vehiculo por placa");
        System.out.println("5. Mostrar ruta mas corta entre entrada y salida");
        System.out.println("6. Mostrar ingresos");
        System.out.println("7. Salir");
        System.out.print("Seleccione una opcion: ");
}
}


