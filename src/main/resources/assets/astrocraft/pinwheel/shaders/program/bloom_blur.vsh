uniform vec2 InSize = vec2(1);
uniform vec2 OutSize = vec2(1);
uniform vec2 BlurDir = vec2(1.0, 0.0);

out vec2 texCoord;
out vec2 sampleStep;

void main() {
    vec2 uv = vec2(gl_VertexID & 1, gl_VertexID & 2);
    gl_Position = vec4(uv * vec2(3.0) - vec2(1.0), 0.0, 1.0);

    vec2 oneTexel = 1.0 / InSize;
    sampleStep = oneTexel * BlurDir;

    texCoord = uv * vec2(1.5);
}