uniform sampler2D DiffuseSampler0;
uniform sampler2D MainSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 bloomBlur = texture(DiffuseSampler0, texCoord).rgb;
    vec3 mainColor = texture(MainSampler, texCoord).rgb;

    vec3 color = mainColor + bloomBlur;

    fragColor = vec4(color, 1.0);
}