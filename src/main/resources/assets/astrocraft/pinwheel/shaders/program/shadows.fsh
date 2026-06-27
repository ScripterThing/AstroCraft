#version 410 core
#define clipToWorldSpaceDirection clipToWorldSpace(vec4(pos, 0.0)).xyz
#define worldToScreenSpacePosition worldToScreenSpace(vec4(pos, 1.0)).xyz
#define viewToWorldSpacePosition viewToWorldSpace(vec4(pos, 1.0)).xyz
#define worldToLocalSpaceDirection worldToLocalSpace(vec4(pos, 0.0)).xyz
#define viewToScreenSpacePosition viewToScreenSpace(vec4(pos, 1.0))
#define viewToLocalSpaceDirection viewToLocalSpace(vec4(pos, 0.0)).xyz
#define clipToLocalSpacePosition clipToLocalSpace(vec4(pos, 1.0)).xyz
#define clipToLocalSpaceDirection clipToLocalSpace(vec4(pos, 0.0)).xyz
#define worldToScreenSpaceDirection worldToScreenSpace(vec4(pos, 0.0)).xyz
#define clipToScreenSpacePosition clipToScreenSpace(vec4(pos, 1.0))
#define viewToScreenSpaceDirection viewToScreenSpace(vec4(pos, 0.0))
#define viewToLocalSpacePosition viewToLocalSpace(vec4(pos, 1.0)).xyz
#define clipToViewSpaceDirection clipToViewSpace(vec4(pos, 0.0)).xyz
#define worldToLocalSpacePosition worldToLocalSpace(vec4(pos, 1.0)).xyz
#define worldToViewSpacePosition worldToViewSpace(vec4(pos, 1.0)).xyz
#define viewToClipSpaceDirection viewToClipSpace(vec4(pos, 0.0)).xyz
#define clipToWorldSpacePosition clipToWorldSpace(vec4(pos, 1.0)).xyz
#define worldToClipSpacePosition worldToClipSpace(vec4(pos, 1.0)).xyz
#define worldToViewSpaceDirection worldToViewSpace(vec4(pos, 0.0)).xyz
#define clipToViewSpacePosition clipToViewSpace(vec4(pos, 1.0)).xyz
#define worldToClipSpaceDirection worldToClipSpace(vec4(pos, 0.0)).xyz
#define viewToClipSpacePosition viewToClipSpace(vec4(pos, 1.0)).xyz
#define clipToScreenSpaceDirection clipToScreenSpace(vec4(pos, 0.0))
#define viewToWorldSpaceDirection viewToWorldSpace(vec4(pos, 0.0)).xyz
layout(shared) uniform VeilBuffer538496897 {
    mat4 ProjMat;
    mat4 IProjMat;
    mat4 ViewMat;
    mat4 IViewMat;
    mat3 IViewRotMat;
    vec3 CameraPosition;
    float NearPlane;
    vec3 CameraBobOffset;
    float FarPlane;
} VeilCamera;
float getFov() {
    return (2.0 * atan((1.0 / VeilCamera.ProjMat[1][1])));
}
float getAspectRatio() {
    return (VeilCamera.ProjMat[1][1] / VeilCamera.ProjMat[0][0]);
}
vec4 worldToViewSpace(vec4 pos) {
    vec4 viewSpacePos = (VeilCamera.ViewMat * (pos - vec4(VeilCamera.CameraPosition, 0.0)));
    return (viewSpacePos / viewSpacePos.w);
}
vec4 worldToLocalSpace(vec4 pos) {
    return (pos - vec4(VeilCamera.CameraPosition, 0.0));
}
vec4 worldToClipSpace(vec4 pos) {
    vec4 viewSpacePos = (VeilCamera.ViewMat * (pos - vec4(VeilCamera.CameraPosition, 0.0)));
    return (VeilCamera.ProjMat * (viewSpacePos / viewSpacePos.w));
}
vec3 worldToScreenSpace(vec4 pos) {
    vec4 viewSpacePos = (VeilCamera.ViewMat * (pos - vec4(VeilCamera.CameraPosition, 0.0)));
    vec4 clipSpace = (VeilCamera.ProjMat * (viewSpacePos / viewSpacePos.w));
    clipSpace.xyz /= clipSpace.w;
    return vec3(((clipSpace.xy + vec2(1.0)) / 2.0), clipSpace.z);
}
vec4 viewToWorldSpace(vec4 pos) {
    return (vec4(VeilCamera.CameraPosition, 0.0) + (VeilCamera.IViewMat * pos));
}
vec4 viewToLocalSpace(vec4 pos) {
    return (VeilCamera.IViewMat * pos);
}
vec4 viewToClipSpace(vec4 pos) {
    return (VeilCamera.ProjMat * pos);
}
vec3 viewToScreenSpace(vec4 pos) {
    vec4 clipSpace = (VeilCamera.ProjMat * pos);
    clipSpace.xyz /= clipSpace.w;
    return vec3(((clipSpace.xy + vec2(1.0)) / 2.0), clipSpace.z);
}
vec4 clipToWorldSpace(vec4 pos) {
    return (vec4(VeilCamera.CameraPosition, 0.0) + ((VeilCamera.IViewMat * VeilCamera.IProjMat) * pos));
}
vec4 clipToLocalSpace(vec4 pos) {
    return ((VeilCamera.IViewMat * VeilCamera.IProjMat) * pos);
}
vec4 clipToViewSpace(vec4 pos) {
    return (VeilCamera.IProjMat * pos);
}
vec3 clipToScreenSpace(vec4 pos) {
    pos.xyz /= pos.w;
    return vec3(((pos.xy + vec2(1.0)) / 2.0), pos.z);
}
vec4 screenToWorldSpace(vec3 pos) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(pos.xy, pos.z, 1.0) * 2.0) - 1.0));
    return (vec4(VeilCamera.CameraPosition, 0.0) + (VeilCamera.IViewMat * (viewSpacePos / viewSpacePos.w)));
}
vec4 screenToLocalSpace(vec3 pos) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(pos.xy, pos.z, 1.0) * 2.0) - 1.0));
    return (VeilCamera.IViewMat * (viewSpacePos / viewSpacePos.w));
}
vec4 screenToViewSpace(vec3 pos) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(pos.xy, pos.z, 1.0) * 2.0) - 1.0));
    return (viewSpacePos / viewSpacePos.w);
}
vec4 screenToClipSpace(vec3 pos) {
    return ((vec4(pos.xy, pos.z, 1.0) * 2.0) - 1.0);
}
vec4 screenToWorldSpace(vec2 uv, float depth) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(uv, depth, 1.0) * 2.0) - 1.0));
    return (vec4(VeilCamera.CameraPosition, 0.0) + (VeilCamera.IViewMat * (viewSpacePos / viewSpacePos.w)));
}
vec4 screenToLocalSpace(vec2 uv, float depth) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(uv, depth, 1.0) * 2.0) - 1.0));
    return (VeilCamera.IViewMat * (viewSpacePos / viewSpacePos.w));
}
vec4 screenToViewSpace(vec2 uv, float depth) {
    vec4 viewSpacePos = (VeilCamera.IProjMat * ((vec4(uv, depth, 1.0) * 2.0) - 1.0));
    return (viewSpacePos / viewSpacePos.w);
}
vec4 screenToClipSpace(vec2 uv, float depth) {
    return ((vec4(uv, depth, 1.0) * 2.0) - 1.0);
}
vec3 viewDirFromUv(vec2 uv) {
    return normalize(screenToLocalSpace(uv, 1.0).xyz);
}
float depthSampleToWorldDepth(float depthSample, vec2 texCoord) {
    vec3 pos = screenToLocalSpace(texCoord, depthSample).xyz;

    return length(pos);
}

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D ShadowDepthSampler;
uniform sampler2D NormalSampler;
uniform sampler2D NoiseTex;
uniform sampler2D AlbedoSampler;
uniform sampler2D AlbedoDepthSampler;
uniform sampler2D HandSampler;
uniform sampler2D HandDepthSampler;
uniform sampler2D HandNormalSampler;
uniform sampler2D ParticlesSampler;
uniform sampler2D ParticlesDepthSampler;
uniform sampler2D UnlitDepthSampler;

