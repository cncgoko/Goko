#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 vColor;

// Ouput data
out vec4 color;

vec4 tmpColor;
// Values that stay constant for the whole mesh.
uniform sampler2D fontTextureSampler;

void main(){
	tmpColor =  vColor * texture2D( fontTextureSampler, UV ).rgba;
	
	if(tmpColor.a < 0.15){		
		discard;
	}
	color = tmpColor;
}