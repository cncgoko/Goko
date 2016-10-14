#version 330 core

// Interpolated values from the vertex shaders
in vec2 UV;
in vec4 vColor;
in vec3 vCharChannel;

// Ouput data
out vec4 color;

vec4 tmpColor;
// Values that stay constant for the whole mesh.
uniform sampler2D fontTextureSampler;

void main(){
	tmpColor = vec4(vCharChannel * texture2D( fontTextureSampler, UV ).rgb, 1);
	tmpColor.a = tmpColor.r + tmpColor.g + tmpColor.b; 	
	
	if(tmpColor.a < 0.01){		
		discard;		
	}else{
		color = vColor;
	}	
	color.a = tmpColor.a * vColor.a;
}