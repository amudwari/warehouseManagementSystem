

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SupplierList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Supplier> suppliers = new LinkedList<Supplier>();
	private static SupplierList supplierList;
	
	private SupplierList() {}
	
	public static SupplierList instance() {
		if(supplierList == null) supplierList = new SupplierList();
		return supplierList;
	}
	
	public boolean insertSupplier(Supplier s) {
		return suppliers.add(s);
	}
	
	public Supplier getSupplier(String id) {
		Iterator<Supplier> it = suppliers.iterator();
		
		while(it.hasNext()) {
			Supplier s = it.next();
			
			if(s.getId().equals(id)) 
				return s;
		}
		
		return null;
	}
	
	public boolean contains(String id) {
        Iterator<Supplier> suppliers = supplierList.getSuppliers();
        while (suppliers.hasNext())
        {
            Supplier newSupplier = (Supplier)(suppliers.next());
            String checkId = newSupplier.getId();
            if (checkId.equals(id)) {
                return true;
            }
        }
        return false;
    }
	
	public Iterator<Supplier> getSuppliers() {
		return suppliers.iterator();
	}
	
	private void writeObject(ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(supplierList);
		}catch(IOException e) { e.printStackTrace(); }
	}
	
	private void readObject(ObjectInputStream input) {
		try {
			if(supplierList != null) return;
			else {
				input.defaultReadObject();
				if(supplierList == null) supplierList = (SupplierList) input.readObject();
				else input.readObject();
			}
		}catch(IOException e) { e.printStackTrace(); }
		 catch(ClassNotFoundException e) { e.printStackTrace(); }
	}
	
	public String toString() {
		return suppliers.toString();
	}

}