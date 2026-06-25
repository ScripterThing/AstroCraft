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

mat3 yawRotation(float yaw) {
    float c = cos(yaw);
    float s = sin(yaw);

    return mat3(
    c, 0.0, -s,
    0.0, 1.0, 0.0,
    s, 0.0,  c
    );
}

#define INF (1.0/0.0)
#define PI 3.14159265359

#define UI0 1597334673U
#define UI1 3812015801U
#define UI2 uvec2(UI0, UI1)
#define UI3 uvec3(UI0, UI1, 2798796415U)
#define UIF (1.0 / float(0xffffffffU))

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

uniform float yawOffset;

uniform int shouldRender;
uniform float gameTime;

in vec2 texCoord;
out vec4 fragColor;

vec2 raySphere(vec3 sphereCentre, float sphereRadius, vec3 rayOrigin, vec3 rayDir) {
    vec3 offset = rayOrigin - sphereCentre;
    float a = 1; // Set to dot(rayDir, rayDir) if rayDir might not be normalized
    float b = 2 * dot(offset, rayDir);
    float c = dot (offset, offset) - sphereRadius * sphereRadius;
    float d = b * b - 4 * a * c; // Discriminant from quadratic formula

    // Number of intersections: 0 when d < 0; 1 when d = 0; 2 when d > 0
    if (d > 0) {
        float s = sqrt(d);
        float dstToSphereNear = max(0, (-b - s) / (2 * a));
        float dstToSphereFar = (-b + s) / (2 * a);

        // Ignore intersections that occur behind the ray
        if (dstToSphereFar >= 0) {
            return vec2(dstToSphereNear, dstToSphereFar - dstToSphereNear);
        }
    }
    // Ray did not intersect sphere
    return vec2(-1.0, 0);
}

vec2 getUvCoords(vec3 normal) {
    float u = atan(normal.z, normal.x) / (2.0 * PI) + 0.5;
    float v = acos(normal.y) / PI;
    return vec2(1.0-u, v);
}

vec3 hash33(vec3 p)
{
    uvec3 q = uvec3(ivec3(p)) * UI3;
    q = (q.x ^ q.y ^ q.z)*UI3;
    return vec3(q) * UIF;
}

vec3 voronoi(vec3 pos) {
    vec3 center = floor(pos);
    vec3 cells[27];
    cells[0] = center;
    cells[1] = center + vec3(0,0,1);
    cells[2] = center + vec3(0,1,0);
    cells[3] = center + vec3(0,1,1);
    cells[4] = center + vec3(1,0,0);
    cells[5] = center + vec3(1,0,1);
    cells[6] = center + vec3(1,1,0);
    cells[7] = center + vec3(1,1,1);
    cells[8] = center + vec3(0,0,-1);
    cells[9] = center + vec3(0,1,-1);
    cells[10] = center + vec3(1,0,-1);
    cells[11] = center + vec3(1,1,-1);
    cells[12] = center + vec3(0,-1,0);
    cells[13] = center + vec3(0,-1,1);
    cells[14] = center + vec3(1,-1,0);
    cells[15] = center + vec3(1,-1,1);
    cells[16] = center + vec3(0,-1,-1);
    cells[17] = center + vec3(1,-1,-1);
    cells[18] = center + vec3(-1,0,0);
    cells[19] = center + vec3(-1,0,1);
    cells[20] = center + vec3(-1,1,0);
    cells[21] = center + vec3(-1,1,1);
    cells[22] = center + vec3(-1,0,-1);
    cells[23] = center + vec3(-1,1,-1);
    cells[24] = center + vec3(-1,-1,0);
    cells[25] = center + vec3(-1,-1,1);
    cells[26] = center + vec3(-1,-1,-1);
    float minDist = 2.;
    vec3 closestPos = vec3(0);
    for(int i = 0; i < 27; i++) {
        vec3 cellHash = hash33(cells[i]) + cells[i];
        vec3 seperation = pos - cellHash;
        float sqrDist = dot(seperation,seperation);
        if(sqrDist < minDist) {
            minDist = sqrDist;
            closestPos = cellHash;
        }
    }
    return closestPos;
}

vec3 stars(int seed,vec3 dir, float starSpacing,float lightThreshold) {
    vec3 seedOffset = vec3(float(seed));
    vec3 closestStar = voronoi((dir+seedOffset)*starSpacing)/starSpacing-seedOffset;
    vec3 starHash = hash33(closestStar * starSpacing);
    float strength = starHash.x;
    float phaseOffset = starHash.y;
    strength = strength - 0.5 * strength * phaseOffset;
    float starProximity = dot(dir,normalize(closestStar));
    float star = smoothstep(13.0,14.,-log(1.-starProximity));
    vec3 result = vec3(star * strength);
    return result * smoothstep(vec3(lightThreshold/2.),vec3(lightThreshold),result);
}

void main() {
    vec3 color = texture(DiffuseSampler0, texCoord).rgb;

    if(shouldRender == 1) {
        vec3 viewDir = viewDirFromUv(texCoord) * yawRotation(radians(yawOffset));
        float rawDepth = texture(DiffuseDepthSampler, texCoord).r;

        //render sky
        if (rawDepth == 1.0) {
            int seed = 1;

            color = stars(seed, viewDir, 100.0, 1.27);
        }
    }

    fragColor = vec4(color, 1.0);
}
