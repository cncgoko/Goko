#version 330 core

// Interpolated values from the vertex shaders
in vec4 vColor;
in vec4 vNormal;
in vec4 position;

in vec4 lightPosition;

// Ouput data
out vec4 color;

vec4 tmpColor;

void main(){
	vec4 lightDirection = normalize(position- lightPosition);
	color = 0.4*vColor + 0.6*vec4(1,1,1,1)*dot(normalize(vNormal),lightDirection);
	color.w = 1;
}