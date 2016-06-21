import java.util.ArrayList;
import java.util.BitSet;

public class Trivium {

	ArrayList<Boolean> registro1 = new ArrayList<Boolean>();
	ArrayList<Boolean> registro2 = new ArrayList<Boolean>();
	ArrayList<Boolean> registro3 = new ArrayList<Boolean>();
	BitSet key = new BitSet(80);
	BitSet iv = new BitSet(80);
	boolean t1, t2, t3;

	public Trivium() {
		this.key.set(0);
		this.key.set(1);
		this.key.set(2);
		this.key.set(90);
		this.key.set(78);
		this.key.set(79); // XXX Basicamente la clave tiene tres "1" al
							// principio, tres al final
		this.iv.set(0);
		this.iv.set(79); // XXX El vector de inicializacion tiene un "1" al principio y otro al final

		//this.key.set(0);
		inicializarTrivium();
	}

	// FASE 1 DEL ALGORITMO ---> INICIANDO EL ESTADO INTERNO
	private void inicializarTrivium() {

		for (int i = 0; i <= 79; i++) {
			registro1.add(i, key.get(i));

		}
		for (int j = 80; j <= 92; j++) {
			registro1.add(j, false);

		}
		for (int k = 0; k <= 79; k++) {
			registro2.add(k, iv.get(k));

		}
		for (int l = 80; l <= 83; l++) {
			registro2.add(l, false);

		}
		for (int m = 0; m <= 107; m++) {
			registro3.add(m, false);

		}
		registro3.add(108, true);
		registro3.add(109, true);
		registro3.add(110, true);
		
		for (int z = 0; z < 4*288 ; z++){
			t1 = registro1.get(65) ^ (registro1.get(90) & registro1.get(91)) ^ registro1.get(92) ^ registro2.get(77);
			t2 = registro2.get(68) ^ (registro2.get(81) & registro2.get(82)) ^ registro2.get(83) ^ registro3.get(86);
			t3 = registro3.get(65) ^ (registro3.get(108) & registro3.get(109)) ^ registro3.get(110) ^ registro1.get(68);
			registro1.remove(92);
			registro1.add(0, t3);
			registro2.remove(83);
			registro2.add(0, t1);
			registro3.remove(110);
			registro3.add(0, t2);
			
		} 
		System.out.println("Trivium Iniciado");

	}

	// FASE 2: KEYSTREAM GENERATION
	public BitSet keystream(int i) {
		BitSet z = new BitSet(i);
		for (int x = 0; x <= i; x++) {
			t1 = registro1.get(65) ^ registro1.get(92);
			t2 = registro2.get(68) ^ registro2.get(83);
			t3 = registro3.get(65) ^ registro3.get(110);
			if (t1 ^ t2 ^ t3) {
				z.set(x);
			}
			t1 = t1 ^ (registro1.get(90) & registro1.get(91)) ^ registro2.get(77);
			t2 = t2 ^ (registro2.get(81) & registro2.get(82)) ^ registro3.get(86);
			t3 = t3 ^ (registro3.get(108) & registro3.get(109)) ^ registro1.get(68);
			registro1.remove(92);
			registro1.add(0, t3);
			registro2.remove(83);
			registro2.add(0, t1);
			registro3.remove(110);
			registro3.add(0, t2);
		}
		//System.out.println(bytesToHex(z.toByteArray())); //FIXME PARA PRUEBA: C:\Users\Tincho\Desktop\prueba\cero.txt
		return z;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
