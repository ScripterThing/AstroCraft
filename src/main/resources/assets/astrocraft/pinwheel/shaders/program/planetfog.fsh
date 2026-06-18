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

uniform vec3 pointPositions[32];
uniform float pointBrightnesses[32];
uniform float pointRadii[32];

uniform int pointLightCount;

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;
uniform sampler2D ShadowDepthSampler;
uniform sampler2D NormalSampler;
uniform sampler2D NoiseTex;

uniform int shouldRender;

uniform float lightDarkFactor;
uniform float fogFactor;
uniform vec3 fogColor;
uniform vec3 fogDarkColor;

in vec2 texCoord;

out vec4 fragColor;

uniform mat4 shadowViewMatrix;
uniform mat4 shadowOrthographMatrix;

#define SHADOW_SAMPLES 3

#define SHADOW_STRENGTH 0.75

#define VOL_LIGHT

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
    vec3 playerSpace = viewToPlayerSpace(viewPos) + VeilCamera.CameraBobOffset;
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


float random(vec2 p)
{
    return fract(sin(dot(p, vec2(12.9898,78.233))) * 43758.5453);
}

float IGN(vec2 pixel) {
    return mod(52.9829189 * mod(0.06711056 * pixel.x + 0.00583715 * pixel.y, 1.0), 1.0);
}

float attenuate_no_cusp(float dist, float radius) {
    float s = dist / radius;

    if (s >= 1.0) {
        return 0.0;
    }

    float oneMinusS = 1.0 - s;
    return oneMinusS * oneMinusS * oneMinusS;
}

vec3 shadow(vec3 inColor, vec3 viewPos, vec3 fogColor, float depth, float rawDepth) {
    vec3 color = inColor;

    vec3 ro = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;
    vec3 rd = viewDirFromUv(texCoord);

    int numSteps = 256;
    float maxDist = depth >= fogFactor ? fogFactor : depth;
    float stepDistance = maxDist / numSteps;

    float dist = 0.0;
    float transmittance = 1.0;
    vec3 scatteredLight = vec3(0.0);

    vec2 si = vec2(textureSize(DiffuseSampler, 0));
    float noise = IGN(texCoord * si);
    float t = noise * stepDistance;

    for(int i = 0; i < numSteps; i++) {
        vec3 rp = ro + rd * t;
        dist += stepDistance;

        if(dist >= depth) {
            break;
        }

        vec3 playerSpace = rp - ro;
        vec3 shadowScreenSpace = getShadowCoords(playerSpace, shadowViewMatrix, shadowOrthographMatrix);
        float shadowDepth = shadowScreenSpace.z;
        float shadowSampler = texture(ShadowDepthSampler, shadowScreenSpace.xy).r;

        float density = dist / fogFactor;

        float shadow = shadowDepth < shadowSampler ? 1.0 : 0.0;

        float pointLightAmount = 0;

        for(int i = 0; i < pointLightCount; i++) {
            vec3 lightPos = pointPositions[i].xyz;
            float lightBrightness = pointBrightnesses[i] * 2;
            float lightRadius = pointRadii[i];

            float dst = distance(lightPos, rp);
            float oi = attenuate_no_cusp(dst, lightRadius);

            pointLightAmount += oi * lightBrightness;
        }

        float lightAmount = shadow + pointLightAmount;

        vec3 inscatter = fogColor * lightAmount * density;
        scatteredLight += inscatter * transmittance * stepDistance;

        transmittance *= exp(-density * 1 * stepDistance);

        t += stepDistance;
    }

    color = color * transmittance + scatteredLight;
    color += (1.0 / 255.0) * noise;

    return color;
}

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;

    float rawDepth = texture(DepthSampler, texCoord).r;
    float depth = depthSampleToWorldDepth(rawDepth, texCoord);
    vec3 viewPos = screenToViewSpace(texCoord, depth).xyz;

    float d = min(max((VeilCamera.CameraPosition.y - 64.0) / 6.0, 0), 1);

    vec3 fogColor = mix(fogDarkColor, fogColor, lightDarkFactor);

    if(shouldRender == 1) {
//        float t = exp(-depth / fogFactor);
//        color = mix(fogColor, color, t);

        color = shadow(color, viewPos, fogColor, depth, rawDepth);
    }

    fragColor = vec4(color, 1.0);
}