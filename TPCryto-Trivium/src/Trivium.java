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
		this.key.set(40);
		this.key.set(77);
		this.key.set(78);
		this.key.set(79); // XXX Basicamente la clave tiene tres "1" al
							// principio, tres al final y uno al medio
		this.iv.set(0);
		this.iv.set(39);
		this.iv.set(40);
		this.iv.set(41);
		this.iv.set(79); // XXX El vector de inicializacion tiene tres "1" en el
							// medio, uno al principio y otro al final

		inicializarTrivium();
	}

	// FASE 1 DEL ALGORITMO ---> INICIANDO EL ESTADO INTERNO
	private void inicializarTrivium() {

		for (int i = 0; i <= key.length() - 1; i++) {
			registro1.add(i, key.get(i));

		}
		for (int j = key.length(); j <= 92; j++) {
			registro1.add(j, false);

		}
		for (int k = 0; k <= iv.length() - 1; k++) {
			registro2.add(k, iv.get(k));

		}
		for (int l = iv.length(); l <= 83; l++) {
			registro2.add(l, false);

		}
		for (int m = 0; m <= 107; m++) {
			registro3.add(m, false);

		}
		registro3.add(108, true);
		registro3.add(109, true);
		registro3.add(110, true);

		for (int z = 0; z <= 4; z++) {
			t1 = (boolean) ((registro1.get(65) ^ registro1.get(90)) & (registro1
					.get(91) ^ registro1.get(92) ^ registro2.get(77)));
			t2 = (boolean) ((registro2.get(68) ^ registro2.get(81)) & (registro2
					.get(82) ^ registro2.get(83) ^ registro3.get(86)));
			t3 = (boolean) ((registro3.get(65) ^ registro3.get(108)) & (registro3
					.get(109) ^ registro3.get(110) ^ registro1.get(68)));
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
			t1 = (boolean) (registro1.get(65) ^ registro1.get(92));
			t2 = (boolean) (registro2.get(68) ^ registro2.get(83));
			t3 = (boolean) (registro3.get(65) ^ registro3.get(110));
			if (t1 ^ t2 ^ t3) {
				z.set(x);
			}
			t1 = (boolean) (t1 ^ registro1.get(90) ^ registro1.get(91) ^ registro2
					.get(77));
			t2 = (boolean) (t2 ^ registro2.get(81) ^ registro2.get(82) ^ registro3
					.get(86));
			t3 = (boolean) (t3 ^ registro3.get(108) ^ registro3.get(109) ^ registro1
					.get(68));
			registro1.remove(92);
			registro1.add(0, t3);
			registro2.remove(83);
			registro2.add(0, t1);
			registro3.remove(110);
			registro3.add(0, t2);
		}
		return z;
	}

}
