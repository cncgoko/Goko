#version 330 core

// Interpolated values from the vertex shaders
in vec4 vColor;
in vec4 vNormal;
in vec4 position;

in vec4 oLight0Position;
in vec4 oLight0Diffuse;
in vec4 oLight0Ambient;

in vec4 oLight1Position;
in vec4 oLight1Diffuse;
in vec4 oLight1Ambient;

in vec3 oMaterialDiffuse;
in vec3 oMaterialAmbient;
in vec3 oMaterialSpecular;

// Ouput data
out vec4 color;

const float shininess = 16.0;
const float screenGamma = 2.2; // Assume the monitor is calibrated to the sRGB color space

void main(){
	vec4 light0Direction = normalize(position- oLight0Position);	 
	vec4 light1Direction = normalize(position- oLight1Position);	
	float light0Incidence = max(dot(vNormal,light0Direction), 0);
	float light1Incidence = max(dot(vNormal,light1Direction), 0);
	
	vec4 reflectionDirection = 2*(dot(light0Direction, vNormal)*vNormal)-light0Direction;
	
	color = vec4(oMaterialAmbient.xyz,1) * (oLight0Ambient + oLight1Ambient) + 
			vec4(oMaterialDiffuse,1) * (oLight0Diffuse*light0Incidence + oLight1Diffuse*light1Incidence);// +
			//vec4(oMaterialSpecular,1) * max(dot(normalize(vec4(0,1,0,0)),reflectionDirection),0) ;
			// TODO : add specular
	color.w = 1;	
}