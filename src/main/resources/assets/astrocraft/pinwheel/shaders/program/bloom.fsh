uniform sampler2D DiffuseSampler0;
uniform sampler2D MainSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 bloomBlur = texture(DiffuseSampler0, texCoord);
    vec3 mainColor = texture(MainSampler, texCoord).rgb;

    vec3 color = mainColor + bloomBlur.r;

    fragColor = vec4(color, 1.0);
}