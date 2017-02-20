package br.univel.servidor;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import br.univel.comum.ICliente;
import br.univel.comum.IServidor;

public class TelaServidor extends JFrame implements IServidor{

	
	private Map<String, ICliente> mapClientes = new HashMap<>();
	private JTextArea jtxt;
	
	public TelaServidor(){
		this.setTitle("Servidor");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 600);
		
		
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		
		jtxt = new JTextArea();
		jp.add(jtxt, BorderLayout.CENTER);
		
		this.setContentPane(jp);
		
		this.setLocationRelativeTo(null);		
		
		try {
			inicializaRMI();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void inicializaRMI() throws RemoteException{
		System.setProperty("java.rmi.server.hostname", "127.0.0.1");
		
		IServidor servidor = (IServidor) UnicastRemoteObject.exportObject(this, 0);
		
		Registry registry = LocateRegistry.createRegistry(1818);
		
		registry.rebind(IServidor.SERVICO, servidor);
	}

	public static void main(String[] args) {
		TelaServidor ts = new TelaServidor();
		ts.setVisible(true);
	}
	
	
	@Override
	public void finalizar(String nome) throws RemoteException {
		
		synchronized (mapClientes) {
			mapClientes.values().forEach(e -> {
				try {
					e.notificaSaida(nome);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			});
			
			mapClientes.remove(nome);
			mostrar(nome + " se desconectou."); 
		}		
	}

	
	@Override
	public List<String> registrar(String nome, ICliente cliente) throws RemoteException {
		
		synchronized (mapClientes) {
			mapClientes.values().forEach(e -> {
				try {
					e.notificaEntrada(nome);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			});
			
			mapClientes.put(nome, cliente);
			mostrar(nome + " se conectou.");

			return new ArrayList<>(mapClientes.keySet());			
		}
	}

	private void mostrar(String string) {
		jtxt.append(string);
		jtxt.append("\n");
	}


	@Override
	public void enviarMensagemPublica(String remetente, String msg) throws RemoteException {
		
		synchronized (mapClientes) {
			mapClientes.values().forEach(e -> {
				try {
					e.receberMensagemPublica(remetente, msg);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			});		
			
			mostrar(remetente + " enviou mensagem para todos.");			
		}
	}

	@Override
	public void enviarMensagemPrivada(String remetente, String destinatario, String msg) throws RemoteException {
		synchronized (mapClientes) {

			mapClientes.get(destinatario).receberMensagemPrivada(remetente, msg);
			
			mostrar(remetente + " enviou mensagem para " + destinatario);
		}
		
	}

}
