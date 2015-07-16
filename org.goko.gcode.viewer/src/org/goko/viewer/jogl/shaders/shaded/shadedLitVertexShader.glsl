#version 330 core

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec4 vertexPosition_modelspace;
layout(location = 1) in vec4 vertexColor;
layout(location = 3) in vec4 normal;

// Output data ; will be interpolated for each fragment.
out vec4 vColor;
out vec4 vNormal;
out vec4 position;
out vec4 lightPosition;

struct Material // user defined structure.
{
  vec4 ambient;
  vec4 diffuse;
  vec4 specular;
  float shininess;
};
 
uniform Material material;


void main(){ 
	gl_Position = projectionMatrix*modelViewMatrix*vertexPosition_modelspace; 
    position = vertexPosition_modelspace;
    
	// Color, simply pass it
	vColor = vertexColor;
	vNormal = normalize(normal);
	lightPosition = vec4(10.0,  0.0,  0.0, 1.0);	
}