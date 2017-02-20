package br.univel.comum;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServidor extends Remote{


	public final static String SERVICO = "ChatRmi"; 
	
	public void finalizar(String nome) throws RemoteException;
	
	public List<String> registrar(String nome, ICliente cliente) throws RemoteException;
	
	public void enviarMensagemPublica(String remetente, String msg) throws RemoteException;
	
	public void enviarMensagemPrivada(String remetente, String destinatario, String msg) throws RemoteException;
	
}
