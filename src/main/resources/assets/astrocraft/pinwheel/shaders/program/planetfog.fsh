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

uniform sampler3D BlockGrid;
uniform vec3 GridOrigin;

#define VOXELSHADOW_GRID_SIZE 64
#define VOXELSHADOW_MAX_STEPS 128

float voxelshadowVisibility(vec3 fragPos, vec3 lightPos) {
    vec3 startG = fragPos - GridOrigin;
    vec3 endG = lightPos - GridOrigin;
    vec3 delta = endG - startG;
    float rayLen = length(delta);
    if (rayLen < 0.001) return 1.0;

    vec3 rDir = delta / rayLen;
    ivec3 cell = ivec3(floor(startG));
    ivec3 iStep = ivec3(sign(rDir));

    vec3 invAbs = 1.0 / max(abs(rDir), vec3(1e-5));
    vec3 tDelta = invAbs;

    vec3 cellF = vec3(cell);
    vec3 tMax;
    tMax.x = (rDir.x >= 0.0) ? (cellF.x + 1.0 - startG.x) * invAbs.x : (startG.x - cellF.x) * invAbs.x;
    tMax.y = (rDir.y >= 0.0) ? (cellF.y + 1.0 - startG.y) * invAbs.y : (startG.y - cellF.y) * invAbs.y;
    tMax.z = (rDir.z >= 0.0) ? (cellF.z + 1.0 - startG.z) * invAbs.z : (startG.z - cellF.z) * invAbs.z;

    for (int i = 0; i < VOXELSHADOW_MAX_STEPS; i++) {
        if (any(lessThan(cell, ivec3(0))) || any(greaterThanEqual(cell, ivec3(VOXELSHADOW_GRID_SIZE)))) break;
        if (i > 0 && texelFetch(BlockGrid, cell, 0).r > 0.5) return 0.0;

        if (tMax.x < tMax.y && tMax.x < tMax.z) {
            if (tMax.x >= rayLen) break;
            tMax.x += tDelta.x;
            cell.x += iStep.x;
        } else if (tMax.y < tMax.z) {
            if (tMax.y >= rayLen) break;
            tMax.y += tDelta.y;
            cell.y += iStep.y;
        } else {
            if (tMax.z >= rayLen) break;
            tMax.z += tDelta.z;
            cell.z += iStep.z;
        }
    }

    return 1.0;
}

uniform vec3 pointPositions[10];
uniform float pointBrightnesses[10];
uniform float pointRadii[10];

uniform int pointLightCount;

uniform vec3 spotLightPositions[10];
uniform vec2 spotLightRotations[10];
uniform float spotLightRadii[10];
uniform float spotLightDistances[10];
uniform float spotLightBrightnesses[10];

uniform int spotLightCount;

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

vec3 spotDirection(vec2 rot)
{
    float pitch = radians(rot.x);
    float yaw = radians(rot.y);

    return normalize(vec3(
                     -sin(yaw) * cos(pitch),
                     -sin(pitch),
                     cos(yaw) * cos(pitch)
                     ));
}

float sampleSpotLight(
    vec3 samplePos,
    vec3 lightPos,
    vec2 lightRot,
    float radius,
    float maxDistance,
    float brightness
)
{
    vec3 forward = spotDirection(lightRot);

    // WORLD SPACE
    vec3 toSample = samplePos - lightPos;
    float dist = length(toSample);

    if (dist > maxDistance)
    return 0.0;

    vec3 dirToSample = toSample / dist;

    float coneAngle = atan(radius / maxDistance);
    float coneCos = cos(coneAngle);

    // light-space cone test
    float cosTheta = dot(forward, dirToSample);

    if (cosTheta < coneCos)
    return 0.0;

    float atten = attenuate_no_cusp(dist, maxDistance);

    float angular = smoothstep(coneCos, 1.0, cosTheta);

    return atten * angular * brightness;
}

vec3 shadow(vec3 inColor, vec3 viewPos, vec3 fogColor, float depth, float rawDepth) {
    vec3 color = inColor;

    vec3 ro = VeilCamera.CameraPosition + VeilCamera.CameraBobOffset;
    vec3 rd = viewDirFromUv(texCoord);

    float blend = smoothstep(18.0, 22.0, depth);

    int numSteps = int(round(mix(50.0, 500.0, blend)));

    float maxDist = mix(depth, fogFactor, blend);
    float stepDistance = maxDist / numSteps;

    float dist = 0.0;
    float transmittance = 1.0;
    vec3 scatteredLight = vec3(0.0);

    vec2 si = vec2(textureSize(DiffuseSampler, 0));
    float noise = IGN(texCoord * si);

    float noise22 = fract(sin(dot(texCoord, vec2(12.9898,78.233))) * 43758.5453);

    float rayOffset = noise22 * stepDistance;

    for(float t = rayOffset; t < maxDist; t += stepDistance) {
        vec3 rp = ro + rd * t;

        if(t >= depth) {
            break;
        }

        vec3 playerSpace = rp - ro;
        vec3 shadowScreenSpace = getShadowCoords(playerSpace, shadowViewMatrix, shadowOrthographMatrix);
        float shadowDepth = shadowScreenSpace.z;
        float shadowSampler = texture(ShadowDepthSampler, shadowScreenSpace.xy).r;

        float density = t / fogFactor;

        float shadow = shadowDepth < shadowSampler ? 1.0 : 0.0;

        float veilLightsAmount = 0;

        for(int i = 0; i < pointLightCount; i++) {
            vec3 lightPos = pointPositions[i].xyz;
            float lightBrightness = pointBrightnesses[i] * 2;
            float lightRadius = pointRadii[i];

            float dst = distance(lightPos, rp);
            float oi = attenuate_no_cusp(dst, lightRadius);

            veilLightsAmount += oi * lightBrightness;
        }

        for(int i = 0; i < spotLightCount; i++) {
            vec3 lightPos = spotLightPositions[i].xyz;
            vec2 lightRot = spotLightRotations[i].xy;
            float lightRadius = spotLightRadii[i];
            float lightDistance = spotLightDistances[i];
            float lightBrightness = spotLightBrightnesses[i];

            float res = sampleSpotLight(rp, lightPos, lightRot, lightRadius, lightDistance, lightBrightness);

            veilLightsAmount += res;
        }

        float lightAmount = shadow + veilLightsAmount;

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
