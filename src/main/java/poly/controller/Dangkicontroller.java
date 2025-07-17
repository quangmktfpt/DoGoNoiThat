/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.controller;

import poly.util.XDialog;

/**
 *
 * @author quang
 */
public interface Dangkicontroller {
           void open();
 void dangki();
 default void exit(){
 if(XDialog.confirm("Bạn muốn kết thúc?")){
 System.exit(0);}
 }
}