uniform int shouldRender;
uniform int inSpace;
uniform float brightness;
uniform vec3 lightColor;

uniform mat4 shadowViewMatrix;
uniform mat4 shadowSpaceMatrix;
uniform mat4 shadowOrthographMatrix;

in vec2 texCoord;

out vec4 fragColor;

#define SHADOW_SAMPLES 3

#define SHADOW_STRENGTH 0.75

vec3 viewToPlayerSpace(vec3 v)
{
    return mat3(VeilCamera.IViewMat) * v;
}

vec3 distort(in vec3 shadowPosition) {
    const float bias0 = 0.95;
    const float bias1 = 1.0 - bias0;

    float factorDistance = length(shadowPosition.xy);

    float distortFactor = factorDistance * bias0 + bias1;

    return shadowPosition * vec3(vec2(1.0 / distortFactor), 0.2);
}

mat2 randRotMat(vec2 coord, sampler2D NoiseTex){
    float randomAngle = texture(NoiseTex, coord * 20.0).r * 100.0;
    float cosTheta = cos(randomAngle);
    float sinTheta = sin(randomAngle);
    return mat2(cosTheta, -sinTheta, sinTheta, cosTheta) / 2048;
}

vec3 getShadowCoords(vec4 normal, vec3 viewPos, mat4 viewMatrix, mat4 orthographMatrix){
    vec3 playerSpace = viewToPlayerSpace(viewPos);
    vec3 adjustedPlayerSpace = playerSpace + 0.01 * viewToPlayerSpace(normal.xyz) * length(viewPos);
    vec3 shadowViewPos = (viewMatrix * vec4(adjustedPlayerSpace, 1.0)).xyz;
    vec4 homogenousPos = orthographMatrix * vec4(shadowViewPos, 1.0);
    vec3 shadowNdcPos = homogenousPos.xyz / homogenousPos.w;
    vec3 distortedNdcSpace = distort(shadowNdcPos);
    vec3 shadowScreenSpace = distortedNdcSpace * 0.5 + 0.5;
    shadowScreenSpace.z = shadowScreenSpace.z - 0.0001;

    return shadowScreenSpace;
}

