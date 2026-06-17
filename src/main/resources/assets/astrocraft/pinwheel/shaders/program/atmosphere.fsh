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

struct Sphere {
    vec3 pos;
    float radius;

    float atmosphereRadius;
    float densityFallOff;

    vec3 scatteringCoefficients;
    float scatterStrength;

    vec3 color;
};

uniform sampler2D DiffuseSampler0;
uniform sampler2D DepthSampler;

uniform float gameTime;

uniform int sphereCount;

uniform vec4 worldOffset;

uniform vec3 positions[32];
uniform float radii[32];
uniform float atmosphereRadii[32];
uniform float densityFallOffs[32];
uniform vec3 wavelengthses[32];
uniform float scatteringStrengths[32];
uniform vec3 colors[32];

uniform int shouldRender;

in vec2 texCoord;

out vec4 fragColor;

const float epsilon = 0.0001;

const int numInScatteringPoints = 10;
const int numOpticalPoints = 10;

mat3 yawRotation(float yaw) {
    float c = cos(yaw);
    float s = sin(yaw);

    return mat3(
    c, 0.0, -s,
    0.0, 1.0, 0.0,
    s, 0.0,  c
    );
}

float densityAtPoint(vec3 densitySamplePoint, vec3 planetCenter, float planetRadius, float atmosphereRadius, float densityFallOff) {
    float heightAboveSurface = length(densitySamplePoint - planetCenter) - planetRadius;
    float height01 = heightAboveSurface / (atmosphereRadius - planetRadius);
    float localDensity = exp(-height01 * densityFallOff) * (1.0 - height01);
    return localDensity;
}

float opticalDepth(vec3 rayOrigin, vec3 rayDir, float rayLength, vec3 planetCenter, float planetRadius, float atmosphereRadius, float densityFallOff) {
    vec3 densitySamplePoint = rayOrigin;
    float stepSize = rayLength / float(numInScatteringPoints - 1);
    float opticalDepth = 0;

    for(int i = 0; i < numOpticalPoints; i++) {
        float localDensity = densityAtPoint(densitySamplePoint, planetCenter, planetRadius, atmosphereRadius, densityFallOff);
        opticalDepth += localDensity * stepSize;
        densitySamplePoint += rayDir * stepSize;
    }

    return opticalDepth;
}

vec3 calculateLight(vec3 rayOrigin, vec3 rayDir, float rayLength, vec3 dirToSun, vec3 planetCenter, float planetRadius, float atmosphereRadius, float densityFallOff, vec3 scatteringCoefficients, vec3 originalCol) {
    vec3 inScatterPoint = rayOrigin;
    float stepSize = rayLength / float(numInScatteringPoints - 1);
    vec3 inScatteredLight = vec3(0);
    float viewRayOpticalDepth = 0;

    for(int i = 0; i < numInScatteringPoints; i++) {
        float sunRayLength = raySphere(planetCenter, atmosphereRadius, inScatterPoint, dirToSun).y;

        float sunRayOpticalDepth = opticalDepth(inScatterPoint, dirToSun, sunRayLength, planetCenter, planetRadius, atmosphereRadius, densityFallOff);
        viewRayOpticalDepth = opticalDepth(inScatterPoint, -rayDir, stepSize * i, planetCenter, planetRadius, atmosphereRadius, densityFallOff);
        vec3 transmittance = exp(-(sunRayOpticalDepth + viewRayOpticalDepth) * scatteringCoefficients);

        float localDensity = densityAtPoint(inScatterPoint, planetCenter, planetRadius, atmosphereRadius, densityFallOff);

        inScatteredLight += localDensity * transmittance * scatteringCoefficients * stepSize;
        inScatterPoint += rayDir * stepSize;
    }

    float originalColorTransmittance = exp(-viewRayOpticalDepth);

    return originalCol * originalColorTransmittance + inScatteredLight;
}

void main() {
    vec4 info = texture(DiffuseSampler0, texCoord);

    float rawDepth = texture(DepthSampler, texCoord).r;
    float depth = depthSampleToWorldDepth(rawDepth, texCoord);

    vec3 camPos = worldOffset.xyz;

    vec3 color = info.rgb;
    if(shouldRender == 1) {
        float closDis = info.a;

        float sceneDepth = min(depth, closDis);

        vec3 rayOrigin = vec3(0);
        vec3 rayDir = viewDirFromUv(texCoord) * yawRotation(radians(worldOffset.w));

        Sphere closSphere;
        vec2 hitInfo = vec2(INF, INF);
        int sphereId;

        for (int i = 0; i < sphereCount; i++) {
            vec3 waveLengths = wavelengthses[i];
            float scatteringStrength = scatteringStrengths[i];

            float scatterR = pow(400.0 / waveLengths.r, 4) * scatteringStrength;
            float scatterG = pow(400.0 / waveLengths.g, 4) * scatteringStrength;
            float scatterB = pow(400.0 / waveLengths.b, 4) * scatteringStrength;
            vec3 scatterCoefficients = vec3(scatterR, scatterG, scatterB);

            Sphere sphere = Sphere(positions[i] - camPos, radii[i], atmosphereRadii[i] + radii[i], densityFallOffs[i], scatterCoefficients, scatteringStrength, colors[i]);

            vec2 hitInfo1 = raySphere(sphere.pos, sphere.atmosphereRadius, rayOrigin, rayDir);

            if (hitInfo1.x > -1.0 && hitInfo1.x < hitInfo.x) {
                closSphere = sphere;
                hitInfo = hitInfo1;
                sphereId = i;
            }
        }

        float dstToAtmosphere = hitInfo.x;

        float dstThroughAtmosphere = min(hitInfo.y, sceneDepth - dstToAtmosphere);

        if (rawDepth == 1 && dstToAtmosphere < INF && dstToAtmosphere >= VeilCamera.FarPlane)
            dstThroughAtmosphere = min(hitInfo.y, closDis - dstToAtmosphere);

        if (dstThroughAtmosphere > 0.0 && (dstToAtmosphere <= 100000 || sphereId == 1) && rawDepth == 1) {
            if (sphereId != 1) {
                vec3 pointInAtmosphere = rayOrigin + rayDir * (dstToAtmosphere + epsilon);

                vec3 sunPos = positions[1] - camPos;

                vec3 dirToSun = normalize(sunPos - closSphere.pos);

                vec3 light = calculateLight(
                    pointInAtmosphere,
                    rayDir,
                    dstThroughAtmosphere,
                    dirToSun,
                    closSphere.pos,
                    closSphere.radius,
                    closSphere.atmosphereRadius - epsilon * 2.0,
                    closSphere.densityFallOff,
                    closSphere.scatteringCoefficients, color
                );

                color = light;
            }
            else {
                float p = dstThroughAtmosphere / (closSphere.atmosphereRadius * 2.0);

                float fallOff = exp(-(1.0 - p) * 10.0);

                color = mix(color, closSphere.color, fallOff);
            }
        }
    }

    fragColor = vec4(color, 1.0);
}