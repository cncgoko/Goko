#version 330 core

uniform float zTop;
uniform float zCenter;
uniform float zBottom;

uniform vec4 colorTop;
uniform vec4 colorCenter;
uniform vec4 colorBottom;

// Interpolated values from the vertex shaders
in float zPos;

// Ouput data
out vec4 color;


void main(){
	if(zPos > zCenter){
		float p = (zTop - zPos) / ( (zTop - zCenter));
		color = (1-p) * colorTop + p * colorCenter; 
	}else{	                    
		float p = (zBottom - zPos) / (zBottom - zCenter);
		color = (1 - p) * colorBottom + p * colorCenter;
	}	
}