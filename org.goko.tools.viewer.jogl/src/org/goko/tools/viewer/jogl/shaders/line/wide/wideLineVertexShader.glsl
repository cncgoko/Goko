#version 330

layout(location = 0)in vec4 vertexPosition_modelspace;
layout(location = 1)in vec4 vertexColor;

flat out vec4 vFragColor;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
	// Output position of the vertex, in clip space : MVP * position
    gl_Position = projectionMatrix*modelViewMatrix*vertexPosition_modelspace; 
   	vFragColor = vertexColor;  
 }