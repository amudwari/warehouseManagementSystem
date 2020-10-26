

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class ClientList implements Serializable
{
    private static final long serialVersionUID = 1L;
    public LinkedList<Client> clients= new LinkedList<>();
    private static ClientList clientList;

    private ClientList(){ }

    public static ClientList instance() {
        if(clientList == null) clientList = new ClientList();
        return clientList;
    }

    public boolean insertClient (Client client){
        return clients.add(client);
    }

    public Client getClient(String id) {
        Iterator<Client> it = clients.iterator();

        while(it.hasNext()) {
            Client client = it.next();

            if(client.getId().equals(id)) return client;
        }

        return null;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public boolean contains(String id)
    {
        Iterator<Client> clients = clientList.getClients();
        while (clients.hasNext())
        {
        	
            Client newclient = (Client)(clients.next());
            String checkId = newclient.getId();
            if (checkId.equals(id)) {
                return true;
            }
        }
        return false;
    }
    
    private void writeObject(ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(clientList);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	private void readObject(ObjectInputStream input) {
		try {
			if(clientList != null) return;
			else {
				input.defaultReadObject();
				if(clientList == null) clientList = (ClientList) input.readObject();
				else input.readObject();
			}
		}catch(IOException e) { e.printStackTrace(); }
		 catch(ClassNotFoundException e) { e.printStackTrace(); }
	}

    public String toString() {
        return clients.toString();
    }


}