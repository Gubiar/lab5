package com.br.lab5;

/*
 * Alunos: Gustavo Lázaro e Yuri Getaruck
 */

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMensagem extends Remote {
    
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;
    
}
