package br.univel.comum;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICliente extends Remote{

	public void receberMensagemPrivada(String remetente, String msg) throws RemoteException;
	
	public void receberMensagemPublica(String remetente, String msg) throws RemoteException;
	
	public void notificaEntrada(String nome) throws RemoteException;
	
	public void notificaSaida(String nome) throws RemoteException;
}
