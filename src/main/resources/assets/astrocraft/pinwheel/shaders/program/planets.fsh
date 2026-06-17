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

#define INF (1.0/0.0)
#define PI 3.14159265359

mat3 yawRotation(float yaw) {
    float c = cos(yaw);
    float s = sin(yaw);

    return mat3(
    c, 0.0, -s,
    0.0, 1.0, 0.0,
    s, 0.0,  c
    );
}

mat3 rotateX(float a) {
    float c = cos(a), s = sin(a);
    return mat3(1, 0, 0,
    0, c,-s,
    0, s, c);
}

mat3 rotateY(float a) {
    float c = cos(a), s = sin(a);
    return mat3( c, 0, s,
    0, 1, 0,
    -s, 0, c);
}

mat3 rotateZ(float a) {
    float c = cos(a), s = sin(a);
    return mat3( c,-s, 0,
    s, c, 0,
    0, 0, 1 );
}

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

float intersectPlane(vec3 ro, vec3 rd, vec3 p0, vec3 n) {
    float denom = dot(n, rd);
    if (abs(denom) < 1e-5) return -1.0;  // parallel; no hit
    float t = dot(p0 - ro, n) / denom;
    return (t > 0.0) ? t : -1.0;
}

struct Ring {
    vec3  center;
    vec3  normal;        // usually (0,1,0)
    float innerRadius;
    float outerRadius;
};

float intersectRing(vec3 ro, vec3 rd, Ring ring) {

    // intersect ray with ring plane
    float t = intersectPlane(ro, rd, ring.center, ring.normal);
    if (t < 0.0) return -1.0; // no intersection

    // compute hit point
    vec3 hit = ro + rd * t;

    // distance from center in the ring plane
    vec3 v = hit - ring.center;

    // radial distance (project onto plane)
    float r = length(v - ring.normal * dot(v, ring.normal));

    // check within annulus boundaries
    if (r < ring.innerRadius || r > ring.outerRadius)
    return -1.0;

    return t;
}

vec2 getRingUV(vec3 hit, vec3 ringCenter, vec3 ringNormal, float innerRadius, float outerRadius){
    // 1. Flatten hit point into ring plane
    vec3 v = hit - ringCenter;
    vec3 flat1 = v - ringNormal * dot(v, ringNormal);

    // 2. Build tangent basis (uDir, vDir)
    // Choose arbitrary vector not parallel to ringNormal
    vec3 temp = abs(ringNormal.y) < 0.99 ? vec3(0.0, 1.0, 0.0) : vec3(1.0, 0.0, 0.0);

    vec3 uDir = normalize(cross(ringNormal, temp));
    vec3 vDir = normalize(cross(ringNormal, uDir));

    // 3. Convert hit point to 2D ring coordinates
    float x = dot(flat1, uDir);
    float y = dot(flat1, vDir);

    float r = length(flat1);
    float angle = atan(y, x);      // radians (-PI, PI)

    // 4. Normalize to 0–1 UV space
    float U = angle / (2.0 * PI);
    U = fract(U);                  // wrap

    float V = (r - innerRadius) / (outerRadius - innerRadius);
    V = clamp(V, 0.0, 1.0);

    return vec2(U, V);
}


vec2 getUvCoords(vec3 normal) {
    float u = atan(normal.z, normal.x) / (2.0 * PI) + 0.5;
    float v = acos(normal.y) / PI;
    return vec2(1.0-u, v);
}

struct Sphere {
    vec3 pos;
    float radius;

    vec3 col;

    float atmosphereRadius;
};

uniform vec3 positions[32];
uniform vec3 colors[32];

uniform float radii[32];

uniform float atmosphereRadii[32];

uniform sampler2D EarthTex;
uniform sampler2D EarthNormal;
uniform sampler2D EarthClouds;

uniform sampler2D SaturnTex;
uniform sampler2D SaturnRingTex;

uniform sampler2D VenusTex;
uniform sampler2D VenusAtmosTex;

