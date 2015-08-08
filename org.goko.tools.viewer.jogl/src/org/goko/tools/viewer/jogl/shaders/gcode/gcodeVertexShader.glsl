#version 330

layout(location = 0)in vec4 vertexPosition_modelspace;
layout(location = 1)in vec4 vertexColor;
layout(location = 2)in int vertexStatus;
flat out vec4 vColor;
uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

void main(){
	// Output position of the vertex, in clip space : MVP * position
    gl_Position = projectionMatrix*modelViewMatrix*vertexPosition_modelspace; 
  	
  	 
    if(vertexStatus == 0){ // Normal
   		vColor = vertexColor;
    }else if(vertexStatus == 1){ // SENT
    	vColor = vec4(0.75,0.1,0.95,1); 
    }else if(vertexStatus == 2){ // EXECUTED
 	  	vColor = vec4(0.4,0.4,0.4,0.2);
    }else if(vertexStatus == 3){ // CONFIRMED
 	  	vColor = vec4(0.0,0.4,0.0,0.5);    
    }else if(vertexStatus == 5){ // ERROR
 	  	vColor = vec4(0.9,0.0,0.0,1);
    }else{
    	vColor = vec4(0,0.0,1,1);
    }
 }