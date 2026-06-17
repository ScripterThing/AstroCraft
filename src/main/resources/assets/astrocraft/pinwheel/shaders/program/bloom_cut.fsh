uniform sampler2D DiffuseSampler0;
uniform sampler2D DepthSampler;

in vec2 texCoord;

out vec4 fragColor;

float conv(float x) {
    return exp((x*3)-3);
}

void main() {
    vec3 color = texture(DiffuseSampler0, texCoord).rgb;

    float avg = (color.r + color.g + color.b) / 3.0;
    float weighted = conv(avg);

    if(weighted > 0.8) {
        fragColor = vec4(1);
        return;
    }

    fragColor = vec4(0);
}