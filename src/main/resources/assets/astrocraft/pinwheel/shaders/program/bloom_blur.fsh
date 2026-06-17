uniform sampler2D DiffuseSampler0;
uniform sampler2D DepthSampler;

in vec2 sampleStep;
in vec2 texCoord;

uniform float Radius = 10.0;
uniform float RadiusMultiplier = 1.0;

out vec4 fragColor;

void main() {
    vec4 blurred = vec4(0.0);
    float actualRadius = round(Radius * RadiusMultiplier);
    for (float a = -actualRadius + 0.5; a <= actualRadius; a += 2.0) {
        blurred += texture(DiffuseSampler0, texCoord + sampleStep * a);
    }
    blurred += texture(DiffuseSampler0, texCoord + sampleStep * actualRadius) / 2.0;
    fragColor = blurred / (actualRadius + 0.5);
}