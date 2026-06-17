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

uniform int shouldRender;

uniform float sunSize;
uniform float sunWhiteSize;

uniform mat4 shadowViewMatrix;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;

    float rawDepth = texture(DepthSampler, texCoord).r;

    if(rawDepth == 1 && shouldRender == 1) {
        vec3 lightangle = (shadowViewMatrix * vec4(0.0, 0.0, 1.0, 0.0)).xyz;
        lightangle.xy = -lightangle.xy;

        vec3 viewDir = viewDirFromUv(texCoord);

        float diff = max(dot(viewDir, normalize(lightangle)), 0);
        float sunOrg = pow(diff, sunSize);

        float sunWhite = pow(diff, sunWhiteSize);

        vec3 sunCol = vec3(0.988, 0.792, 0.427);
        vec3 centerColor = mix(vec3(1), sunCol, 0.3);
        sunCol = mix(sunCol, centerColor, sunWhite);

        color = mix(color, sunCol, sunOrg);
    }

    fragColor = vec4(color, 1.0);
}