vec3 getShadowCoords(vec3 playerSpace, mat4 viewMatrix, mat4 orthographMatrix){
    vec3 shadowViewPos = (viewMatrix * vec4(playerSpace, 1.0)).xyz;
    vec4 homogenousPos = orthographMatrix * vec4(shadowViewPos, 1.0);
    vec3 shadowNdcPos = homogenousPos.xyz / homogenousPos.w;
    vec3 distortedNdcSpace = distort(shadowNdcPos);
    vec3 shadowScreenSpace = distortedNdcSpace * 0.5 + 0.5;
    shadowScreenSpace.z = shadowScreenSpace.z - 0.0001;

    return shadowScreenSpace;
}

float luminance(vec3 p) {
    return (p.r + p.g + p.b) / 3.0f;
}

vec4 getShadow(vec4 incolor, vec2 texCoord, vec3 viewPos, vec4 normal, mat4 viewMatrix, mat4 orthographMatrix, sampler2D NoiseTex, sampler2D ShadowSampler, float depth2) {
    float worldDepth = length(viewPos);
    vec3 orgColor = texture(DiffuseSampler, texCoord).rgb;
    vec4 color = incolor;

    float unlitDepth = texture(UnlitDepthSampler, texCoord).r;

    if(unlitDepth == depth2)
            return incolor;

    vec3 fwd = vec3(viewMatrix[0][2], viewMatrix[1][2], viewMatrix[2][2]);
    vec3 lightDir = normalize(-fwd);

    vec3 normalWorld = normalize(mat3(VeilCamera.IViewMat) * normal.xyz);

    float diff = dot(normalize(-lightDir), normalWorld);
    diff = max(diff, 0.1);

    vec3 playerSpace = viewToPlayerSpace(viewPos);
    vec3 shadowScreenSpace = getShadowCoords(playerSpace, viewMatrix, orthographMatrix);

    float shadowDepth = shadowScreenSpace.z;

    float shadowSum = 0.0;
    mat2 randRotation = randRotMat(texCoord, NoiseTex);
    for (int x = -SHADOW_SAMPLES; x <= SHADOW_SAMPLES; x++) {
        for (int y = -SHADOW_SAMPLES; y <= SHADOW_SAMPLES; y++) {
            vec2 offset = randRotation * vec2(x, y);
            float shadowSampler = texture(ShadowSampler, shadowScreenSpace.xy + offset).r;

            if (shadowDepth < shadowSampler) {
                shadowSum += 1.0;
            }
        }
    }

    shadowSum /= pow(2.0 * SHADOW_SAMPLES + 1.0, 2.0);

    float particleDepth = texture(ParticlesDepthSampler, texCoord).r;
    vec4 particles = texture(ParticlesSampler, texCoord);

    vec4 hCol = texture(HandSampler, texCoord);
    vec3 handColor = hCol.rgb;

    vec3 albedoColor = hCol.a == 0 ? texture(AlbedoSampler, texCoord).rgb : handColor;

    float lightBrightness = brightness;

    if(particleDepth < 1 && particleDepth <= depth2) {
        albedoColor = particles.rgb;
        diff = 1.0;
        lightBrightness /= 2;
    }

    vec3 shadowed = orgColor * (1.0 - SHADOW_STRENGTH);
    vec3 inSpaceColor = mix(shadowed, albedoColor * lightColor * lightBrightness, shadowSum * diff);
    vec3 outOfSpaceColor = mix(shadowed, orgColor * lightColor * lightBrightness, shadowSum * diff);

    float lumInSpace = luminance(inSpaceColor);
    float lumOrg = luminance(orgColor);

    vec3 finalInSpaceCol = lumInSpace > lumOrg ? inSpaceColor : orgColor;
    vec3 finalColor = inSpace == 1 ? finalInSpaceCol : outOfSpaceColor;

    color.rgb = finalColor;

    return color;
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    float handDepth = texture(HandDepthSampler, texCoord).r;
    float albedoDepth = texture(AlbedoDepthSampler, texCoord).r;

    vec4 normal = albedoDepth < handDepth ? texture(NormalSampler, texCoord) : texture(HandNormalSampler, texCoord);
    float depth = texture(DepthSampler, texCoord).r;
    vec3 viewPos = screenToViewSpace(texCoord, depth).xyz;

    if(shouldRender == 1)
        color = getShadow(color, texCoord, viewPos, normal, shadowViewMatrix, shadowOrthographMatrix, NoiseTex, ShadowDepthSampler, depth);

    fragColor = color;
}