uniform sampler2D UranusTex;
uniform sampler2D UranusRingTex;

uniform sampler2D JupiterTex;
uniform sampler2D MarsTex;
uniform sampler2D MercuryTex;
uniform sampler2D NeptuneTex;

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

uniform int shouldRender;

uniform int sphereCount;

uniform float gameTime;

uniform vec4 worldOffset;

in vec2 texCoord;

out vec4 fragColor;

vec3 getTexRGB(int planetId, vec3 normal) {
    vec2 uv = getUvCoords(normal);

    switch(planetId) {
        case 0:
            uv.x -= gameTime / 10000.0;
            return texture(EarthTex, uv).rgb;
        case 2:
            uv.x -= gameTime / 10000.0;
            return texture(MercuryTex, uv).rgb;
        case 3:
            uv.x -= gameTime / 10000.0;
            return texture(VenusTex, uv).rgb;
        case 4:
            uv.x -= gameTime / 10000.0;
            return texture(MarsTex, uv).rgb;
        case 5:
            uv.x -= gameTime / 10000.0;
            return texture(JupiterTex, uv).rgb;
        case 6:
            uv.x -= gameTime / 10000.0;
            return texture(SaturnTex, uv).rgb;
        case 7:
            uv.x -= gameTime / 10000.0;
            return texture(UranusTex, uv).rgb;
        case 8:
            uv.x -= gameTime / 10000.0;
            return texture(NeptuneTex, uv).rgb;
    }

    return vec3(1.0);
}

vec3 sampleNormal(int planetId, vec2 uv) {
    if (planetId == 0) {
        uv.x -= gameTime / 1000.0;
        vec3 n = texture(EarthNormal, uv).xyz;
        return normalize(n * 2.0 - 1.0);
    }

    return vec3(0.0, 0.0, 1.0);
}

mat3 sphereTBN(vec3 normal) {
    vec3 tangent = normalize(cross(vec3(0.0, 1.0, 0.0), normal));

    // If normal is parallel to Y axis, fallback
    if (length(tangent) < 0.001)
    tangent = normalize(cross(vec3(1.0, 0.0, 0.0), normal));

    vec3 bitangent = cross(normal, tangent);

    return mat3(tangent, bitangent, normal);
}

vec3 applyNormalMap(int planetId, vec3 hitNormal) {
    vec2 uv = getUvCoords(hitNormal);

    // Sample tangent space normal
    vec3 tNormal = sampleNormal(planetId, uv);

    // Build TBN
    mat3 TBN = sphereTBN(hitNormal);

    // Transform into world space
    return normalize(TBN * tNormal);
}

void handleUranusRing(vec3 rayOrigin, vec3 rayDir, float closDis, float depth, float rawDepth, vec3 camPos, inout vec3 color) {
    float vert = radians(0.0);
    float horiz = radians(-60.0);

    mat3 R = rotateZ(horiz) * rotateX(vert);

    // Tilted ring normal
    vec3 tiltedNormal = normalize(R * vec3(0.0, 1.0, 0.0));

    vec3 center = positions[7] - camPos;

    Ring ring = Ring(center, tiltedNormal, radii[7]*1.6, radii[7]*2.5);

    float saturnOuterResult = intersectRing(rayOrigin, rayDir, ring);

    if(saturnOuterResult > -1.0 && saturnOuterResult < closDis && (saturnOuterResult < depth || rawDepth == 1)) {
        vec3 hitPoint = rayOrigin + rayDir * saturnOuterResult;

        vec3 sunPos = positions[1] - camPos;
        vec3 sunDir = normalize(sunPos - hitPoint);

        vec2 result = raySphere(center, radii[7], hitPoint, sunDir);

        vec2 uv = getRingUV(hitPoint, ring.center, ring.normal, ring.innerRadius, ring.outerRadius);

        vec4 ringCol = texture(UranusRingTex, uv);


        if(result.x == -1.0) {
            color = mix(color, ringCol.rgb, ringCol.a);
        }
        else {
            color = mix(color, vec3(0), ringCol.a);
        }
    }
}

