package br.com.rest.resources.helper;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.coobird.thumbnailator.Thumbnails;

public class ImageHelper {

	public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static byte[] writeToByte(InputStream uploadedInputStream) {


		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		try {

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = uploadedInputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return buffer.toByteArray();
	}

	public static BufferedImage resizeImage(BufferedImage originalImage, int type){
		Image image = originalImage.getScaledInstance(300, 300, Image.SCALE_FAST);
		BufferedImage bufferedImage = new BufferedImage(300, 300, originalImage.getType());
	    Graphics2D g=bufferedImage.createGraphics();
	    g.drawImage(image,0,0,null);
	    g.dispose();
	    return bufferedImage;
    
	}

	public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

		BufferedImage resizedImage = new BufferedImage(300, 300, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, 300, 300, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
	}	

	public static byte[] imageXY(byte[] image, int x, int y){

		InputStream in = null;
		ByteArrayOutputStream out = null;

		try{

			in = new ByteArrayInputStream(image);
			out = new ByteArrayOutputStream();

			try {
				Thumbnails.of(in)
				.size(x, y)
				.toOutputStream(out);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			return out.toByteArray();

		}finally{
			in = null;
			out = null;
		} 
	}

}
