uniform sampler2D DiffuseSampler0;
uniform sampler2D DepthSampler;

in vec2 sampleStep;
in vec2 texCoord;

uniform float Radius = 10.0;
uniform float RadiusMultiplier = 1.0;

out vec4 fragColor;

uniform float weight[5] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216);
void main() {
    vec2 tex_offset = vec2(1.0) / textureSize(DiffuseSampler0, 0); // gets size of single texel
    vec3 result = texture(DiffuseSampler0, texCoord).rgb * weight[0]; // current fragment's contribution

    for(int i = 1; i < 5; ++i)
    {
        result += texture(DiffuseSampler0, texCoord + vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
        result += texture(DiffuseSampler0, texCoord - vec2(tex_offset.x * i, 0.0)).rgb * weight[i];
    }

    fragColor = vec4(result, 1.0);
}