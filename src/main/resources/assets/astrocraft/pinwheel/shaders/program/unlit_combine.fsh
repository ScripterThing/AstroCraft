uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform sampler2D UnlitSampler;
uniform sampler2D UnlitDepthSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;
    vec4 unlitColor = texture(UnlitSampler, texCoord);

    float depth = texture(DepthSampler, texCoord).r;
    float unlitDepth = texture(UnlitDepthSampler, texCoord).r;

    if(unlitDepth == depth) {
        color = mix(color, unlitColor.rgb, unlitColor.a);
        gl_FragDepth = unlitDepth;
    }

    fragColor = vec4(color, 1.0);
}
