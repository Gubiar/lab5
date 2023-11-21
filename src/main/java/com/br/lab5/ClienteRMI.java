package com.br.lab5;

/*
 * Alunos: Gustavo Lázaro e Yuri Getaruck
 */

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ClienteRMI {

    public void iniciarCliente() {

        List<PeerLista> listaPeers = new ArrayList<PeerLista>();
        for (PeerLista peer : PeerLista.values())
            listaPeers.add(peer);

        try {

            Registry registro = LocateRegistry.getRegistry("127.0.0.1", 1099);
            // Solicita ao usuário que escolha um peer para se conectar
            boolean conectou = false;
            while (!conectou) {
                Scanner leitura = new Scanner(System.in);
                System.out.println("Escolha um peer para se conectar:");
                int i = 1;
                for (PeerLista peer : PeerLista.values()) {
                    System.out.println(i + ". " + peer.getNome());
                    i++;
                }
                IMensagem stub = null;
                int indicePeer = leitura.nextInt();

                if (indicePeer < 1 || indicePeer > PeerLista.values().length) {
                    System.out.println("Índice inválido. Por favor, tente novamente.");
                } else {
                    PeerLista peerEscolhido = PeerLista.values()[indicePeer - 1];
                    try {
                        stub = (IMensagem) registro.lookup(peerEscolhido.getNome());
                        conectou = true;
                    } catch (java.rmi.ConnectException e) {
                        System.out.println("\n" + peerEscolhido.getNome() + " indisponível. ConnectException. Tentando o próximo...");
                    } catch (java.rmi.NotBoundException e) {
                        System.out.println("\n" + peerEscolhido.getNome() + " indisponível. NotBoundException. Tentando o próximo...");
                    }

                    if(conectou) {

                        System.out.println("Conectado no peer: " + peerEscolhido.getNome());

                        String opcao = "";
                        do {
                            System.out.println("1) Read");
                            System.out.println("2) Write");
                            System.out.println("x) Exit");
                            System.out.print(">> ");
                            opcao = leitura.next();
                            switch (opcao) {
                                case "1": {
                                    Mensagem mensagem = new Mensagem("", opcao);
                                    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'read'
                                    System.out.println(resposta.getMensagem());
                                    break;
                                }
                                case "2": {
                                    //Monta a mensagem
                                    System.out.print("Add fortune: ");
                                    String fortune = leitura.next();

                                    Mensagem mensagem = new Mensagem(fortune, opcao);
                                    Mensagem resposta = stub.enviar(mensagem); //dentro da mensagem tem o campo 'write'
                                    System.out.println(resposta.getMensagem());
                                    break;
                                }
                            }
                        } while (!opcao.equals("x"));

                        java.lang.System.exit(0);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("Iniciando cliente RMI");
        new ClienteRMI().iniciarCliente();

    }
}
