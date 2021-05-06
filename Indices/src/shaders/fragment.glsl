#version 410

out vec4 FragColor;

uniform vec3 outColor;

void main() {
    FragColor = vec4(outColor, 1.0);
}