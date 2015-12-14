#version 330 core

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;
// First light
uniform vec4 iLight0Position;
uniform vec4 iLight0Diffuse;
uniform vec4 iLight0Ambient;
// Second light
uniform vec4 iLight1Position; 
uniform vec4 iLight1Diffuse; 
uniform vec4 iLight1Ambient;

struct Material{
	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

uniform Material material;

// Input vertex data, different for all executions of this shader.
layout(location = 0) in vec4 vertexPosition_modelspace;
layout(location = 1) in vec4 vertexColor;
layout(location = 3) in vec4 normal;

// Output data ; will be interpolated for each fragment.
out vec4 vColor;
out vec4 vNormal;
out vec4 position;

out vec4 oLight0Position;
out vec4 oLight0Diffuse;
out vec4 oLight0Ambient;

out vec4 oLight1Position;
out vec4 oLight1Diffuse;
out vec4 oLight1Ambient;

out vec3 oMaterialDiffuse;
out vec3 oMaterialAmbient;
out vec3 oMaterialSpecular;

void main(){ 

	gl_Position = projectionMatrix*modelViewMatrix*vertexPosition_modelspace; 
    position = modelViewMatrix*vertexPosition_modelspace;
    
	// Color, simply pass it
	vColor = vertexColor;
	vNormal = normalize(modelViewMatrix*normal);
	oLight0Position = iLight0Position;	
	oLight0Diffuse = iLight0Diffuse;
	oLight0Ambient = iLight0Ambient;
	
	oLight1Position = iLight1Position;	
	oLight1Diffuse = iLight1Diffuse;
	oLight1Ambient = iLight1Ambient;
	
	oMaterialAmbient = material.ambient;
	oMaterialDiffuse = material.diffuse;	
	oMaterialSpecular = material.specular; 
}