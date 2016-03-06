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

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL3;

import org.apache.commons.lang3.StringUtils;

import com.jogamp.opengl.util.texture.Texture;

public class BitmapFontFileManager {
	private static BitmapFontFileManager instance;
	private Map<EnumBitmapFont, BitmapFontFile> mapBffByFontName;

	private BitmapFontFileManager(){
		mapBffByFontName = new HashMap<EnumBitmapFont, BitmapFontFile>();
	}

	public static BitmapFontFileManager getInstance(){
		if(instance == null){
			instance = new BitmapFontFileManager();
		}
		return instance;
	}

	public static BitmapFontFile getBitmapFontFile(EnumBitmapFont font, int size){
		return getInstance().getBitmapFontFileInternal(font, size);
	}

	public static Texture getTextureFont(GL3 gl, EnumBitmapFont font, int size){
		return getInstance().getTextureFontInternal(gl, font, size);
	}

	private Texture getTextureFontInternal(GL3 gl, EnumBitmapFont font, int size) {
		BitmapFontFile bff = getBitmapFontFileInternal(font, size);
		return bff.getTexture(gl);
	}

	private BitmapFontFile getBitmapFontFileInternal(EnumBitmapFont font, int size){
		if(!mapBffByFontName.containsKey(font)){
			String path = StringUtils.replace(font.getFilepath(),"$size",String.valueOf(size));
			BitmapFontFile file = new BitmapFontFile(path);
			mapBffByFontName.put(font, file);
		}
		return mapBffByFontName.get(font);
	}

}
