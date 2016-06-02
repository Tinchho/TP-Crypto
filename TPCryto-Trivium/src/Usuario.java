import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.Scanner;

import javax.imageio.ImageIO;

class Usuario {
	
	static Trivium cifrador = new Trivium();
	static String opcion;
	
	public static void main(String[] args) throws IOException {
		try {
			Scanner scaner = new Scanner(System.in);
			System.out.println("Encriptacion por Stream Cipher Trivium");
			System.out.println("Eliga Texto (t) o Imagen (i)");
			opcion = scaner.nextLine();
			System.out.println("Ingresar Archivo a encriptar:");
			String path = "";
			path = scaner.nextLine();
			System.out.println("Ingresar ruta resultado:");
			String result = "";
			result = scaner.nextLine();
			switch (opcion) {
			//Encriptacion del Texto
			case "t":
				
				//Se Abre el archivo y extrae el array de bytes
				File original = new File(path);
				FileInputStream fileInput = new FileInputStream(original);
				byte[] buffer = new byte[(int) original.length()];
				fileInput.read(buffer);
				byte[] encriptacion = new byte[(int) original.length()];
				
				//Se encripta el array de bytes
				encriptacion = encriptar(buffer);
				
				//Se graba en otro Archivo
				File encryp = new File(result);
				FileOutputStream fos = new FileOutputStream(encryp);
				fos.write(encriptacion);
				scaner.close();
				System.out.println("Terminado el proceso");
				break;
			case "i":
				
				//Se Abre la imagen y extrae el array de bytes
				BufferedImage imagen = ImageIO.read(new File(path));
				int w = imagen.getWidth();
				int h = imagen.getHeight();
				byte[] dataBuffByte = ((DataBufferByte) imagen.getRaster()
						.getDataBuffer()).getData();
				
				//Se encripta el array de bytes
				encriptacion = encriptar(dataBuffByte);
				
				//Se crea la nueva Imagen con el array de pixels encriptado
				DataBuffer bufferEncryp = new DataBufferByte(encriptacion,encriptacion.length);
				WritableRaster raster = Raster.createInterleavedRaster(bufferEncryp, w, h, 3 * w, 3, new int[] { 2, 1, 0 },(Point) null);
				ColorModel cm = new ComponentColorModel(ColorModel.getRGBdefault().getColorSpace(), false, true,Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
				BufferedImage resultado = new BufferedImage(cm, raster, true,null);
				ImageIO.write(resultado, "bmp", new FileOutputStream(result));
				
				System.out.println("Terminado el proceso");
				break;
			}

		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException");
		}
	}
	
	public static byte[] encriptar(byte[] bytes){
		byte[] byteEncryp = new byte[bytes.length];
	
		BitSet bitsOriginal = BitSet.valueOf(bytes);
		BitSet keyStream = new BitSet();
		if(opcion=="i"){
		 keyStream = cifrador.keystream(bitsOriginal.length()+16);
		//Le tengo que agregar dos bytes por que no se 
		//por que cuando lo pasa al array me toma dos byte menos y hace que rompa
		//Solo pasa con la imagenes
		}else{
		keyStream = cifrador.keystream(bitsOriginal.length());
		}
		keyStream.xor(bitsOriginal);
		byteEncryp = keyStream.toByteArray();
		return byteEncryp; 
	}
	

}
