/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.tools.viewer.jogl.utils.render.text.v1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.media.opengl.GL3;

import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.log.GkLog;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;

public class BitmapFontFile {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(BitmapFontFile.class);
	/** Consolas font */
	public static final String CONSOLAS = "/org/goko/viewer/jogl/utils/render/text/Consolas.bff";
	public static final String CONSOLAS_512 = "/org/goko/viewer/jogl/utils/render/text/Consolas_512.bff";
	public static final String CONSOLAS_1024 = "/org/goko/viewer/jogl/utils/render/text/Consolas_1024.bff";
	public static final String CONSOLAS_2048 = "/org/goko/viewer/jogl/utils/render/text/Consolas_2048.bff";
	private int textureWidth;
	private int textureHeight;
	private int cellWidth;
	private int cellHeight;
	private int columnCount;
	private int bitsPerPixel;
	private int firstCharOffset;
	private int[] charWidth;
	private ByteBuffer pix;
	private Texture texture;

	public BitmapFontFile(String file){
		try {
			charWidth = new int[256];
			load(file);
		} catch (GkTechnicalException e) {
			LOG.error(e);
		}
	}

	protected void load(String bffFileName) throws GkTechnicalException{
		try {

			URL url = new URL("platform:/plugin/org.goko.tools.viewer.jogl"+bffFileName);
			InputStream uStream = url.openConnection().getInputStream();
			    //BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			//InputStream uStream = getClass().getResourceAsStream(bffFileName);
			byte[] head = new byte[20];

			// Read header
			int bytesRead = uStream.read(head, 0, 20);
			
			// Check header size is correct
			if(bytesRead < 20)
			{
				throw new IOException("BFF File : Header read failed");
			}

			ByteBuffer headBuf = ByteBuffer.wrap(head);

			// Check header
			int h0 = getUnsignedByteVal(headBuf.get());
			int h1 = getUnsignedByteVal(headBuf.get());

			// Check header signature
			if(h0 != 0xBF || h1 != 0xF2)
			{
				uStream.close();
				throw new IOException("BFF File : Bad header signature");
			}
			
			// Get image width and height
			textureWidth  = flipEndian(headBuf.getInt());
			textureHeight = flipEndian(headBuf.getInt());
			
			// Get cell dimensions
			cellWidth  = flipEndian(headBuf.getInt());
			cellHeight = flipEndian(headBuf.getInt());
			
			// Sanity check (prevent divide by zero)
			if(cellWidth <= 0 || cellHeight <= 0)
			{
				throw new IOException("Invalid header content");
			}

			// Pre-calculate column count
			columnCount = textureWidth / cellWidth;

			// Get colour depth
			bitsPerPixel = getUnsignedByteVal(headBuf.get());

			// Get base offset
			firstCharOffset = getUnsignedByteVal(headBuf.get());
			
			// Read width information
			for(int wLoop = 0; wLoop < 256; ++wLoop)
			{
				charWidth[wLoop] = uStream.read(); // read() returns an unsigned byte, unlike the overloaded versions.  :sLOG.info("BFF Debug - charWidth["+wLoop+"] (aka "+ ((char)wLoop)+") = "+charWidth[wLoop]);
			}

			// Get bitmap
			int bitLen = (textureHeight * textureWidth) * (bitsPerPixel / 8);
			byte bits[] = new byte[bitLen];
			
			int received = 0;
			do{
				// Loop to force complete reading, since read doesn't garantee to read all the buffer at once
				received += uStream.read(bits, received, bitLen - received);
			}while(uStream.available() > 0);



			// Flip image scanlines and wrap in Bytebuffer for glTexImage2D
			pix = ByteBuffer.allocate(bits.length);
			pix.rewind();
			int lineLen = textureWidth * (bitsPerPixel / 8);
			
			for(int lines = textureHeight-1; lines > 0 ; --lines){
				pix.put(bits, lines * lineLen, lineLen);
			}
			pix.rewind();
			uStream.close();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	protected Texture getTexture(GL3 gl){

		if(texture == null){
			TextureData tData = new TextureData(gl.getGLProfile(),    //GLProfile glp,
					   GL3.GL_RGBA,     	  //int internalFormat,
			           getTextureWidth(),     //int width,
			           getTextureHeight(), 	  //int height,
			           0,                     //int border,
			           GL3.GL_RGBA,           //int pixelFormat,
			           GL3.GL_UNSIGNED_BYTE,  //int pixelType,
			           false,                 //boolean mipmap,
			           false,                 //boolean dataIsCompressed,
			           false,                 //boolean mustFlipVertically,
			           getBuffer(),		       //Buffer buffer,
			           null);                 //TextureData.Flusher flusher)
			texture = new Texture(gl, tData);
		}
		return texture;
	}

	public ByteBuffer getBuffer(){
		return pix;
	}
	/**
	 * Flip endian-ness of a 32 bit integer value.
	 * @param val the value to flip
	 * @return the filpped value
	 */
	private int flipEndian(int val) {
		return  (val >>> 24) | (val << 24) | ((val << 8) & 0x00FF0000) | ((val >> 8) & 0x0000FF00);
	}

	/**
	 * Bodge to get unsigned byte values
	 * @param val the value
	 * @return the uint value
	 */
	private int getUnsignedByteVal(byte val) {
		if(val < 0) {
			return 256 + val;
		} else {
			return val;
		}
	}

	/**
	 * @return the textureWidth
	 */
	public int getTextureWidth() {
		return textureWidth;
	}

	/**
	 * @param textureWidth the textureWidth to set
	 */
	public void setTextureWidth(int textureWidth) {
		this.textureWidth = textureWidth;
	}

	/**
	 * @return the textureHeight
	 */
	public int getTextureHeight() {
		return textureHeight;
	}

	/**
	 * @param textureHeight the textureHeight to set
	 */
	public void setTextureHeight(int textureHeight) {
		this.textureHeight = textureHeight;
	}

	/**
	 * @return the cellWidth
	 */
	public int getCellWidth() {
		return cellWidth;
	}

	/**
	 * @param cellWidth the cellWidth to set
	 */
	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}

	/**
	 * @return the cellHeight
	 */
	public int getCellHeight() {
		return cellHeight;
	}

	/**
	 * @param cellHeight the cellHeight to set
	 */
	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the bitsPerPixel
	 */
	public int getBitsPerPixel() {
		return bitsPerPixel;
	}

	/**
	 * @param bitsPerPixel the bitsPerPixel to set
	 */
	public void setBitsPerPixel(int bitsPerPixel) {
		this.bitsPerPixel = bitsPerPixel;
	}

	/**
	 * @return the firstCharOffset
	 */
	public int getFirstCharOffset() {
		return firstCharOffset;
	}

	/**
	 * @param firstCharOffset the firstCharOffset to set
	 */
	public void setFirstCharOffset(int firstCharOffset) {
		this.firstCharOffset = firstCharOffset;
	}

	/**
	 * @return the pix
	 */
	public ByteBuffer getPix() {
		return pix;
	}

	/**
	 * @param pix the pix to set
	 */
	public void setPix(ByteBuffer pix) {
		this.pix = pix;
	}

	public int getCharWidth(char c){
		return charWidth[c];
	}
}
