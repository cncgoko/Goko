#version 330 core

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec4 vertexPosition_modelspace;

// Output data ; will be interpolated for each fragment.
out float zPos;

void main(){ 
	gl_Position = projectionMatrix*modelViewMatrix*vertexPosition_modelspace; 
    
	// Color, simply pass it
	zPos = vertexPosition_modelspace.z;
}