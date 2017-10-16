#version 330

layout(lines) in;
layout(triangle_strip, max_vertices = 6) out;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;

flat in vec4 vColor[];
flat out vec4 vFragColor;


void main() {
	float width = 0.1;

	vec4 up =vec4(modelViewMatrix[0][1], modelViewMatrix[1][1], modelViewMatrix[2][1] ,0);
	vec4 right =vec4(modelViewMatrix[0][0], modelViewMatrix[1][0], modelViewMatrix[2][0] ,0); 

	//vec4 lineDirection  = gl_in[1].gl_Position - gl_in[0].gl_Position;
	//vec4 cameraDirection = projectionMatrix*modelViewMatrix*vec4(0, 0, -1.0, 0.0);
	
	//vec4 cross = vec4(cross(vec3(lineDirection.xyz), vec3(cameraDirection.xyz)), 0);
	
    gl_Position = gl_in[0].gl_Position + (up + right) * width;
    vFragColor = vec4(1,0,0,1); 
    EmitVertex();

    gl_Position = gl_in[0].gl_Position + (up - right) * width;
    vFragColor = vec4(1,0,0,1); 
    EmitVertex();
    
    gl_Position = gl_in[1].gl_Position + (up - right) * width;
    vFragColor = vec4(1,0,0,1);
    EmitVertex();


    gl_Position = gl_in[1].gl_Position + (- up + right) * width;
    vFragColor = vec4(0,1,0,1);
    EmitVertex();
    
    gl_Position = gl_in[1].gl_Position + (-up - right) * width;
    vFragColor = vec4(0,1,0,1);
    EmitVertex();
    
    gl_Position = gl_in[0].gl_Position + (up + right) * width;
    vFragColor = vec4(0,1,0,1);  
    EmitVertex();
    
    EndPrimitive();
    
} 