void handleSaturnRing(vec3 rayOrigin, vec3 rayDir, float closDis, float depth, float rawDepth, vec3 camPos, inout vec3 color) {
    float vert = radians(0.0);
    float horiz = radians(-20.0);

    mat3 R = rotateZ(horiz) * rotateX(vert);

    // Tilted ring normal
    vec3 tiltedNormal = normalize(R * vec3(0.0, 1.0, 0.0));

    vec3 center = positions[6] - camPos;

    Ring ring = Ring(center, tiltedNormal, radii[6]*1.6, radii[6]*4);

    float saturnOuterResult = intersectRing(rayOrigin, rayDir, ring);

    if(saturnOuterResult > -1.0 && saturnOuterResult < closDis && (saturnOuterResult < depth || rawDepth == 1)) {
        vec3 hitPoint = rayOrigin + rayDir * saturnOuterResult;

        vec3 sunPos = positions[1] - camPos;
        vec3 sunDir = normalize(sunPos - hitPoint);

        vec2 result = raySphere(center, radii[6], hitPoint, sunDir);

        vec2 uv = getRingUV(hitPoint, ring.center, ring.normal, ring.innerRadius, ring.outerRadius);

        vec4 ringCol = texture(SaturnRingTex, uv);

        if(result.x == -1.0) {
            color = mix(color, ringCol.rgb, ringCol.a);
        }
        else {
            color = mix(color, vec3(0), ringCol.a);
        }
    }
}

void main() {
    vec3 color = texture(DiffuseSampler, texCoord).rgb;

    float closDis = INF;

    if(shouldRender == 1) {
        float rawDepth = texture(DepthSampler, texCoord).r;
        float depth = depthSampleToWorldDepth(rawDepth, texCoord);

        vec3 camPos = worldOffset.xyz;

        vec3 rayOrigin = vec3(0);
        vec3 rayDir = viewDirFromUv(texCoord) * yawRotation(radians(worldOffset.w));

        Sphere closSphere;
        int sphereId;

        for (int i = 0; i < sphereCount; i++) {
            Sphere currSphere = Sphere(positions[i] - camPos, radii[i], colors[i], atmosphereRadii[i]);

            vec2 result = raySphere(currSphere.pos, currSphere.radius, rayOrigin, rayDir);

            if (result.x > -1.0 && result.x < closDis) {
                closDis = result.x;
                closSphere = currSphere;
                sphereId = i;
            }
        }

        if (closDis < INF && rawDepth == 1) {
            vec3 hitPoint = rayOrigin + rayDir * closDis;
            vec3 baseNormal = normalize(hitPoint - closSphere.pos);

            vec3 sunPos = positions[1] - camPos;

            vec3 lightDir = normalize(sunPos - closSphere.pos);

            vec3 normal = applyNormalMap(sphereId, baseNormal);

            float diff = dot(normal, normalize(lightDir));

            vec3 texCol = getTexRGB(sphereId, baseNormal);

            if (sphereId != 1)
                color = closSphere.col * texCol * diff;
            else
                color = closSphere.col * texCol;

            if (sphereId == 0) {
                vec2 uv = getUvCoords(baseNormal);
                uv.x -= gameTime / 10000;
                float clouds = texture(EarthClouds, uv).r;
                color = mix(color, vec3(1.0) * diff, clouds);
            }
            else if (sphereId == 3) {
                vec2 uv = getUvCoords(baseNormal);
                uv.x -= gameTime / 10000;
                vec3 atmos = texture(VenusAtmosTex, uv).rgb;
                color = mix(color, atmos * diff, 0.8);
            }
        }

        handleSaturnRing(rayOrigin, rayDir, closDis, depth, rawDepth, camPos, color);
        handleUranusRing(rayOrigin, rayDir, closDis, depth, rawDepth, camPos, color);
    }

    fragColor = vec4(color, closDis);
}