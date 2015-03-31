#version 330 core

// Interpolated values from the vertex shaders
in vec4 vColor;

// Ouput data
out vec4 color;

vec4 tmpColor;

void main(){
	color = vColor;	
}