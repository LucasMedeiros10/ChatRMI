package br.univel.cliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import br.univel.comum.ICliente;
import br.univel.comum.IServidor;

public class TelaCliente implements ICliente{
	
	public static void main(String[] args) {
		Registry registry;
		
		try {
			registry = LocateRegistry.getRegistry("192.168.101.10", 1818);
		
			IServidor servidor = (IServidor)  registry.lookup(IServidor.SERVICO);
		
			TelaCliente tela = new TelaCliente();
			
			ICliente cliente = (ICliente) UnicastRemoteObject.exportObject(tela, 0);
			
			String nome = "Lucas Medeiros";
			
			servidor.registrar(nome, cliente);
			servidor.enviarMensagemPublica(nome, "Hello World!");
		
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void receberMensagemPrivada(String remetente, String msg) throws RemoteException {
		
	}

	@Override
	public void receberMensagemPublica(String remetente, String msg) throws RemoteException {
		
	}

	@Override
	public void notificaEntrada(String nome) throws RemoteException {
		
	}

	@Override
	public void notificaSaida(String nome) throws RemoteException {
		
	}

}
